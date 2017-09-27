package org.szelc.app.antstock.view.transaction;

import org.szelc.app.antstock.repository.TransactionRepository;
import org.szelc.app.antstock.view.transaction.comparator.TransactionDateComparator;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.persistence.trasaction.TransactionPersistence;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import org.szelc.app.antstock.view.transaction.creator.TransactionCreatorView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.szelc.app.antstock.view.transaction.table.TransactionTableView;
import org.szelc.app.antstock.view.transaction.table.filter.TransactionFilterView;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.factory.TransactionServiceFactory;
import org.szelc.app.antstock.util.string.StringUtil;
import org.szelc.app.antstock.view.listener.SystemListener;



/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class TransactionViewController implements Initializable, TableUpdateEvent {

    private static final Logger log = Logger.getLogger(TransactionViewController.class.toString());
   
    @FXML
    private TransactionTableView transactionTable;
    @FXML
    private TransactionCreatorView transactionCreatorView;
    @FXML
    private TransactionFilterView transactionFilterView;
    private ObservableList<Transaction> transactionsDataList = null;
    private TransactionRepository transactionRepository;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        log.info("TransactionViewController initializing ...");
        setTransactionsOnTableView(new TransactionFilter());
        addEventForTransactionCreatorView();
        addEventForTransactionFilterView();
        addBankToCombo();
    }

    private void addBankToCombo() {
        final ObservableList<BankEnum> bankList = FXCollections.observableArrayList(Arrays.asList(BankEnum.values())
        );
        transactionCreatorView.setItems(bankList);
    }

    private void setTransactionsOnTableView(TransactionFilter filter) {
        transactionRepository = TransactionServiceFactory.instance().getTransactionService().getTransactionRepository();
        transactionsDataList = FXCollections.observableArrayList(transactionRepository.getTransactionDataList());
        transactionTable.setItems(FXCollections.observableArrayList((transactionsDataList)));

        transactionFilterView.setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true));
        transactionCreatorView.setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true));
    }

    private void addEventForTransactionCreatorView() {
        Button addTransactionBtn = transactionCreatorView.getAddTransactionButton();
        Button delTransactionBtn = transactionCreatorView.getDelTransactionButton();

        transactionTable.setTableUpdate(this);

        addTransactionBtn.setOnAction((ActionEvent event) -> {
            log.info("addEventForTransaction");
            Transaction data = transactionCreatorView.getTransactionData();
            transactionsDataList.add(data);
            transactionTable.getItems().add(0,
                    data);
            persistTransaction();
            SystemListener.getInstance().addTransactionEvent();
        });

        delTransactionBtn.setOnAction((ActionEvent event) -> {
            log.info("delEventForTransaction");

            ObservableList<Transaction> selectedData = transactionTable.getSelectedItems();
            Iterator<Transaction> it = selectedData.iterator();
            while (it.hasNext()) {
                Transaction data = it.next();
                transactionTable.getItems().remove(data);
                transactionsDataList.remove(data);
                persistTransaction();
                SystemListener.getInstance().addTransactionEvent();
            }
        });
    }

    private void addEventForTransactionFilterView() {
        Button filterBtn = transactionFilterView.getFilterBtn();
        filterBtn.setOnAction((ActionEvent event) -> {
            log.info("filter transaction");
            TransactionFilter filter = new TransactionFilter();
            String company = transactionFilterView.getCompanyName();
            if (StringUtil.isSet(company)) {
                filter.setCompanyName(company);
            }
            String dateFrom = transactionFilterView.getDateFrom();
            if (StringUtil.isSet(dateFrom)) {
                filter.setDateFrom(dateFrom);
            }
            String dateTo = transactionFilterView.getDateTo();
            if (StringUtil.isSet(dateTo)) {
                filter.setDateTo(dateTo);
            }

            transactionTable.setItems(FXCollections.observableArrayList(
                    TransactionServiceFactory.instance().getTransactionService().getTransactionList(filter))
            );
        });
    }

    @Override
    public void update() {
        persistTransaction();
    }

    private void persistTransaction() {
        TransactionPersistence persist = new TransactionPersistence();
        Collections.sort(transactionsDataList, new TransactionDateComparator());
        persist.updateTransactionFile(transactionsDataList);
    }

}
