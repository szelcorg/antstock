package org.szelc.app.antstock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.szelc.stockthml.MessageLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author by marcin.szelc on 2017-09-25.
 */
public class StockMessagesController implements Initializable {

    @FXML
    WebView webViewMessages;
    private WebEngine webEngine;



    @FXML
    private ListView messageListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webViewMessages.getEngine();
        webEngine.load("http://google.pl");
        List<String> msgList = MessageLoader.loadMessageForCompanies();
        for(int i=0;i<100;i++){
            msgList.add("http://abc"+i+".pl");
        }
        Hyperlink[] hpls = new Hyperlink[msgList.size()];
        int i=0;
        for(String msg : msgList){
            Hyperlink hpl = hpls[i] = new Hyperlink(msg);
            String url = msg;
            // process event
            hpl.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Click link");
                    webEngine.load(url);
                }
            });

            i++;
        }

        messageListView.setItems(FXCollections.observableArrayList(hpls));
    }
}
