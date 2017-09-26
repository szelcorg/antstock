
package org.szelc.app.antstock.view.evaluate;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.szelc.app.antstock.StockEventController;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.persistence.evaluate.EvaluatePersistence;
import org.szelc.app.antstock.view.evaluate.table.EvaluateTableView;
import org.szelc.app.antstock.view.tabbed.TabbedController;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.factory.EvaluateServiceFactory;
import org.szelc.stockthml.StockParser;


/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class EvaluateViewController implements Initializable , TableUpdateEvent {
    private Logger log = Logger.getLogger(EvaluateViewController.class.toString());
    private ObservableList<Evaluate> evaluateDataList = null;

    @FXML
    private EvaluateTableView evaluateTableView;
    @FXML
    private CheckBox autoSortAfterModifyCheck;
    @FXML
    private Button addEvaluateBtn;
     @FXML 
    private TextField newEvaluateCompanyTF;
    
    @FXML
    private Button removeEvaluateBtn;
    
    @FXML
    private TextField searchCompanyField;
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setEvaluateOnTableView(new TransactionFilter());
        } catch (FileNotFoundException ex) {
            
            log.error(ex);
            System.exit(0);
        }
       evaluateTableView.setTableUpdate(this);
       evaluateTableView.setController(this);
       autoSortAfterModifyCheck.setSelected(true);
    }    
    
       private void setEvaluateOnTableView(TransactionFilter filter) throws FileNotFoundException {
        evaluateDataList = FXCollections.observableArrayList(
                EvaluateServiceFactory.instance().getEvaluateService().getEvaluateRepository().getEvaluateStockDataList()
        );
        evaluateTableView.setItems(evaluateDataList);



        addEvent();

        startTask();
        //startTaskEvaluate();
    }
       
    private void addEvent() {
        addEvaluateBtn.setOnAction((ActionEvent event) -> {
            log.info("addEventForEvaluate");
            Evaluate evaluate = new Evaluate(newEvaluateCompanyTF.getText());
            //evaluateDataList.add(evaluate);
            evaluateDataList.add(0, evaluate);
            evaluateTableView.getTable().getSelectionModel().select(evaluate);
            persistEvaluate();
            afterUpdate();
        });

        removeEvaluateBtn.setOnAction((ActionEvent event) -> {
            log.info("delEventForEvaluate");
            Evaluate evaluate = evaluateTableView.getTable().getSelectionModel().getSelectedItem();
            evaluateDataList.remove(evaluate);
            persistEvaluate();
            afterUpdate();
        });
        
       
    }
    
    private void afterUpdate(){
        evaluateTableView.update();
    }

    private void persistEvaluate() {
        EvaluatePersistence persist = new EvaluatePersistence();
        //Collections.sort(evaluateDataList, new DateComparator());
        persist.update(evaluateDataList);
        log.info("TABLE UPDATED");

    }

    @Override
    public void update() {
        persistEvaluate();
    }

    public boolean isAutoUpdateAfterSort() {
        return autoSortAfterModifyCheck.isSelected();
    }

    public void startEvaluate(){
        System.out.println("***start task evaluate ***");
        StringBuilder buySb= new StringBuilder();
        StringBuilder sellSb= new StringBuilder();
        List<DayCompanyQuote> currentQuoteList = StockParser.displayQuotesGpwFromBankier();
        for(DayCompanyQuote dcqCurrent : currentQuoteList){
            if(dcqCurrent.getCourse()==-1.0f){
                continue;
            }
            FilteredList<Evaluate> elList = evaluateDataList.filtered(evaluate -> evaluate.getCompanyName().equals(dcqCurrent.getCompanyName()));
            if(!elList.isEmpty()){

                Evaluate evaluate = elList.get(0);
               /** System.out.println("COMPARE ["+dcqCurrent.getCompanyName()+"] "+dcqCurrent.getCourse()+"] "
                        + " "+evaluate.getCompanyName()+" "+evaluate.getRequiredPriceToBuy()+" "+evaluate.getRequiredPriceToSell());
*/
                if(evaluate.getRequiredPriceToBuy()!=0 && dcqCurrent.getCourse()<=evaluate.getRequiredPriceToBuy()){
                    String msg = "BUY "+dcqCurrent.getCompanyName()+" "+dcqCurrent.getCourse()+"\n";
                    System.out.println(msg);
                    buySb.append(msg);
                }
                if(evaluate.getRequiredPriceToSell()!=0 && dcqCurrent.getCourse()>=evaluate.getRequiredPriceToSell()){
                    String msg = "SELL "+dcqCurrent.getCompanyName()+" "+dcqCurrent.getCourse()+"\n";
                    System.out.println(msg);
                    sellSb.append(msg);
                }
            }
        }
        Platform.runLater(() -> {
            /**
            if(alert.isShowing()){
                 alert.close();
            }
             */
            alert = new Alert(Alert.AlertType.INFORMATION);
            System.out.println("ShowAndWait");

            alert.setTitle("Buy/Sell");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
            alert.setHeaderText("Communicate "+sdf.format(new Date()));
            alert.setContentText(buySb.toString() + "\n" + sellSb);


            if(tabbedController!=null) {
                tabbedController.addMessages(sdf.format(new Date())+"\n\n");
                tabbedController.addMessages(buySb.toString() + "\n" + sellSb);
                tabbedController.addMessages("\n\n");
            }
            System.out.println("Dodany komunikat");
            alert.showAndWait();

        });
    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION);


    private void startTask(){
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                while(true) {

                    int iterations;
                    if (isCancelled()) {
                        return 0;
                    }
                    startEvaluate();

                    try {
                        Thread.sleep(1000 * 60 * 15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        Thread th = new Thread(task);

        th.setDaemon(true);

        th.start();

    }

    TabbedController tabbedController;

    public void setTabbedController(TabbedController tabbedController){
        this.tabbedController = tabbedController;
    }
     
}
