package org.szelc.app.antstock.task;


import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.data.quotes.CurrentEvaluateMessages;

/**
 * @author by marcin.szelc on 2017-11-06.
 */
public class CurrentQuotesService extends Service<CurrentEvaluateMessages> {

    public CurrentQuotesService() {

    }

    @Override
    protected Task createTask() {
        return new CurrentQuotesTask();
    }
}
