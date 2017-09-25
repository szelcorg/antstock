
package org.szelc.app.antstock.view.statistic;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.quotes.QuotesStatistic;
import org.szelc.app.antstock.view.quotes.QuotesStatisticRepository;
import org.szelc.app.antstock.view.statistic.table.StatisticTableView;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;

/**
 *
 * @author szelc.org
 */
public class StatisticViewController implements Initializable, TableUpdateEvent {

    private static final Logger log = Logger.getLogger(StatisticViewController.class.toString());
    
    @FXML
    private StatisticTableView statisticView;

    private ObservableList<QuotesStatistic> quotesStatisticList = null;

    private QuoteRepository quoteRepository;

    @FXML
    private DatePicker dateFromDP;

    @FXML
    private DatePicker dateToDP;

    @FXML
    private Button showBtn;
//
//   
//    private TransactionRepository transactionRepository;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("StatisticViewController initializing ...");
        quoteRepository = QuoteServiceFactory.instance().getQuoteService().getQuoteRepository();
        log.info("QR size ["+quoteRepository.getDayQuotesMap().keySet().size()+"]");
        Date dateFrom = null;
        Date dateTo = null;
        try {
            dateFrom = Settings.STATISTIC_VIEW_DATE_FORMAT.parse(Settings.STATISTIC_START_DATE_STR);
            dateTo = new Date();
        } catch (ParseException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
        dateFromDP.setValue(dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dateToDP.setValue(dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        showStatisticTable(dateFrom, dateTo);
        
        
        showBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                try {
                    log.info("showBtn action on evaluate");
                    SimpleDateFormat sdfDD = new SimpleDateFormat("dd.MM.yyyy");                   
                    showStatisticTable(sdfDD.parse(dateFromDP.getEditor().getText()),
                            sdfDD.parse(dateToDP.getEditor().getText()));                    
                } catch (ParseException ex) {
                    ex.printStackTrace();
            System.exit(0);
                }
            }
        });
    }
    
   
    
    private void showStatisticTable(Date dateFrom, Date dateTo) {
        long t1 = System.currentTimeMillis();
        Set<String> companySet = new HashSet();
//        companySet.add("TAURONPE");
//        companySet.add("ASSECOPOL");
        //companySet = CompanyServiceFactory.instance().getCompanyService().getCompanyEvaluated();
        companySet = CompanyServiceFactory.instance().getCompanyService().getCompaniesCiagleAll(true);
        QuotesStatisticRepository qsr = new QuotesStatisticRepository(companySet, quoteRepository, dateFrom, dateTo);
        quotesStatisticList = FXCollections.observableArrayList(qsr.getStatisticList());
        statisticView.setItems(quotesStatisticList);

        log.info("Statistic loaded time ["+(System.currentTimeMillis() - t1) +"] ms");
    }

    public DatePicker getDateFromDP() {
        return dateFromDP;
    }

    public void setDateFromDP(DatePicker dateFromDP) {
        this.dateFromDP = dateFromDP;
    }

    public DatePicker getDateToDP() {
        return dateToDP;
    }

    public void setDateToDP(DatePicker dateToDP) {
        this.dateToDP = dateToDP;
    }
    
    
    

    @Override
    public void update() {
       
    }

}
