package org.szelc.app.antstock.app;

import org.szelc.app.antstock.view.tabbed.TabbedController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.log4j.Logger;

/**
 *
 * @author mszelc
 */
public class AntStockController implements Initializable {

    @FXML
    private TabbedController stockMainTabbedController;

    private static final Logger log = Logger.getLogger(AntStockController.class.toString());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!checkController()) {
            log.error("Controller in application failed. Exit application.");
            System.exit(0);
        }
        log.info("AntStockController subcontroller loaded successfully.");
    }

    private boolean checkController() { 
        boolean result = true;
        if (stockMainTabbedController == null) {
           
            log.error("StockMainTabbedController is null.");
            result = false;
        }
        return result;
    }

}
