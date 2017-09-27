package org.szelc.app.antstock.view.quotes.table;


import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.comparator.FloatFormatComparator;
import org.szelc.app.antstock.comparator.IntegerFormatComparator;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class QuotesTableView extends StockTableView{

    private static final Logger log = Logger.getLogger(QuotesTableView.class.toString());
    private static final String FXML_FILE_COMPONENT = "QuotesTableView.fxml";

    @FXML
    private TableView<DayCompanyQuote> table;
    private static final int INDEX_FIELD_ORDER = 0;
    private static final int INDEX_FIELD_COMPANY = 1;
    private static final int INDEX_FIELD_COURSE = 2;
    private static final int INDEX_FIELD_CHANGE = 3;
    private static final int INDEX_FIELD_REFERENCE = 4;
    private static final int INDEX_FIELD_OPEN = 5;
    private static final int INDEX_FIELD_HIGH = 6;
    private static final int INDEX_FIELD_LOW = 7;
    private static final int INDEX_FIELD_VOLUME = 8;
    private static final int INDEX_FIELD_VOLUME_MONEY = 9;
    private static final int INDEX_FIELD_DAY = 10;

    public QuotesTableView() {
        log.info("Loading component from file [" + FXML_FILE_COMPONENT + "]");
        loadComponent();
        table.getColumns().get(INDEX_FIELD_ORDER).setComparator(new IntegerFormatComparator());
        table.getColumns().get(INDEX_FIELD_CHANGE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_REFERENCE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_OPEN).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_HIGH).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_LOW).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_COURSE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_VOLUME).setComparator(new IntegerFormatComparator());
        table.getColumns().get(INDEX_FIELD_VOLUME_MONEY).setComparator(new FloatFormatComparator());
        
        defineFieldUpDownEqColor( table.getColumns().get(INDEX_FIELD_CHANGE));
    }

    private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(FXML_FILE_COMPONENT));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        ResourceBundle bundle = ResourceBundle.getBundle("locale", Locale.getDefault());

        fxmlLoader.setResources(bundle);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    public void setItems(ObservableList<DayCompanyQuote> list) {
        int i = 0;
        if(list==null){
            table.setItems(null);
            return;
        }
        for(DayCompanyQuote dcq: list){
            if(dcq==null){
                continue;
            }
            dcq.setOrder(++i);
        }
        table.setItems(list);
        TableColumn sortColumn1 = table.getColumns().get(INDEX_FIELD_CHANGE);
        TableColumn sortColumn2 = table.getColumns().get(INDEX_FIELD_COMPANY);
        sortColumn1.setSortType(TableColumn.SortType.DESCENDING);
        sortColumn2.setSortType(TableColumn.SortType.ASCENDING);

    }

    public ObservableList<DayCompanyQuote> getItems(){
        return table.getItems();
    }
   

}
