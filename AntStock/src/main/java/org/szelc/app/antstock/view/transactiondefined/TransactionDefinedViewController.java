package org.szelc.app.antstock.view.transactiondefined;


import org.szelc.app.antstock.collection.transactiondefined.TransactionDefinedContainer;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import org.szelc.app.antstock.loader.transactiondefined.TransactionDefinedLoader;
import org.szelc.app.antstock.view.quotes.table.QuotesTableView;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.szelc.app.antstock.view.transactiondefined.table.TransactionDefinedTableView;
import java.util.Arrays;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.persistence.transactiondefined.TransactionDefinedPersistence;
import org.szelc.app.antstock.view.transactiondefined.comparator.TransactionDefinedCompanyComparator;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class TransactionDefinedViewController implements Initializable, TableUpdateEvent {
    
    private static final Logger log = Logger.getLogger(TransactionDefinedViewController.class.toString());

    @FXML
    private Button addTransactionDefinedBtn;
    @FXML
    private Button removeTransactionDefinedBtn;

    @FXML
    private TextField newCompanyTF;
        @FXML
    private ComboBox transactionTypeCombo;

    @FXML
    private TransactionDefinedTableView transactionDefinedTable;

    private ObservableList<TransactionDefined> transactionsDefinedDataList = null;
    private final TransactionDefinedLoader loader = new TransactionDefinedLoader();

   @FXML
    private QuotesTableView quotesTableView;
   
   private TransactionDefinedContainer tdc;
     
    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tdc = loader.loadTransactionsFromCSVFile(null);
        transactionsDefinedDataList = FXCollections.observableArrayList(
                tdc.getTransactionDefinedList()
        );

        transactionDefinedTable.setItems(transactionsDefinedDataList);
        transactionDefinedTable.setTableUpdate(this);
        addEvent();
        transactionTypeCombo.setItems(
                FXCollections.observableArrayList(
                        "K",
                        "S"
                )
        );
   
    }

    private void addBankToCombo() {
        final ObservableList<BankEnum> bankList = FXCollections.observableArrayList(Arrays.asList(BankEnum.values())
        );

       // transactionCreatorView.setItems(bankList);
    }

//    private void setTransactionsOnTableView(TransactionFilter filter) {
//        tdc = DataContainerRegister.getTransactionDataContainer();
////        if(tdc==null){
////             tdc = loader.loadTransactionsFromCSVFile(filter);
////            DataContainerRegister.setTransactionDataContainer(tdc);
////        }
//        
//        transactionsDataList = FXCollections.observableArrayList(
//                tdc.getTransactionDataList()
//        );
////        transactionTable.setItems(FXCollections.observableArrayList(loader.filter(transactionsDataList, filter)));
////        
////        //TODO move 
////        transactionFilterView.setCompanies(tdc.getAllCompanies());
////        transactionCreatorView.setCompanies(tdc.getAllCompanies());
//        
//    }

    private void addEvent() {
        addTransactionDefinedBtn.setOnAction((ActionEvent event) -> {
            log.info("addTransactionDefined");
            TransactionDefined rec = new TransactionDefined(newCompanyTF.getText().toUpperCase(), 
                    TransactionType.valueOf(transactionTypeCombo.getValue().toString()));
            //evaluateDataList.add(evaluate);
            transactionsDefinedDataList.add(0, rec);
            transactionDefinedTable.getTable().getSelectionModel().select(rec);
            persist();
            //afterUpdate();
        });

        removeTransactionDefinedBtn.setOnAction((ActionEvent event) -> {
            log.info("delTransactionDefined");
            TransactionDefined rec = transactionDefinedTable.getTable().getSelectionModel().getSelectedItem();
            transactionsDefinedDataList.remove(rec);
            persist();
            //afterUpdate();
        });

    }

    private boolean isSet(String item) {
        return item != null && item.length() > 0;
    }


    @Override
    public void update() {
      persist();
    }
    
    private void persist() {
        TransactionDefinedPersistence persist = new TransactionDefinedPersistence();
        Collections.sort(transactionsDefinedDataList, new TransactionDefinedCompanyComparator());
        persist.updateTransactionFile(transactionsDefinedDataList);
    }
}
