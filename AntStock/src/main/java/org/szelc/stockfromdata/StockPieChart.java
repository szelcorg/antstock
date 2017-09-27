package org.szelc.stockfromdata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;

/**
 * @author by marcin.szelc on 2017-09-15.
 */
public class StockPieChart extends PieChart {

    private Map<String, Double> datas = new HashMap();

    public StockPieChart(Map<String, Double> datas) {
        super(loadDatas(datas));
    }

    public StockPieChart(String filePath) {
        super(loadDatas(filePath));

    }

    private static ObservableList<Data> loadDatas(Map<String, Double> datas) {
        List<Data> inner = new ArrayList<>();
        if (datas == null || datas.isEmpty()) {
            throw new NullPointerException("Stock chart map is empty");
        }
        datas.forEach((k, v) -> inner.add(new Data(k, v)));
        return FXCollections.observableArrayList(inner);
    }


    private static ObservableList<Data> loadDatas(String filePath) {
        List<Data> inner = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(filePath);
        } catch (IOException e) {
            return FXCollections.observableArrayList(new ArrayList());
        }
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        while (iterator.hasNext()) {
            Row r = iterator.next();
            String key = r.getCell(0).toString();
            Double value = Double.valueOf(r.getCell(1).toString());
            inner.add(new Data(key, value));
        }
        return FXCollections.observableArrayList(inner);
    }
}
