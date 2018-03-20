package org.szelc.app.antstock.view.evaluate.table.cell;

import java.util.Date;

import org.szelc.app.antstock.data.enumeration.MarketEnum;
import org.szelc.app.antstock.data.enumeration.RatingEnum;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.view.table.TableInterface;
import org.szelc.app.antstock.view.table.cell.EditingCell;
import javafx.scene.control.TableColumn;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.enumeration.SectorEnum;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.evaluate.table.EvaluateTableView;

/**
 *
 * @author szelc.org
 */
public class EvaluateTableEditingCell extends EditingCell<Evaluate, String> {

    private final Logger log = Logger.getLogger(EvaluateTableEditingCell.class.toString());

    public EvaluateTableEditingCell(TableColumn column, TableInterface tableInt) {
        super(column, tableInt);
    }

    @Override
    protected boolean acceptEdit() {
        EvaluateTableView t = ((EvaluateTableView) tableInt);
        String newValue = textField.getText().replace(",", ".");
        Evaluate selEval = getTableView().getSelectionModel().getSelectedItem();
        if (column == t.getColumnCompany()) {
            selEval.setCompanyName(textField.getText());
        } else if (column == t.getColumnRequiredPriceToBuy()) {
            selEval.setRequiredPriceToBuy(Float.valueOf(newValue));
            setDateBuySell(selEval, new Date());
        } else if (column == t.getColumnRequiredPriceToSell()) {
            selEval.setRequiredPriceToSell(Float.valueOf(newValue));
            setDateBuySell(selEval, new Date());
        } else if (column == t.getColumnPriceToBookValue()) {
            selEval.setPriceToBookValue(Float.valueOf(newValue));
            setDatePointer(selEval, new Date());
        } else if (column == t.getColumnPriceToEarning()) {
            selEval.setPriceToEarning(Float.valueOf(newValue));
            setDatePointer(selEval, new Date());
        } else if (column == t.getColumnRating()) {
            selEval.setRating(RatingEnum.fromName(textField.getText()));
            setDatePointer(selEval, new Date());
        } else if (column == t.getColumnZScore()) {
            selEval.setZscore(Float.valueOf(newValue));
            setDatePointer(selEval, new Date());
        } else if (column == t.getColumnPriceWhenEvaluatePEPBV()) {
            selEval.setPriceWhenEvaluatePEPBV(Float.valueOf(newValue));
            setDatePointer(selEval, new Date());
        } else if (column == t.getColumnDividendMoney()) {
            selEval.setDividendInZL(Float.valueOf(newValue));
        } else if (column == t.getColumnSector()) {
            selEval.setSector(SectorEnum.valueOf(textField.getText()));
        }
        else if (column == t.getColumnMarket()) {
            selEval.setMarket(MarketEnum.valueOf(textField.getText()));
        }
        else if (column == t.getColumnDateEarliestBuySell()) {
            selEval.setDateEarliestBuySell(newValue);
        } else if (column == t.getColumnDateLatestBuySell()) {
            selEval.setDateLatestBuySell(newValue);
        } else if (column == t.getColumnDateNextUpdateBuySell()) {
            selEval.setDateNextUpdateBuySell(newValue);
        } else if (column == t.getColumnDividendDay()) {
            selEval.setDividendDay(newValue);
        } else if (column == t.getColumnDividendPaymentDay()) {
            selEval.setDividendPaymentDay(newValue);
        }
        else if(column==t.getColumnDividend5year()){
            selEval.setDividend5year(Integer.valueOf(newValue));
        }
        else {
            log.warn("Column not found in EvaluateTable");
        }

        commitEdit(textField.getText());
        return true;
    }

    public void setDateBuySell(Evaluate eval, Date date) {
        eval.setDateChangeBuySell(Settings.TODAY_DATE_FORMAT.format(date));
    }

    public void setDatePointer(Evaluate eval, Date date) {
        eval.setDateChangePointer(Settings.TODAY_DATE_FORMAT.format(date));
    }
}
