package org.szelc.app.antstock.view.transaction.table;

import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.loader.transaction.TransactionUtil;
import org.szelc.app.antstock.view.table.TableInterface;
import org.szelc.app.antstock.view.table.cell.EditingCell;
import java.text.ParseException;
import javafx.scene.control.TableColumn;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class TransactionTableEditingCell extends EditingCell<Transaction, String> {

    private final Logger log = Logger.getLogger(TransactionTableEditingCell.class.toString());
  
    public TransactionTableEditingCell(TableColumn column, TableInterface tableInt) {
        super(column, tableInt);
        TransactionTableView t = ((TransactionTableView) tableInt);
        if (column == t.getColumnPercentProvision()) {
            //Transaction transaction = getTableView().getSelectionModel().getSelectedItem();

                    //setPercentProvision(Float.valueOf(textField.getText().replace(",", ".")));
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        TransactionTableView t = ((TransactionTableView) tableInt);

    }

    @Override
    protected boolean acceptEdit() {
        
        TransactionTableView t = ((TransactionTableView) tableInt);

        Transaction transaction = getTableView().getSelectionModel().getSelectedItem();

        if (column == t.getColumnDay()) {
            try {
                transaction.setDay(Settings.TRANSACTION_DATE_FORMAT_IN_FILE.parse(textField.getText()));
            } catch (ParseException ex) {
                log.error(ex);
                System.exit(0);
            }
        } else if (column == t.getColumnCompany()) {
            transaction.setCompanyName(textField.getText());
        } else if (column == t.getColumnType()) {
            transaction.setTransactionType(TransactionType.valueOf(textField.getText()));
        } else if (column == t.getColumnPriceForOne()) {
            transaction.setPriceOneShare(Float.valueOf(textField.getText().replace(",", ".")));
        } else if (column == t.getColumnNumberShares()) {
            transaction.setNumberShares(Integer.valueOf(textField.getText()));
        } else if (column == t.getColumnPercentProvision()) {
            transaction.setPercentProvision(Float.valueOf(textField.getText().replace(",", ".")));
            transaction.setMoneyProvision(TransactionUtil.calculateProvision(transaction.getBankName(), (transaction.getPriceAllShares() * transaction.getPercentProvision())/100));

        } else if (column == t.getColumnMoneyProvision()) {
            transaction.setMoneyProvision(Float.valueOf(textField.getText().replace(",", ".")));
        } else if (column == t.getColumnBank()) {
            transaction.setBankName(BankEnum.valueOf(textField.getText()));
        }
        commitEdit(textField.getText());
        return true;
    }

}
