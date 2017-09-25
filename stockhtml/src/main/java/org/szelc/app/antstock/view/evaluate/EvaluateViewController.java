
package org.szelc.app.antstock.view.evaluate;

import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.persistence.evaluate.EvaluatePersistence;
import org.szelc.app.antstock.view.evaluate.table.EvaluateTableView;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.factory.EvaluateServiceFactory;

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
    
     
}
