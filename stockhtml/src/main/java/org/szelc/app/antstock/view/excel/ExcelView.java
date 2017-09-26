package org.szelc.app.antstock.view.excel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class allows for the displaying of excel files within a window by
 * utilizing SpreadsheetView from controlsFX and the reading capabilities of     the POI library.
 *
 * Only usable for .xlsx files
 *
 * @author Theo
 *
 */
public class ExcelView implements Initializable {

    /**
     * Path to Excel
     */
    private String filePath;

    private int sheetIndex;

    private boolean editible;

    private FileInputStream inStream;
    private XSSFWorkbook poiWorkbook;
    private XSSFSheet poiSheet;

    private SpreadsheetView theView;

    @FXML
    private BorderPane excelViewBorder;

    public ExcelView() {
    }

    public void createExcelView(String path, int sheetIndex , boolean editable){
        filePath  =path;
        this.editible  =editable;
        this.sheetIndex  =sheetIndex;
    }

    public ExcelView(String path, int sheetIndex){
        filePath  =path;
        this.editible  =false;
        this.sheetIndex = sheetIndex;
    }

    private void initializeView() throws Exception{
        GridBase grid = excelToGrid();

        theView = new SpreadsheetView(grid);
        theView.setEditable(editible);
    }

    public SpreadsheetView getView() throws Exception{
        initializeView();
        return theView;
    }

    public void showInNewWindow(){
        Parent root;
        try {

            initializeView();

            root = theView;
            Stage stage = new Stage();
            stage.setTitle(new File(filePath).getName());
            stage.setScene(new Scene(root, 450, 450));

            //stage.getIcons().addAll(ResourceLoader.getIcons("Excel.ico"));

            stage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the values in the view. This may happen after the Excel file has been
     * modified after the initial reading.
     * @throws Exception
     */
    public void updateView() throws Exception{
        GridBase newgrid = excelToGrid();

        theView.setGrid(newgrid);
    }

    /**
     * Creates a {@link GridBase} object from the excel file located at the path
     * @return
     * @throws Exception - when opening the file
     */
    private GridBase excelToGrid() throws Exception{

        // Read the Excel document and collect the rows
        openBook();
        poiSheet = poiWorkbook.getSheetAt(sheetIndex);

        int[] size =  getSize();
        GridBase grid = new GridBase(size[0], size[1]);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        Row poiRow;
        Cell cell;
        String value;
        FormulaEvaluator evaluator = poiWorkbook.getCreationHelper().createFormulaEvaluator();

        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            poiRow = poiSheet.getRow(row);
            for (int column = 0; column < grid.getColumnCount(); ++column) {

                cell = poiRow.getCell(column);
                value = cell.getStringCellValue();
                //value = ExcelUtils.cellStringValue(evaluator,cell);

                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,value));
            }
            rows.add(list);
        }
        grid.setRows(rows);

        closeBook();

        return grid;
    }

    /**
     * Calculates the number of rows and columns in the sheet by looping
     * and reading all the things :)
     * @return the size as int[{rows, cols}]
     */
    private int[] getSize(){

        int numRows = 0;
        int numCols =0;

        int nullRowCounter = 0;
        int nullColCounter = 0;

        int maxNullRows = 6;
        int maxNullCols = 6;

        Row row;
        Cell cell;
        int localColCounter;

        while(true){

            row= poiSheet.getRow(numRows);
            numRows++;

            // Check row...
            if(row == null){
                nullRowCounter++;
            }
            else{
                nullRowCounter = 0;
                // If row not null, check columns...
                localColCounter = 0;
                while(true){
                    cell = row.getCell(localColCounter);
                    localColCounter++;
                    if(cell==null){
                        nullColCounter++;
                    }else{
                        nullColCounter = 0;
                    }

                    if(nullColCounter == maxNullCols){
                        // reached max null cells
                        localColCounter -= maxNullCols;

                        if(localColCounter >  numCols)
                            numCols = localColCounter;

                        break;
                        // go to next row...
                    }

                }
            }

            if(nullRowCounter == maxNullRows){
                // reached max null rows
                numRows -= maxNullRows;

                break;

            }
        }
        return new int[]{numRows, numCols};

    }

    private void openBook() throws Exception{
        try {
            File myFile = new File(filePath);
            inStream = new FileInputStream(myFile);

            poiWorkbook = new XSSFWorkbook (inStream);

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void closeBook() throws Exception{


        try {
            //poiWorkbook.close();
            inStream.close();

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("***ExcelView");
        createExcelView("C://Temp/excel.xlsx",1, false);
        try {
            //initializeView();
            excelViewBorder.setCenter(getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}