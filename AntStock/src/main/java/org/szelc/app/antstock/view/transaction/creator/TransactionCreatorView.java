
package org.szelc.app.antstock.view.transaction.creator;

import javafx.scene.control.Alert;
import org.szelc.app.antstock.loader.transaction.TransactionUtil;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.view.transaction.table.TransactionTableView;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class TransactionCreatorView extends AnchorPane {

    private static final Logger log = Logger.getLogger(TransactionTableView.class.toString());

    private final static SimpleDateFormat dateFormatFromYear = new SimpleDateFormat("yyyy-MM-dd");
    @FXML
    private Button addTransactionBtn;
    @FXML
    private Button removeTransactionBtn;
    @FXML
    private ComboBox transactionTypeCombo;
    @FXML
    private TextField companyField;
    @FXML
    private TextField bankField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField numberSharesField;
    @FXML
    private TextField priceForOneShareField;

    public TransactionCreatorView() {
        log.info("TransactionTable component constructor");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/transaction/creator/TransactionCreatorView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
        dateField.setText(dateFormatFromYear.format(new Date()));
        transactionTypeCombo.setItems(
                FXCollections.observableArrayList(
                        "K",
                        "S"
                )
        );
        transactionTypeCombo.getSelectionModel().select("K");
    }

    public Button getAddTransactionButton() {
        return addTransactionBtn;
    }

    public Button getDelTransactionButton() {
        return removeTransactionBtn;
    }

    public Transaction getTransactionData() {
         
        Date transactionDate;
        String companyName = companyField.getText();
        TransactionType transactionType = TransactionType.valueOf(String.valueOf(transactionTypeCombo.getSelectionModel().getSelectedItem()));
        int numberShares;
        try {
            numberShares = Integer.valueOf(numberSharesField.getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Zły format liczby akcji");
            alert.setContentText("Popraw liczbę akcji transakcji i zapisz ponownie");
            alert.showAndWait();
        }
        catch(NumberFormatException e){
            return null;
        }

        float priceForOneShare = Float.valueOf(priceForOneShareField.getText().replace(",", "."));
        float priceForShares = numberShares * priceForOneShare;
        BankEnum bank = BankEnum.valueOf(bankField.getText());
        float percentProvision = TransactionUtil.getPercentProvision(bank);
        float moneyProvision = TransactionUtil.calculateProvision(bank,(percentProvision * priceForShares) / 100);
        float priceTotal = priceForShares + moneyProvision;
        try {

            transactionDate = dateFormatFromYear.parse(dateField.getText());
        } catch (ParseException ex) {
            transactionDate = Calendar.getInstance().getTime();
           

        }
        Transaction data = new Transaction(transactionDate, companyName, transactionType, numberShares, priceForOneShare,
                 bank, percentProvision, moneyProvision);
        return data;
    }

    public void setItems(ObservableList<BankEnum> bankList) {
        bankField.setText("DB");
        TextFields.bindAutoCompletion(bankField, bankList);
    }

    public void setCompanies(Set<String> companies) {
        TextFields.bindAutoCompletion(companyField, companies);
    }

}
