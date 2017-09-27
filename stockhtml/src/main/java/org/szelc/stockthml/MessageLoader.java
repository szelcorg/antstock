package org.szelc.stockthml;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



public class MessageLoader extends Application {

    Logger log = Logger.getLogger(MessageLoader.class);



    //0 - evaluate
    //1 - portfel
    //2 - transactioned
    private static List getCompanyList(int type){
        String[] fileNames = new String[]{"companiesFromEvaluate", "companiesFromWallet", "companiesFromTransaction"};
        String fileName = "C:\\WORKSPACE\\AntStock3\\Storage\\cache\\companies\\"+fileNames[type]+".txt";
        try {
            return Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
    }

    public MessageLoader() {
        loadMessageForCompanies();

    }

    public static List<String> loadMessageForCompanies(){
        StockParser stockParser = new StockParser();
        //stockParser.displayDividendBossa();

        boolean onlyToday = false;
        //type
        //0 - evaluate
        //1 - portfel
        //2 - transactioned
        List<String> result= stockParser.displayMultiplePageMesssageOnet(6, onlyToday, getCompanyList(0));
        //log.info("Readed number of message ["+countMessage+"]");

        //int count = stockParser.displayQuotesNewConnectFromBankier();
        // int count = stockParser.displayQuotesGpwFromBankier();
        //log.info(count);
        return result;
    }

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
            File file = new File("javafx/stockhtml.fxml");

            loader = getLoader(file.getAbsolutePath());
            root = loader.load();
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    private FXMLLoader getLoader(String path) throws MalformedURLException {
        File f = new File(path);
        System.out.println("Absolute ["+f.getAbsolutePath()+"]");


        URL url = new URL("file:"+f.getAbsolutePath());
        FXMLLoader loader = new FXMLLoader(url);
        System.out.println("LOADER ["+loader+"]");
        return loader;
    }

    public static void main(String args[]){
        System.out.println("Started stockhtml");
        new MessageLoader();
    }
}
