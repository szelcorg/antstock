package org.szelc.stockfromdata;

import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;

/**
 * @author by marcin.szelc on 2017-09-15.
 */
public class StockLineChart extends LineChart{

    private Map<String, Double> datas = new HashMap();
    private static final NumberAxis xAxis = new NumberAxis();
    private static final NumberAxis yAxis = new NumberAxis();

    public StockLineChart(Map<Double, Double> datas) {
        super(xAxis, yAxis);
        loadDatas(datas);

    }

    public StockLineChart(String filePath) {
        super(xAxis, yAxis);
        loadDatas(filePath);
        setCreateSymbols(false);
        getStyleClass().add("thick-chart");
        //setVerticalGridLinesVisible(false);
    }

    private void loadDatas(Map<Double, Double> datas) {
        Series series = new Series();
        List<Data> inner = new ArrayList<>();
        if (datas == null || datas.isEmpty()) {
            throw new NullPointerException("Stock chart map is empty");
        }
        datas.forEach((k, v) -> inner.add(new Data(k, v)));
        series.setData(FXCollections.observableArrayList(inner));

        getData().add(series);
    }


    private  void loadDatas(String filePath) {
        Series series = new Series();
        List<Data> inner = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(filePath);
        } catch (IOException e) {
            return;
        }
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        Double key = 0d;
        while (iterator.hasNext()) {
            Row r = iterator.next();
            //Double key =  Double.valueOf(r.getCell(1).toString()) - 19980602;
            key++;
            //String key = r.getCell(1).toString();
            Double value = Double.valueOf(r.getCell(5).toString());
            inner.add(new Data(key, value));
        }
        series.setData(FXCollections.observableArrayList(inner));
        getData().add(series);
    }
}
