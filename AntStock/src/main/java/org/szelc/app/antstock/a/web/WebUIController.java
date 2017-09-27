package org.szelc.app.antstock.a.web;

/**
 * @author by marcin.szelc on 2017-09-25.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
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
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author Alvin Tabontabon
 */
public class WebUIController implements Initializable {

    @FXML
    TextField txtURL;

    @FXML
    WebView webViewCustom;
    private WebEngine webEngineCustom;

    @FXML
    WebView webViewAliorbank;
    private WebEngine webEngineAliorbank;

    @FXML
    WebView webViewBrokeralior;
    private WebEngine webEngineBrokeralior;

    @FXML
    WebView webViewBossa;
    private WebEngine webEngineBossa;

    @FXML
    private void goAction(ActionEvent evt) {
        System.out.println("GO ACTION");
        webEngineCustom.load(txtURL.getText().startsWith("http://") ? txtURL.getText() : "http://" + txtURL.getText());
        webEngineCustom.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if ( newState == Worker.State.FAILED) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Zły adres");
                alert.setContentText("Strona o podanym adresie nie istnieje lub jest niedostępna");
                alert.showAndWait();

            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if(webViewCustom!=null) {
            webEngineCustom = webViewCustom.getEngine();
        }

        if(webViewAliorbank!=null) {
            webEngineAliorbank = webViewAliorbank.getEngine();
            webEngineAliorbank.load("http://aliorbank.pl");
        }

        if(webViewBossa!=null) {
            webEngineBossa = webViewBossa.getEngine();
            webEngineBossa.load("http://bossa.pl");
        }

        if(webViewBrokeralior!=null) {
            webEngineBrokeralior = webViewBrokeralior.getEngine();
            webEngineBrokeralior.load("https://broker.aliorbank.pl/narzedzia/profil-spolki/");
        }


        String username;
        String password;
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("C://secret/pass.properties"));
            username = props.getProperty("bab.login");
            password = props.getProperty("bab.pass");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        String enterPw = "document.getElementsByName('haslo')[0].value='" + password + "';";
        String enterUserName = "document.getElementById('login').value='" + username + "';";

        if(webEngineBrokeralior!=null) {
            webEngineBrokeralior.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    try {
                        webEngineBrokeralior.executeScript(enterPw);
                        webEngineBrokeralior.executeScript(enterUserName);

                        HTMLInputElement element2 = (HTMLInputElement) webEngineBrokeralior.executeScript("document.getElementsByName('zaloguj')[0]");

                        element2.click();


                        System.out.println("Element [" + element2 + "]");
                    } catch (netscape.javascript.JSException e) {

                    }
                }
            });
        }

        if(txtURL!=null)
            txtURL.setText("http://www.google.com");
    }
}