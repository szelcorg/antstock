package org.szelc.app.antstock.task;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import org.szelc.app.antstock.StockMessagesController;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.data.messages.CompanyMessagesList;
import org.szelc.app.antstock.data.quotes.CurrentEvaluateMessages;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.logger.LOG;
import org.szelc.stockthml.MessageLoader;
import org.szelc.stockthml.StockParser;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author by marcin.szelc on 2017-11-06.
 */
public class StockMessageTask extends Task<CompanyMessagesList> {
    private static final boolean requiredStop = false;
    private static boolean firtStart = true;
    @Override
    protected CompanyMessagesList call() throws Exception {
        LOG.i("CompanyMessagesList call");
        if(requiredStop && !firtStart){
            LOG.i("NOT FIRTS - WAIT");
            Thread.sleep(15 * 60 * 1000);
        }

        LOG.i("CompanyMessagesList loading");
        CompanyMessagesList msgList = MessageLoader.loadMessageForCompanies();
        if(firtStart){
            firtStart = false;
        }
        return msgList;
    }




}
