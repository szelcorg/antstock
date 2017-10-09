package org.szelc.app.antstock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.messages.CompanyMessagesList;
import org.szelc.app.antstock.view.admin.AdminViewController;
import org.szelc.stockthml.MessageLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author by marcin.szelc on 2017-09-25.
 */
public class StockMessagesController implements Initializable {

    private static final Logger LOG = Logger.getLogger(StockMessagesController.class.toString());

    @FXML
    WebView webViewMessages;
    private WebEngine webEngine;

    @FXML
    private Button refreshMessages;

    @FXML
    private void refreshMessagesEvent(ActionEvent e){
        LOG.info("refreshMessages ");
        refreshMessages();
    }

    @FXML
    private ListView messageListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webViewMessages.getEngine();
    }

    public void refreshMessages(){
        CompanyMessagesList msgList = MessageLoader.loadMessageForCompanies();

        Hyperlink[] hpls = new Hyperlink[msgList.getMessageList().size()];
        int i=0;
        for(CompanyMessagesList.Message msg : msgList.getMessageList()){
            Hyperlink hpl = hpls[i] = new Hyperlink(msg.getDate()+ " "+msg.getCompany()+" "+msg.getLink());
            String url = msg.getLink();

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
