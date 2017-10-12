package org.szelc.app.antstock.view.evaluate.table;

import org.szelc.app.antstock.data.enumeration.MarketEnum;
import org.szelc.app.antstock.statistic.CompanyTransactionStatistic;
import org.szelc.app.antstock.view.evaluate.table.cell.EvaluateTableEditingCell;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import org.szelc.app.antstock.view.table.TableInterface;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.comparator.FloatFormatComparator;
import org.szelc.app.antstock.comparator.RatingComparator;
import org.szelc.app.antstock.data.enumeration.RatingEnum;
import org.szelc.app.antstock.data.enumeration.SectorEnum;
import org.szelc.app.antstock.view.evaluate.EvaluateViewController;
import org.szelc.app.antstock.view.quotes.table.StockTableView;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class EvaluateTableView extends StockTableView implements TableInterface {

    private static final Logger log = Logger.getLogger(EvaluateTableView.class.toString());
    private static final String FXML_FILE_COMPONENT = "/org/szelc/app/antstock/view/evaluate/table/EvaluateTableView.fxml";

    private EvaluateViewController controller;
    @FXML
    private TableView<Evaluate> table;

    @FXML
    public TableColumn<Evaluate, Integer> order;
    @FXML
    public TableColumn<Evaluate, String> columnCompany;
    @FXML
    public TableColumn<Evaluate, String> columnPercentToBuy;
    @FXML
    public TableColumn<Evaluate, String> columnRequiredPriceToBuy;
    @FXML
    public TableColumn<Evaluate, String> columnCourse;
    @FXML
    public TableColumn<Evaluate, String> columnChange;
    @FXML
    public TableColumn<Evaluate, String> columnRequiredPriceToSell;
    @FXML
    public TableColumn<Evaluate, String> columnPercentToSell;
    @FXML
    public TableColumn<Evaluate, String> columnPriceToBookValue;
    @FXML
    public TableColumn<Evaluate, String> columnPriceToEarning;
    @FXML
    public TableColumn<Evaluate, String> columnRating;
    @FXML
    public TableColumn<Evaluate, String> columnZScore;
    @FXML
    public TableColumn<Evaluate, String> columnPriceWhenEvaluatePEPBV;
    @FXML
    public TableColumn<Evaluate, String> columnDividendMoney;
    @FXML
    public TableColumn<Evaluate, String> columnMarket;
    @FXML
    public TableColumn<Evaluate, String> columnSector;
    @FXML
    public TableColumn<Evaluate, String> columnDateNextUpdateBuySell;
    @FXML
    public TableColumn<Evaluate, String> columnDateEarliestBuySell;
    @FXML
    public TableColumn<Evaluate, String> columnDateLatestBuySell;
    @FXML
    public TableColumn<Evaluate, String> columnDividendDay;
    @FXML
    public TableColumn<Evaluate, String> columnDividendPaymentDay;

    private TableUpdateEvent evaluateTableUpdate;

    public EvaluateTableView() {
        log.info("Loading component from file [" + FXML_FILE_COMPONENT + "]");
        loadComponent();
        setEditForEvaluateTable();
        setComparator();
        defineFieldUpEqColor(columnPercentToBuy);
        defineFieldDownEqColor(columnPercentToSell);
        defineFieldUpDownEqColor(columnChange);
    }

    public void setController(EvaluateViewController controller) {
        this.controller = controller;
    }

    @Override
    public void update() {
        log.info("Update [" + FXML_FILE_COMPONENT + "]");

        if (evaluateTableUpdate != null) {
            log.info("EvaluateTableUpdate");
            evaluateTableUpdate.update();
        }

        if (controller.isAutoUpdateAfterSort()) {
            log.info("Set auto sort");
            table.sort();
        }

        refreshColumn(columnPercentToBuy);
        refreshColumn(columnPercentToSell);
        refreshColumn(columnPriceToEarning);
        refreshColumn(columnPriceToBookValue);
    }

    private void refreshColumn(TableColumn c) {
        c.setVisible(false);
        c.setVisible(true);
    }

    public void setTableUpdate(TableUpdateEvent evaluateTableUpdate) {
        this.evaluateTableUpdate = evaluateTableUpdate;
    }

    public void setItems(ObservableList<Evaluate> list) {
        table.setItems(list);
        columnPercentToBuy.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(columnPercentToBuy);
        table.sort();

        Iterator<Evaluate> it = table.getItems().iterator();
        int i=0;
        while(it.hasNext()){
            Evaluate ev= it.next();
            ev.setOrder(++i);
        }
    }

    public TableColumn<Evaluate, String> getColumnCompany() {
        return columnCompany;
    }

    public TableColumn<Evaluate, String> getColumnPercentToBuy() {
        return columnPercentToBuy;
    }

    public TableColumn<Evaluate, String> getColumnRequiredPriceToBuy() {
        return columnRequiredPriceToBuy;
    }

    public TableColumn<Evaluate, String> getColumnCourse() {
        return columnCourse;
    }

    public TableColumn<Evaluate, String> getColumnRequiredPriceToSell() {
        return columnRequiredPriceToSell;
    }

    public TableColumn<Evaluate, String> getColumnPercentToSell() {
        return columnPercentToSell;
    }

    public TableColumn<Evaluate, String> getColumnPriceToBookValue() {
        return columnPriceToBookValue;
    }

    public TableColumn<Evaluate, String> getColumnPriceToEarning() {
        return columnPriceToEarning;
    }

    public TableColumn<Evaluate, String> getColumnRating() {
        return columnRating;
    }

    public TableColumn<Evaluate, String> getColumnZScore() {
        return columnZScore;
    }

    public TableColumn<Evaluate, String> getColumnPriceWhenEvaluatePEPBV() {
        return columnPriceWhenEvaluatePEPBV;
    }

    public TableColumn<Evaluate, String> getColumnDividendMoney() {
        return columnDividendMoney;
    }

    public TableColumn<Evaluate, String> getColumnSector() {
        return columnSector;
    }

    public TableColumn<Evaluate, String> getColumnMarket() {
        return columnMarket;
    }

    public TableColumn<Evaluate, String> getColumnDateNextUpdateBuySell() {
        return columnDateNextUpdateBuySell;
    }

    public void setColumnDateNextUpdateBuySell(TableColumn<Evaluate, String> columnDateNextUpdateBuySell) {
        this.columnDateNextUpdateBuySell = columnDateNextUpdateBuySell;
    }

    public TableColumn<Evaluate, String> getColumnDateEarliestBuySell() {
        return columnDateEarliestBuySell;
    }

    public void setColumnDateEarliestBuySell(TableColumn<Evaluate, String> columnDateEarliestBuySell) {
        this.columnDateEarliestBuySell = columnDateEarliestBuySell;
    }

    public TableColumn<Evaluate, String> getColumnDateLatestBuySell() {
        return columnDateLatestBuySell;
    }

    public void setColumnDateLatestBuySell(TableColumn<Evaluate, String> columnDateLatestBuySell) {
        this.columnDateLatestBuySell = columnDateLatestBuySell;
    }

    public TableColumn<Evaluate, String> getColumnDividendDay() {
        return columnDividendDay;
    }

    public void setColumnDividendDay(TableColumn<Evaluate, String> columnDividendDay) {
        this.columnDividendDay = columnDividendDay;
    }

    public TableColumn<Evaluate, String> getColumnDividendPaymentDay() {
        return columnDividendPaymentDay;
    }

    public void setColumnDividendPaymentDay(TableColumn<Evaluate, String> columnDividendPaymentDay) {
        this.columnDividendPaymentDay = columnDividendPaymentDay;
    }

    private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(FXML_FILE_COMPONENT));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);

        }
    }

    private void setEditForEvaluateTable() {
        table.setEditable(true);
        setCellFactory(columnCompany);
        setCellFactory(columnDividendMoney);
        setCellFactory(columnPriceToBookValue);
        setCellFactory(columnPriceToEarning);
        setCellFactory(columnRating, RatingEnum.valuesString());
        setCellFactory(columnRequiredPriceToBuy);
        setCellFactory(columnRequiredPriceToSell);
        setCellFactory(columnSector, SectorEnum.valuesString());
        setCellFactory(columnMarket, MarketEnum.valuesString());
        setCellFactory(columnZScore);
        setCellFactory(columnPriceWhenEvaluatePEPBV);
        setCellFactory(columnDateEarliestBuySell);
        setCellFactory(columnDateLatestBuySell);
        setCellFactory(columnDateNextUpdateBuySell);
        setCellFactory(columnDividendDay);
        setCellFactory(columnDividendPaymentDay);
    }

    private void setComparator() {
        columnPercentToBuy.setComparator(new FloatFormatComparator());
        columnRequiredPriceToBuy.setComparator(new FloatFormatComparator());
        columnCourse.setComparator(new FloatFormatComparator());
        columnChange.setComparator(new FloatFormatComparator());
        columnRequiredPriceToSell.setComparator(new FloatFormatComparator());
        columnPercentToSell.setComparator(new FloatFormatComparator());
        columnPriceToBookValue.setComparator(new FloatFormatComparator());
        columnPriceToEarning.setComparator(new FloatFormatComparator());
        columnRating.setComparator(new RatingComparator());
        columnZScore.setComparator(new FloatFormatComparator());
        columnDividendMoney.setComparator(new FloatFormatComparator());
        columnPriceWhenEvaluatePEPBV.setComparator(new FloatFormatComparator());
    }

    private void setCellFactory(final TableColumn column) {
        column.setCellFactory(new Callback<TableColumn<Evaluate, String>, TableCell<Evaluate, String>>() {
            @Override
            public TableCell<Evaluate, String> call(TableColumn<Evaluate, String> param) {
                return new EvaluateTableEditingCell(column, EvaluateTableView.this);
            }
        });
    }

    private void setCellFactory(final TableColumn column, final List<String> autoCompleteList) {
        column.setCellFactory(new Callback<TableColumn<Evaluate, String>, TableCell<Evaluate, String>>() {
            @Override
            public TableCell<Evaluate, String> call(TableColumn<Evaluate, String> param) {
                EvaluateTableEditingCell cell = new EvaluateTableEditingCell(column, EvaluateTableView.this);
                cell.setAutocompleteList(autoCompleteList);
                return cell;
            }
        });
    }

    public TableView<Evaluate> getTable() {
        return table;
    }

}
