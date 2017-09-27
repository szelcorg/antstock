package org.szelc.stockfromdata;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class MainStockFromData extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");

        //String fileName = "C:\\WORKSPACE\\AntStock3\\Storage\\stockQuotes\\mstall\\ASSECOPOL.csv";
        String fileName = "C:\\Temp\\line2.xlsx";

        final LineChart<Number,Number> lineChart = new StockLineChart(fileName);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();

        series.setName("My portfolio");

        Scene scene  = new Scene(lineChart,800,600);

        scene.getStylesheets().add("stylesheet.css");

        stage.setScene(scene);
        stage.show();
    }

    //@Override
    public void startBar(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("chartdata.fxml"));
        primaryStage.setTitle("Stock market");

        String fileName = "C://Temp/piechartdatas.xlsx";



        final PieChart chart = new StockPieChart(fileName);
        chart.setTitle("Graph");

        Scene scene = new Scene(new Group(), 1000, 900);
        primaryStage.setScene(scene);
        ((Group) scene.getRoot()).getChildren().add(chart);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
