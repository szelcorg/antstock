package org.szelc.app.antstock.view.transactiondefined;

import javafx.scene.control.TableColumn;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.table.TableInterface;
import org.szelc.app.antstock.view.table.cell.EditingCell;
import org.szelc.app.antstock.view.transaction.table.TransactionTableEditingCell;
import org.szelc.app.antstock.view.transactiondefined.table.TransactionDefinedTableView;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedTableEditingCell extends EditingCell<TransactionDefined, String> {

    private final Logger log = Logger.getLogger(TransactionTableEditingCell.class.toString());
  
    public TransactionDefinedTableEditingCell(TableColumn column, TableInterface tableInt) {
        super(column, tableInt);
    }

    @Override
    protected boolean acceptEdit() {

        TransactionDefinedTableView t = ((TransactionDefinedTableView) tableInt);
        if (column == t.getColumnCompany()) {
            getTableView().getSelectionModel().getSelectedItem().setCompany(textField.getText());
        }
        else if (column == t.getColumnType()) {
            getTableView().getSelectionModel().getSelectedItem().setTransactionType(TransactionType.valueOf(textField.getText()));
             getTableView().getSelectionModel().getSelectedItem().setToActionPercent(TransactionDefined.TO_ACTION_PERCENT_NOT_DEFINED);
        }
        else if (column == t.getColumnPrice()) {
            getTableView().getSelectionModel().getSelectedItem().setPriceToAction(Float.valueOf(textField.getText().replace(",", ".")));
            getTableView().getSelectionModel().getSelectedItem().setToActionPercent(TransactionDefined.TO_ACTION_PERCENT_NOT_DEFINED);
        }
        else if (column == t.getColumnDateEffectiveFrom()) {
            getTableView().getSelectionModel().getSelectedItem().setDateEffectiveFrom(textField.getText());
        }
        else if (column == t.getColumnDateEffectiveFrom()) {
            getTableView().getSelectionModel().getSelectedItem().setDateEffectiveFrom(textField.getText());
        }
        else if (column == t.getColumnBank()) {
            getTableView().getSelectionModel().getSelectedItem().setBank(textField.getText());
        }
        commitEdit(textField.getText());
        return true;
    }

}
