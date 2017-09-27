package org.szelc.app.antstock.a;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import org.szelc.app.antstock.view.quotes.QuotesViewController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.factory.TransactionServiceFactory;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.service.QuoteService;
import org.szelc.app.antstock.service.TransactionService;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.reader.CSVReader;
import org.szelc.stockthml.MainFX;

import static org.szelc.stockthml.MainFX.getLoader;

/**
 *
 * @author mszelc
 */
public class AntStock extends Application {

    private static final String FXML_VIEW_FILE = "javafx/AntStock.fxml";
    private static final Logger log = Logger.getLogger(AntStock.class.toString());
    private static final Boolean TEST = false;
    private static final Boolean FULLSCREEN = false;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting javafx");

        String filename = "javafx/AntStock.fxml";

        FXMLLoader loader = MainFX.getLoader(filename);
        Parent root;

        try {
            root = loader.load();
        }
        catch(Exception e){
            e.printStackTrace();
            loader = MainFX.getLoader(".");
            root = loader.load();
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }


    public void start2(Stage stage) throws Exception {
        if(true){
       // Locale.setDefault(new Locale("PL", "pl"));
        ResourceBundle bundle = ResourceBundle.getBundle("locale", Locale.getDefault());
        
        long t1 = System.currentTimeMillis();

        log.info("AntStock starting ...");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_VIEW_FILE));
            String filename = "src/main/resources/stockhtml.fxml";

            FXMLLoader loader = getLoader(filename);
        log.info("Resource bundle loaded ["+bundle+"] value ["+bundle.getString("company.name")+"]");
        
        loader.setResources(bundle);
        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);

        if (FULLSCREEN) {
            stage.initStyle(StageStyle.DECORATED);
            stage.setFullScreen(true);
         
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
         ImageIcon img = new ImageIcon("");
        stage.getIcons().add(new Image(new FileInputStream("images\\bull-head48red.png")));
        //stage.getIcons().add(new Image(new FileInputStream("images\\t48.png")));
        
        stage.show();

        log.info("TIME [AntStock starded] miliseconds [" + (System.currentTimeMillis() - t1) + "}");
        }
        else if(false){
            showInfoAboutTransaction();
        }
        else if(true){
            saveCompaniesAllFromPrnBossaFile();
        }
        
    }
        

    public static void main(String[] args) {
        launch(args);
    }

    static QuotesViewController controller;

    public static void setController(QuotesViewController c) {
        controller = c;
    }

    public static QuotesViewController getController() {
        return controller;
    }

    private void showInfoAboutTransaction() {
        TransactionService tService = TransactionServiceFactory.instance().getTransactionService();
        QuoteService qServce = QuoteServiceFactory.instance().getQuoteService();
        
        List<Transaction> transactionAll = tService.getTransactionList(new TransactionFilter());
        log.error("All transaction ["+transactionAll.size()+"]");
        
        float totalPriceTransactionBuy = 0f;
        float totalPriceTransactionBuyOnCloseDay = 0f;
        
        float totalPriceTransactionSell = 0f;
        float totalPriceTransactionSellOnCloseDay = 0f;
        
        int i=0;
        int countException = 0;
        int countSplitResplit = 0;
        for (Transaction t : transactionAll) {
            i++;
            if (i == 1) {
                continue;
            }
            float close;
            try {
                close = getCloseCourse(t);

            } catch (Exception e) {
                countException++;
                continue;
            }
            float realCost = (t.getNumberShares() * t.getPriceOneShare());
            float aiCost = (t.getNumberShares() * close);

            if (realCost > 2 * aiCost || aiCost > 2 * realCost) {
                countSplitResplit++;
                continue;
            }
            if (t.getTransactionType().equals(TransactionType.K)) {
                log.info("[" + t.getDayStr() + "] [" + t.getCompanyName() + "] numberShares [" + t.getNumberShares() + "] price [" + t.getPriceOneShare() + "] close [" + close + "]");

                totalPriceTransactionBuy += realCost;
                totalPriceTransactionBuyOnCloseDay += aiCost;
                log.error("BUY1 [" + totalPriceTransactionBuy + "]");
                log.error("BUY2 [" + totalPriceTransactionBuyOnCloseDay + "]");
            }
            else if (t.getTransactionType().equals(TransactionType.S)) {
                log.info("[" + t.getDayStr() + "] [" + t.getCompanyName() + "] numberShares [" + t.getNumberShares() + "] price [" + t.getPriceOneShare() + "] close [" + close + "]");

                totalPriceTransactionSell += realCost;
                totalPriceTransactionSellOnCloseDay += aiCost;
                log.error("Sell1 [" + totalPriceTransactionSell + "]");
                log.error("Sell2 [" + totalPriceTransactionSellOnCloseDay + "]");
            }
        }
     
        log.info("totalPriceTransactionBuy ["+totalPriceTransactionBuy+"]");
        log.info("totalPriceTransactionBuyOnCloseDay ["+totalPriceTransactionBuyOnCloseDay+"]");
        
        log.info("totalPriceTransactionSell ["+totalPriceTransactionSell +"]");
        log.info("totalPriceTransactionSell OnCloseDay ["+totalPriceTransactionSellOnCloseDay+"]");
        log.info("CountException "+countException+"]");
        log.info("CountSplitResplit "+countSplitResplit+"]");
    }
    
    private float getCloseCourse(Transaction t){
        QuoteService qService = QuoteServiceFactory.instance().getQuoteService();
       // log.error("GetClose ["+t.getCompanyName()+"] ["+t.getDayStr()+"]");
        Date day = null;
        try {
            day = Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.parse(Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.format(t.getDay()));
        } catch (ParseException ex) {
            log.error(ex);
            System.exit(0);
        }
        return qService.getDayCompanyQuote(t.getCompany(), day).get(0).getCourse();
    }

    private void saveCompaniesAllFromPrnBossaFile() {
        try {
            CSVRecords records = CSVReader.getRecordsFromFile(Settings.APP_HOME_PATH + "Storage/sesjacgl.prn", ",");
            int count = 0;
            for(CSVRecord record: records.getRecords()){
                count++;
                String company = record.getFields().get(0);
                log.info("COMPANY ["+company+"]");
                if(count > 500 && company.equals("INVESTORMS")){
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            log.error(ex);
            System.exit(0);
        }
        
    }

}
