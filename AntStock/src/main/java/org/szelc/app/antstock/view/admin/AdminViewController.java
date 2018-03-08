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
    String ftpFolder = "antstock/storage/";
    List<String> localFileToDump = Arrays.asList(new String[]{Settings.EVALUATE_OWN_FILE_CSV, Settings.TRANSACTION_FILE_PATH, Settings.TRANSACTION_TO_REALIZE_FILE });
    List<String> localTmpFileToDump = Arrays.asList(new String[]{Settings.EVALUATE_OWN_FILE_CSV+"_ftp", Settings.TRANSACTION_FILE_PATH+"_ftp", Settings.TRANSACTION_TO_REALIZE_FILE+"_ftp" });
    List<String> ftpFileToDump = Arrays.asList(new String[]{ ftpFolder + "BUY_LONGTERM.csv", ftpFolder + "Transaction.csv", ftpFolder+"Transaction_to_realize.csv" });

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

        log.info("exportDataToFtpEvent");

        boolean res[] = FtpUtil.uploadFile(localFileToDump, ftpFileToDump);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export files data to ftp");
        alert.setHeaderText("Export status");
        alert.setContentText("Transaction exported "+ (res[0] ? "successfull": "error")
                +",\nEvaluate exported "+ (res[1] ? "successfull": "error")
                +",\nDefined exported "+ (res[2] ? "successfull": "error")
        );
        alert.showAndWait();
      
    }
    
    @FXML
    private void importDataToFtpEvent(ActionEvent e) {

        log.info("importDataToFtpEvent");

        boolean res[] = FtpUtil.downloadFile(ftpFileToDump, localTmpFileToDump);
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setWidth(600);        
        alert.setTitle("Import files data from ftp");
        alert.setHeaderText("Import status");
        alert.setContentText("Transaction imported " + (res[0] ? "successfull" : "error")
                + ",\nEvaluate imported " + (res[1] ? "successfull" : "error")
                + ",\nDefined imported " + (res[2] ? "successfull" : "error")

        );
        alert.showAndWait();
        if(res[0]==false && res[1]==false && res[2]==false){
            return;   
        }
        
        boolean res3 = false;
        boolean res4 = false;
        boolean res5 = false;
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
        try {
            if (res[2]) {
                Files.move(Paths.get(Settings.TRANSACTION_TO_REALIZE_FILE + "_ftp"), Paths.get(Settings.TRANSACTION_TO_REALIZE_FILE), StandardCopyOption.REPLACE_EXISTING);
                res5=true;
            }
        } catch (IOException ex) {
            res5 = false;
            ex.printStackTrace();
        }
                         
        alert.setTitle("Restore from ftp finished");
        alert.setHeaderText("Restore status");        
        alert.setContentText(
                "Transaction restore " + (res3 ? "successfull" : "error")+
                ",\nEvaluate restore " + (res4 ? "successfull" : "error")+
                ",\nDefined restore " + (res5 ? "successfull" : "error")+
                ".\n\nRequired Restart.");


        alert.showAndWait();

        System.exit(0);
            
            //todo
            //copy transaction and evaluate to tmp
            //move original to back
            //if successfully copy file move to original        
       
    }
}
