package org.szelc.app.antstock.test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MyController {

    @FXML
    private Label label1 ;
    @FXML
    private Label label2 ;

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {

        // do initialization and configuration work...

        // trivial example, could also be done directly in the fxml:
        label1.setText("Foo");
        label2.setText("Bar");
    }
}