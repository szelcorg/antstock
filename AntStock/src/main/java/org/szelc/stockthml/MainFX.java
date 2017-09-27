package org.szelc.stockthml;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFX extends Application {

    private static final String FXML_VIEW_FILE = "C:\\github\\antstock\\stockhtml\\stockhtml.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting javafx");

        String filename = "src/main/resources/stockhtml.fxml";

        FXMLLoader loader = getLoader(filename);
        Parent root;

        try {
            root = loader.load();
        }
        catch(Exception e){
            loader = getLoader(".");
            root = loader.load();
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    public static FXMLLoader getLoader(String path) throws MalformedURLException {
        File f = new File(path);
        System.out.println("Absolute ["+f.getAbsolutePath()+"]");


        URL url = new URL("file:"+f.getAbsolutePath());
        FXMLLoader loader = new FXMLLoader(url);
        System.out.println("LOADER ["+loader+"]");
        return loader;
    }
}
