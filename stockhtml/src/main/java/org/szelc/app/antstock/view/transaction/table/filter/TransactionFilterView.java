package org.szelc.app.antstock.view.transaction.table.filter;

import org.szelc.app.antstock.view.transaction.table.TransactionTableView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author szelc.org
 */
public class TransactionFilterView extends AnchorPane {

    private static final Logger log = Logger.getLogger(TransactionTableView.class.toString());

    private final static SimpleDateFormat dateFormatFromYear = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    private Button filterTransactionBtn;
    @FXML
    private ComboBox transactionTypeCombo;
    @FXML
    private TextField companyField;
    @FXML
    private ComboBox bankCombo;
    @FXML
    private TextField dateFrom;
    @FXML
    private TextField dateTo;
    @FXML
    private TextField numberSharesField;
    @FXML
    private TextField priceForOneShareField;

    public TransactionFilterView() {
        log.info("TransactionFilterView component constructor");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/transaction/table/filter/TransactionFilterView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }

       
    }

    public Button getFilterBtn() {
        return filterTransactionBtn;
    }

    public String getCompanyName() {
        return companyField.getText();
    }

    public String getDateFrom() {
        return dateFrom.getText();
    }

    public String getDateTo() {
        return dateTo.getText();
    }

    public void setCompanies(Set<String> companies) {
         TextFields.bindAutoCompletion(companyField, companies);
    }

}
