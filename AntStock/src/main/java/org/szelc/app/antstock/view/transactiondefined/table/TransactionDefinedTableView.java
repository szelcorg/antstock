package org.szelc.app.antstock.view.transactiondefined.table;



import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import org.szelc.app.antstock.view.table.TableInterface;
import org.szelc.app.antstock.view.table.event.TableUpdateEvent;
//import org.szelc.app.antstock.view.transaction.table.TransactionTableEditingCell;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.view.transactiondefined.TransactionDefinedTableEditingCell;
import org.szelc.app.antstock.view.transactiondefined.comparator.TransactionDefinedToActionComparator;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedTableView extends AnchorPane implements TableInterface {

    private static final Logger log = Logger.getLogger(TransactionDefinedTableView.class.toString());
    @FXML
    private TableView<TransactionDefined> table;
    private TableUpdateEvent tableUpdate;

    @FXML
    private TableColumn columnCompany;
    @FXML
    private TableColumn columnType;
    @FXML
    private TableColumn columnPrice;
    @FXML
    private TableColumn columnDateEffectiveFrom;
    @FXML
    private TableColumn columnDefinedActivity;
    @FXML
    private TableColumn columnToActionPercent;

    public TransactionDefinedTableView() {
        log.info("TransactionDefinedTable component constructor");
        loadComponent();
        setEditTable();
        setComparator();
    }

    private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/szelc/app/antstock/view/transactiondefined/table/TransactionDefinedTableView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    private void setEditTable() {
        setCellFactory(columnCompany);
        setCellFactory(columnType);
        setCellFactory(columnPrice);
        setCellFactory(columnDateEffectiveFrom);
    }

    
    private void setCellFactory(TableColumn column) {
        column.setCellFactory(new Callback<TableColumn<TransactionDefined, String>, TableCell<TransactionDefined, String>>() {
            @Override
            public TableCell<TransactionDefined, String> call(TableColumn<TransactionDefined, String> param) {
                return new TransactionDefinedTableEditingCell(column, TransactionDefinedTableView.this);
            }
        });
    }

    public ObservableList<TransactionDefined> getItems() {
        return table.getItems();
    }

    public void setItems(ObservableList<TransactionDefined> list) {
        table.setItems(list);
        columnCompany.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(columnCompany);
        table.sort();
    }

    public void addItem(Integer id, TransactionDefined data) {
        table.getItems().add(id, data);
    }

    public ObservableList<TransactionDefined> getSelectedItems() {
        return table.getSelectionModel().getSelectedItems();
    }

    @Override
    public void update() {
        log.info("Update from controller");
        
        if (tableUpdate != null) {
            tableUpdate.update();
        }

        columnToActionPercent.setVisible(false);
        columnToActionPercent.setVisible(true);
      
    }

    public void setTableUpdate(TableUpdateEvent tableUpdate) {
        table.setEditable(true);
        this.tableUpdate = tableUpdate;
    }

    public TableColumn getColumnCompany() {
        return columnCompany;
    }

    public TableColumn getColumnType() {
        return columnType;
    }

    public TableColumn getColumnPrice() {
        return columnPrice;
    }

    public TableColumn getColumnDateEffectiveFrom() {
        return columnDateEffectiveFrom;
    }
    
    

    public TableView<TransactionDefined> getTable() {
        return table;
    }

    private void setComparator() {
        columnToActionPercent.setComparator(new TransactionDefinedToActionComparator());
    }

    
    
    
}
