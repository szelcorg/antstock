package org.szelc.app.antstock.view.admin;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.factory.EvaluateServiceFactory;
import org.szelc.app.antstock.factory.TransactionServiceFactory;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.util.FtpUtil;

/**
 *
 * @author szelc.org
 */
public class AdminViewController implements Initializable{

    private static final Logger log = Logger.getLogger(AdminViewController.class.toString());
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    @FXML
    private void
    repairCompanyFilesEvent(ActionEvent e){
        log.info("repairCompanyFilesEvent event ["+e+"]");
        CompanyServiceFactory.instance().getCompanyService().saveCompanies(
                EvaluateServiceFactory.instance().getEvaluateService().getEvaluateRepository().getCompaniesSet(),
                Settings.COMPANY_FILE_FROM_EVALUATE);
        CompanyServiceFactory.instance().getCompanyService().saveCompanies(
                TransactionServiceFactory.instance().getTransactionService().getTransactionRepository().getCompaniesWithTransaction(),
                Settings.COMPANY_FILE_FROM_TRANSACTION);
    }
    
    @FXML
    private void exportDataToFtpEvent(ActionEvent e) {
        String ftpFolder = "antstock/storage/";
        log.info("exportDataToFtpEvent");
   
        List<String> srcList = Arrays.asList(new String[]{Settings.EVALUATE_OWN_FILE_CSV, Settings.TRANSACTION_FILE_PATH });
        List<String> dstList = Arrays.asList(new String[]{ ftpFolder + "BUY_LONGTERM.csv", ftpFolder + "Transaction.csv" });
        boolean res[] = FtpUtil.uploadFile(srcList, dstList);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export files data to ftp");
        alert.setHeaderText("Export status");
        alert.setContentText("Transaction exported "+ (res[0] ? "successfull": "error")+", Evaluate exported "+ (res[1] ? "successfull": "error"));
        alert.showAndWait();
      
    }
    
    @FXML
    private void importDataToFtpEvent(ActionEvent e) {
        String ftpFolder = "antstock/storage/";
        log.info("importDataToFtpEvent");
        List<String> srcList = Arrays.asList(new String[]{ftpFolder + "Transaction.csv", ftpFolder + "BUY_LONGTERM.csv" });
        List<String> dstList = Arrays.asList(new String[]{Settings.TRANSACTION_FILE_PATH+"_ftp", Settings.EVALUATE_OWN_FILE_CSV+"_ftp" });
        boolean res[] = FtpUtil.downloadFile(srcList, dstList);        
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setWidth(600);        
        alert.setTitle("Import files data from ftp");
        alert.setHeaderText("Import status");
        alert.setContentText("Transaction imported " + (res[0] ? "successfull" : "error") + ", Evaluate imported " + (res[1] ? "successfull" : "error"));
        alert.showAndWait();
        if(res[0]==false && res[1]==false){
            return;   
        }
        
        boolean res3 = false;
        boolean res4 = false;
        try {
            if (res[0]) {
                Files.move(Paths.get(Settings.TRANSACTION_FILE_PATH + "_ftp"), Paths.get(Settings.TRANSACTION_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
                res3 = true;
            }
        } catch (IOException ex) {
            res3 = false;
            ex.printStackTrace();
        }
        try {
            if (res[1]) {
                Files.move(Paths.get(Settings.EVALUATE_OWN_FILE_CSV + "_ftp"), Paths.get(Settings.EVALUATE_OWN_FILE_CSV), StandardCopyOption.REPLACE_EXISTING);
                res4=true;
            }
        } catch (IOException ex) {
            res4 = false;
            ex.printStackTrace();
        }
                         
        alert.setTitle("Restore from ftp finished");
        alert.setHeaderText("Restore status");        
        alert.setContentText("Transaction restore " + (res3 ? "successfull" : "error") + ", Evaluate restore " + (res4 ? "successfull. Required Restart." : "error"));
        alert.showAndWait();

        System.exit(0);
            
            //todo
            //copy transaction and evaluate to tmp
            //move original to back
            //if successfully copy file move to original        
       
    }
}
