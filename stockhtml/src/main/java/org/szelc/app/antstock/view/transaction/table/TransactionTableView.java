package org.szelc.app.antstock.view.transaction.table;

import org.szelc.app.antstock.comparator.FloatFormatComparator;
import org.szelc.app.antstock.comparator.IntegerFormatComparator;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.view.table.TableInterface;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
import java.io.IOException;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.log4j.Logger;

/**
 *
 * @author szelc.org
 */
public class TransactionTableView extends AnchorPane implements TableInterface {

    private Logger log = Logger.getLogger(TransactionTableView.class.toString());

    @FXML
    private TableView<Transaction> table;
    @FXML
    public TableColumn<Transaction, Integer> order;
    @FXML
    private TableColumn columnDay;
    @FXML
    private TableColumn columnCompany;
    @FXML
    private TableColumn columnType;
    @FXML
    private TableColumn columnPriceForOne;
    @FXML
    private TableColumn columnNumberShares;
    @FXML
    private TableColumn columnPriceForAll;
    @FXML
    private TableColumn columnPercentProvision;
    @FXML
    private TableColumn columnMoneyProvision;
    @FXML
    private TableColumn columnPriceTotal;
    @FXML
    private TableColumn columnBank;

    private TableUpdateEvent tableUpdate;

    public TransactionTableView() {
        log.info("TransactionTable component constructor");
        loadComponent();
        setEditForTransactionTable();
        setComparator();
    }

    private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/transaction/table/TransactionTableView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
        
    }

    private void setEditForTransactionTable() {
        table.setEditable(true);
        setCellFactory(columnBank);
        setCellFactory(columnCompany);
        setCellFactory(columnType);
        setCellFactory(columnDay);
        setCellFactory(columnMoneyProvision);
        setCellFactory(columnNumberShares);
        setCellFactory(columnPercentProvision);
        setCellFactory(columnPriceForAll);
        setCellFactory(columnPriceForOne);
        setCellFactory(columnPriceTotal);
    }

    private void setComparator() {
        columnPriceForOne.setComparator(new FloatFormatComparator());
        columnNumberShares.setComparator(new IntegerFormatComparator());
        columnPriceForAll.setComparator(new FloatFormatComparator());
        columnPercentProvision.setComparator(new FloatFormatComparator());
        columnMoneyProvision.setComparator(new FloatFormatComparator());
        columnPriceTotal.setComparator(new FloatFormatComparator());
       
    }

    private void setCellFactory(TableColumn column) {
        column.setCellFactory(new Callback<TableColumn<Transaction, String>, TableCell<Transaction, String>>() {
            @Override
            public TableCell<Transaction, String> call(TableColumn<Transaction, String> param) {
                return new TransactionTableEditingCell(column, TransactionTableView.this);
            }
        });
    }

    public ObservableList<Transaction> getItems() {
        return table.getItems();
    }

    public void setItems(ObservableList<Transaction> list) {
        table.setItems(list);

        Iterator<Transaction> it = table.getItems().iterator();
        int i=0;
        while(it.hasNext()){
            Transaction tr = it.next();
            tr.setOrder(++i);
        }

        columnDay.setSortType(TableColumn.SortType.DESCENDING);
        columnCompany.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(columnDay);
        table.getSortOrder().add(columnCompany);
        table.sort();
    }

    public void addItem(Integer id, Transaction data) {
        table.getItems().add(id, data);
    }

    public ObservableList<Transaction> getSelectedItems() {
        return table.getSelectionModel().getSelectedItems();
    }

    @Override
    public void update() {
        log.info("Update from controller");
        if (tableUpdate != null) {
            log.info("Exist listener");
            tableUpdate.update();
        }
    }

    public void setTableUpdate(TableUpdateEvent tableUpdate) {
        this.tableUpdate = tableUpdate;
    }

    public TableColumn getColumnDay() {
        return columnDay;
    }

    public TableColumn getColumnCompany() {
        return columnCompany;
    }

    public TableColumn getColumnPriceForOne() {
        return columnPriceForOne;
    }

    public TableColumn getColumnNumberShares() {
        return columnNumberShares;
    }

    public TableColumn getColumnPriceForAll() {
        return columnPriceForAll;
    }

    public TableColumn getColumnPercentProvision() {
        return columnPercentProvision;
    }

    public TableColumn getColumnMoneyProvision() {
        return columnMoneyProvision;
    }

    public TableColumn getColumnPriceTotal() {
        return columnPriceTotal;
    }

    public TableColumn getColumnBank() {
        return columnBank;
    }

    public TableColumn getColumnType() {
        return columnType;
    }
  
}
