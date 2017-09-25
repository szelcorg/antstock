package org.szelc.app.antstock.view.transaction.statistic;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class TransactionStatisticView extends AnchorPane {

    public TransactionStatisticView() {
        loadComponent();
    }

    private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/transaction/statistic/TransactionStatisticView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

}
