package org.szelc.app.antstock;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;

public class StockEventController implements Initializable {

    @FXML
    TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addMessages(String msg) {
        System.out.println("addMessages "+msg);
        if (textArea != null) {
            System.out.println("textArea is not null");
            textArea.appendText(msg);
        }

    }
}
