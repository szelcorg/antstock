package org.szelc.app.antstock.view.quotes;

import org.szelc.app.antstock.a.AntStock;
import org.szelc.app.antstock.repository.TransactionRepository;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.view.quotes.table.QuotesTableView;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.factory.TransactionServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.quotes.enumeration.QuoteFilterEnum;


/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class QuotesViewController implements Initializable {

    private static final Logger log = Logger.getLogger(QuotesViewController.class.toString());
    private static final Boolean SET_DATE_LAST_SESSION_AFTER_SERCHING = false;
    private ObservableList<DayCompanyQuote> quotesDataList;
    @FXML
    private QuotesTableView quotesTableView;
    @FXML
    private QuoteFilterView quoteFilterView;
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        log.info("INITIALIZE QuotesViewController rb [" + rb + "]");

        QuoteFilterEnum filter = quoteFilterView.getChoicedType();
        if (QuoteFilterEnum.EVALUATED_OR_TRANSACTIONED.equals(filter) || QuoteFilterEnum.TRANSACTIONED_OR_EVALUATED.equals(filter)) {
            quoteFilterView.setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true));
        } else if (QuoteFilterEnum.ALL.equals(filter)) {
            quoteFilterView.setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompaniesCiagleAll(true));
        }
        //quoteFilterView.setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true));
        addEventForQuoteFilterView();

        setQuotesOnTableView();
        
        AntStock.setController(this);

    }

    private void setQuotesOnTableView() {
        TransactionRepository tdc = TransactionServiceFactory.instance().getTransactionService().getTransactionRepository();
        List<DayCompanyQuote> quotesList = new ArrayList();
        List<DayCompanyQuote> dcqList;
        log.info("quoteFilterView ["+quoteFilterView+"]");
        log.info("quoteFilterView.getCompanies ["+quoteFilterView.getCompanies()+"]");
        QuoteRepository quoteRepository = QuoteServiceFactory.instance().getQuoteService().getQuoteRepository(quoteFilterView.getCompanies());
        for (String company : quoteFilterView.getCompanies()) {
            dcqList = quoteRepository.getCompanyQuotesMap().get(company);
            if (dcqList == null) {
                log.warn("BRAK NOTOWAN DLA SPOLKI [" + company + "]");
                continue;
            }
            int size = dcqList.size();
            DayCompanyQuote quote = dcqList.get(size - 1);
            if(dcqList.size()>1){
                DayCompanyQuote prevQuote = dcqList.get(size - 2);
                quote.setReference(prevQuote.getCourse());
                quotesList.add(quote);
            }
            else {
                quote.setReference(0);
                
            }
        }

        quotesDataList = FXCollections.observableArrayList(quotesList);

        quotesTableView.setItems(quotesDataList);
    }
    
   

   private void addEventForQuoteFilterView() {
        Button filterBtn = quoteFilterView.getFilterBtn();

        filterBtn.setOnAction((ActionEvent event) -> {
            
            List<DayCompanyQuote> filtered;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.toPattern());
            LocalDate ld = quoteFilterView.getDate().getValue();
            log.info("LOCAL DATE [" + ld + "]");
            Date date = null;
         
            if (ld != null) {
                try {
                    date = Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.parse(ld.format(formatter));
                } catch (ParseException ex) {
                    log.error(ex.getMessage());
                    System.exit(0);
                }
            }
            log.info("Quotes filter ["+quoteFilterView.getChoicedType()+"]");
            if (quoteFilterView.getChoicedType().equals(QuoteFilterEnum.SINGLE)) {
                String company = quoteFilterView.getCompanyName().toUpperCase();
                filtered = QuoteServiceFactory.instance().getQuoteService().getDayCompanyQuote(company, date);
            } else if (quoteFilterView.getChoicedType().equals(QuoteFilterEnum.ALL)) {
                if (date == null) {
                    date = QuoteServiceFactory.instance().getQuoteService().getLastQuote("TAURONPE").getDate();
                }
                Set<String> companySet = CompanyServiceFactory.instance().getCompanyService().getCompaniesCiagleAll(true);
                filtered = QuoteServiceFactory.instance().getQuoteService().getDayQuotesList(companySet, date);                 
                      
            } else if (quoteFilterView.getChoicedType().equals(QuoteFilterEnum.EVALUATED_OR_TRANSACTIONED)) {
                if (date == null) {
                    date = QuoteServiceFactory.instance().getQuoteService().getLastQuote("TAURONPE").getDate();
                }
                log.info("EvaluatedOrTransactioned");
                Set<String> companySet = CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true);
                filtered = QuoteServiceFactory.instance().getQuoteService().getDayQuotesList(companySet, date);
            } else {
                filtered = null;
            }
            log.info("Filtered size ["+filtered.size()+"]");
            if (SET_DATE_LAST_SESSION_AFTER_SERCHING && filtered.size() > 0) {
                log.info("FILTERED SIZE [" + filtered.size() + "]");
                quoteFilterView.getDate().setValue(filtered.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if(filtered==null){
                quotesTableView.setItems(null);
            }
            quotesTableView.setItems(FXCollections.observableArrayList(filtered));
        });
    }
}
