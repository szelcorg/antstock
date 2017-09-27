package org.szelc.app.antstock.app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 * @author by marcin.szelc on 2017-09-27.
 */
public class TestUIController implements Initializable {


    @FXML
    TextField txtURL;

    @FXML
    WebView webView;
    private WebEngine webEngine;

    @FXML
    private void goAction(ActionEvent evt) {
        System.out.println("GO ACTION");

        webEngine.load(txtURL.getText().startsWith("http://") ? txtURL.getText() : "http://" + txtURL.getText());
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if ( newState == Worker.State.FAILED) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Zły adres");
                alert.setContentText("Strona o podanym adresie nie istnieje lub jest niedostępna");
                alert.showAndWait();

            }
        });


    }


    private void addListener(){
        webView.getEngine().locationProperty().addListener((observableValue, oldLoc, newLoc) -> {
            int readBytes;
            System.out.println("Location "+newLoc);
            if (newLoc.contains(".pdf") || newLoc.contains("doc") || newLoc.contains("zip")
                    || newLoc.contains("docx") || newLoc.contains("xls") || newLoc.contains("xlsx")) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save " + newLoc);
                chooser.setInitialDirectory(new File("C://Temp//"));
                String[] chunkUrl = newLoc.split("\\/");
                chooser.setInitialFileName(chunkUrl[chunkUrl.length - 1]);

                File saveFile = chooser.showSaveDialog(webView.getScene().getWindow());



                if (saveFile != null) {
                    BufferedInputStream is = null;
                    BufferedOutputStream os = null;
                    try {
                        is = new BufferedInputStream(new URL(newLoc).openStream());
                        os = new BufferedOutputStream(new FileOutputStream(saveFile));
                        byte b[] = new byte[1024];
                        while ((readBytes = is.read(b)) != -1) {
                            os.write(b, 0, readBytes);

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (os != null) os.close();
                        } catch (IOException e) {
                        }
                    }
                }
                chooser.showOpenDialog(webView.getScene().getWindow());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webView.getEngine();
        addListener();
        txtURL.setText("kkbmosir.pl");
        //openPdf();
    }

    public void openPdf(){
        InputStream stream = null;
        URL myUrl = null;
        try {
            myUrl = new URL("http://slimak.onet.pl/_m/nb/biznes/20170926/193939/grupa_kapitalowa_pamapol_rozszerzony_skonsolidowany_raport_polroczny_2017.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            stream = myUrl.openStream();
            //I use IOUtils from org.​apache.​commons.​io
            byte[] data = IOUtils.toByteArray(stream);
            //Base64 from java.org.szelc.stockfromdata
            String base64 = Base64.getEncoder().encodeToString(data);
            //This must be ran on FXApplicationThread
            webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
