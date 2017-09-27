package org.szelc.app.antstock.view.wallet;

import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.view.wallet.table.WalletTableView;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.WalletServiceFactory;
import org.szelc.app.antstock.statistic.CompanyTransactionStatistic;
import org.szelc.app.antstock.statistic.CompanyTransactionStatisticSummary;
import org.szelc.app.util.CsvUtil;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class WalletViewController implements Initializable {

    @FXML
    private WalletTableView walletTableView;
    @FXML
    private TableView<CompanyTransactionStatisticSummary> tableNoActiveSummary;
    @FXML
    private TableView<CompanyTransactionStatisticSummary> tableActiveSummary;

    @FXML
    private TableView<CompanyTransactionStatisticSummary> tableSummary;

    private static final Logger log = Logger.getLogger(WalletViewController.class.toString());

    private static final Integer INDEX_PROFIT_SUMMARY_COLUMN = 8;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reload(false);
    }

     public void reload(boolean reload) {
        log.info("WalletViewController.reload()");
        boolean result = false;
        try {
            result = setWalletOnTableView(new TransactionFilter(), reload);
        } catch (FileNotFoundException ex) {
            log.error("File not found [" + ex.getMessage() + "]");
            System.exit(0);
        }
        if(!result){
            log.warn("Brak jakichkolwiek transakcji w portfelu");
            return;
        }
        defineFieldColor(tableActiveSummary.getColumns().get(INDEX_PROFIT_SUMMARY_COLUMN));
        defineFieldColor(tableNoActiveSummary.getColumns().get(INDEX_PROFIT_SUMMARY_COLUMN));
        defineFieldColor(tableSummary.getColumns().get(INDEX_PROFIT_SUMMARY_COLUMN));
    }

     private boolean setWalletOnTableView(TransactionFilter filter, boolean reload) throws FileNotFoundException {
        List<CompanyTransactionStatistic> ctsList = WalletServiceFactory.instance().getWalletService().loadStockWalletData(reload);
        if(ctsList==null || ctsList.isEmpty()){
            return false;
        }
        walletTableView.setItems(FXCollections.observableArrayList(
                ctsList
        ));

        List<CompanyTransactionStatisticSummary> summary = new ArrayList(1);
        summary.add(new CompanyTransactionStatisticSummary(ctsList));
        log.info("TABLE SUMMARY ");

        tableNoActiveSummary.setItems(FXCollections.observableArrayList(
                summary
        ));

        tableActiveSummary.setItems(FXCollections.observableArrayList(
                summary
        ));

        tableSummary.setItems(FXCollections.observableArrayList(
                summary
        ));
        return true;
    }


    private void defineFieldColor(TableColumn tableColumn) {

        tableColumn.setCellFactory(column -> {
            return new TableCell<DayCompanyQuote, String>() {

                @Override
                public void updateSelected(boolean arg0) {

                    super.updateSelected(arg0);
                    if (isSelected()) {
                        this.setTextFill(Color.AQUA);
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
//                     log.info("UPDATE ITEM");
                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
                    } else {
                        Float value = Float.valueOf(item.replace(" ", "").replace(",", "."));

                        setTextFill(Color.GREEN);

                        if (value > 0) {
                            getStyleClass().clear();
                            this.getStyleClass().add("changeUp");
                        } else if (value < 0) {
                            getStyleClass().clear();
                            this.getStyleClass().add("changeDown");
                        } else {
                            getStyleClass().clear();
                            this.getStyleClass().add("changeEq");
                        }

                    }
                }

            };
        });
    }

}
