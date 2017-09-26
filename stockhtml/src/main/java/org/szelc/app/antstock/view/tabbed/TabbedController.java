package org.szelc.app.antstock.view.tabbed;

import org.szelc.app.antstock.StockEventController;
import org.szelc.app.antstock.view.evaluate.EvaluateViewController;
import org.szelc.app.antstock.view.quotes.QuotesViewController;
import org.szelc.app.antstock.view.quotes.chart.QuotesChartViewController;
import org.szelc.app.antstock.view.transaction.TransactionViewController;
import org.szelc.app.antstock.view.wallet.WalletViewController;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.view.listener.SystemListener;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class TabbedController implements Initializable {

    private static final boolean CHECK_CONTROLLER_TURN_OFF = true;
    private static final Logger log = Logger.getLogger(TabbedController.class.toString());
    @FXML
    private QuotesViewController quotesViewController;
    @FXML
    private TransactionViewController transactionViewController;
    @FXML
    private EvaluateViewController evaluateViewController;
    @FXML
    private WalletViewController walletViewController;
    @FXML
    private QuotesChartViewController quotesChartViewController;
    @FXML
    private StockEventController stockEventController;



    /**
     *
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!checkController()) {
            log.error("Controller in application failed. Exit application.");
            System.exit(0);
        }
        log.info("StockMainTabbedController subcontroller loaded successfully.");

        initalizeListener();
        evaluateViewController.setTabbedController(this);
    }

    private boolean checkController() {
        if (CHECK_CONTROLLER_TURN_OFF) {
            return true;
        }
        boolean result = true;
        if (quotesViewController == null) {
            log.error("QuotesViewController is null.");
            result = false;
        }
        if (transactionViewController == null) {
            log.error("TransactionViewController is null.");
            result = false;
        }
        if (evaluateViewController == null) {
            log.error("QuotesViewController is null.");
            result = false;
        }
        if (walletViewController == null) {
            log.error("WalletViewController is null.");
            result = false;
        }
        if (quotesChartViewController == null) {
            log.error("QuotesChartViewController is null");
            result = false;
        }




        return result;
    }

    private void initalizeListener() {
        SystemListener sysListener = SystemListener.getInstance();
        sysListener.setTransactionViewController(transactionViewController);
        sysListener.setWalletViewController(walletViewController);
    }

    public void addMessages(String s) {
        System.out.println("Tabbed controleed ad "+s);
        stockEventController.addMessages(s);
    }
}
