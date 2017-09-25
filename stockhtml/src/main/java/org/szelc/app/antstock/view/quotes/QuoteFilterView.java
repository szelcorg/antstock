package org.szelc.app.antstock.view.quotes;

import java.io.IOException;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.szelc.app.antstock.view.quotes.enumeration.QuoteFilterEnum;
//import  com.sun.javafx.scene.control.skin.TextFieldSkin;
/**
 *
 * @author szelc.org
 */
public class QuoteFilterView extends AnchorPane {

    private static final Logger log = Logger.getLogger(QuotesViewController.class.toString());

    @FXML
    private TextField companyField;

    @FXML
    private Button filterTransactionBtn;
    @FXML
    private ComboBox transactionTypeCombo;

    @FXML
    private DatePicker date;

    @FXML
    private DatePicker dateToDP;
    
    @FXML
    private RadioButton myCompaniesRB;
    @FXML
    private RadioButton evaluteOrTransactionCompaniesRB;
    @FXML
    private RadioButton allCompaniesRB;
    
    private Set<String> companies;
  
    public QuoteFilterView() {
        log.info("QuoteFilte component constructor");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("QuoteFilterView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    public void setCompanies(Set<String> companies) {
        this.companies = companies;
        TextFields.bindAutoCompletion(companyField, companies);
    }

    public Set<String> getCompanies() {
        return companies;
    }
    
    
    
     public Button getFilterBtn() {
        return filterTransactionBtn;
    }
     
    public DatePicker getDate() {
        return date;
    }

      
    public String getCompanyName() {
        return companyField.getText();
    }
    
    public QuoteFilterEnum getChoicedType() { 
        if (myCompaniesRB.isSelected()) {
            return QuoteFilterEnum.ACTIVE;
        } else if (evaluteOrTransactionCompaniesRB.isSelected()) {
            return QuoteFilterEnum.EVALUATED_OR_TRANSACTIONED;
        } else if (allCompaniesRB.isSelected()) {
            return QuoteFilterEnum.ALL;
        }
        return QuoteFilterEnum.SINGLE;
    }
}
