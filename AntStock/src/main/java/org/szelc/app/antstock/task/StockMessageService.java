package org.szelc.app.antstock.task;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.szelc.app.antstock.StockMessagesController;
import org.szelc.app.antstock.data.messages.CompanyMessagesList;

/**
 * @author by marcin.szelc on 2017-11-06.
 */
public class StockMessageService extends Service<CompanyMessagesList> {

    @Override
    protected Task<CompanyMessagesList> createTask() {
        return new StockMessageTask();
    }
}
