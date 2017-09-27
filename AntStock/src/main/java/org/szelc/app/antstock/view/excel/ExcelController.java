/*
package org.szelc.app.antstock.view.excel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.net.URL;
import java.util.ResourceBundle;

public class ExcelController  implements Initializable {
    @FXML
    private SpreadsheetView spreadsheet;
    */
/**
     * Initializes the controller class.
     * @param url
     * @param rb
     *//*

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Grid starting");
        int rowCount = 5;
        int columnCount = 3;
        GridBase grid = new GridBase(rowCount, columnCount);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
            }
            rows.add(list);
        }
        grid.setRows(rows);
        spreadsheet.setGrid(grid);
        System.out.println("Grid created");
    }
}
*/
