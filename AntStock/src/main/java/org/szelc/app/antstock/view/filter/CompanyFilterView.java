package org.szelc.app.antstock.view.filter;

import java.io.IOException;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.szelc.app.antstock.view.transaction.table.TransactionTableView;

/**
 *
 * @author szelc.org
 */
public class CompanyFilterView extends AnchorPane {

    private static final Logger log = Logger.getLogger(TransactionTableView.class.toString());

    @FXML
    private TextField companyField;

    public CompanyFilterView() {
        log.info("CompanyFilterView  component constructor");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/filter/CompanyFilterView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
            
        }
    }

    
    public void setCompanies(Set<String> companies) {
        TextFields.bindAutoCompletion(companyField, companies);
    }

}
