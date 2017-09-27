package org.szelc.app.antstock.view.statistic.table;

import java.io.IOException;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.quotes.QuotesStatistic;

/**
 *
 * @author szelc.org
 */
public class StatisticTableView extends AnchorPane {

    private static final Logger log = Logger.getLogger(StatisticTableView.class.toString());
    @FXML
    private TableView<QuotesStatistic> table;
    @FXML
    public TableColumn<Transaction, Integer> order;
    @FXML
    private TableColumn columnCompany;
    @FXML
    private TableColumn columnDateFrom;
    @FXML
    private TableColumn columnDateTo;
    @FXML
    private TableColumn columnNumbers;
    @FXML
    private TableColumn columnUp;
    @FXML
    private TableColumn columnDown;
    @FXML
    private TableColumn columnEqual;
     @FXML
    private TableColumn columnUpPercent;
    @FXML
    private TableColumn columnDownPercent;
    @FXML
    private TableColumn columnEqualPercent;
    @FXML
    private TableColumn columnMin;
    @FXML
    private TableColumn columnMax;
    @FXML
    private TableColumn columnMinToMaxPercent;
    @FXML
    private TableColumn columnFirst;
    @FXML
    private TableColumn columnAvgToLastPercent;
    @FXML
    private TableColumn columnMinToLastPercent;
    @FXML
    private TableColumn columnMaxToLastPercent;    
    @FXML
    private TableColumn columnMaxToLastPercentSumMinToLastPercent;
    @FXML
    private TableColumn columnCurrent;
    @FXML
    private TableColumn columnFirstToCurrentPercent;    
    @FXML
    private TableColumn columnAvgArithmeticClose;

    public StatisticTableView() {
        load();
        setComparators();
    }

    private void load() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(Settings.STATISTIC_TABLE_VIEW_FXML));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    private void setComparators() {
        columnNumbers.setComparator(Settings.COMPARATOR_INT_FORMAT);
        columnUp.setComparator(Settings.COMPARATOR_INT_FORMAT);
        columnDown.setComparator(Settings.COMPARATOR_INT_FORMAT);
        columnEqual.setComparator(Settings.COMPARATOR_INT_FORMAT);
        columnUpPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnDownPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnEqualPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMin.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMax.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMinToMaxPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnFirst.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnAvgToLastPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnCurrent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMinToLastPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMaxToLastPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnFirstToCurrentPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnAvgArithmeticClose.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
        columnMaxToLastPercentSumMinToLastPercent.setComparator(Settings.COMPARATOR_FLOAT_FORMAT);
    }

    public ObservableList<QuotesStatistic> getItems() {
        return table.getItems();
    }
//

    public void setItems(ObservableList<QuotesStatistic> list) {
        table.setItems(list);
        Iterator<QuotesStatistic> it = table.getItems().iterator();
        int i=0;
        while(it.hasNext()){
            QuotesStatistic qs = it.next();
            qs.setOrder(++i);
        }
        columnCompany.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(columnCompany);
        table.sort();
    }

}
