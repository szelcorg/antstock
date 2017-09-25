
package org.szelc.app.antstock.view.chart;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.lib.chart.ChartMouseHandler;
import org.szelc.app.antstock.lib.chart.ChartMouseHandlerCallback;

/**
 *
 * @author mszelc
 */
public class ChartViewController implements Initializable, ChartMouseHandlerCallback {

    private static final Logger log = Logger.getLogger(ChartViewController.class.toString());
    @FXML
    private BorderPane borderPane;
    @FXML
    private LineChart lineChart;
    @FXML
    private Label label1;
    Line lineHorizontal;
    Line lineVertical;
    Line lineAvg = new Line();
    ChartMouseHandler handler;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            
             handler = new ChartMouseHandler(this, 2000);
             
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(false);
            //xAxis.setLabel("Number of Month");
            if (lineChart != null) {
                borderPane.getChildren().remove(lineChart);
            }

            lineChart = new LineChart(
                    xAxis, yAxis,
                    FXCollections.observableArrayList(
                            new XYChart.Series(
                                    "Kurs",
                                    FXCollections.observableArrayList(
                                            plot("x")
                                    )
                            )
                    )
            );
           // lineChart.setCreateSymbols(false);
            log.info("TickLength "+lineChart.getYAxis().getTickLength());
            lineChart.getYAxis().setTickMarkVisible(true);
            lineChart.getYAxis().setTickLength(0.5);
            //lineChart.getYAxis().setSide(Side.RIGHT);

            //          lineChart.setCursor(Cursor.NONE);
            lineHorizontal = new Line(0, 0, 0, 0);
            lineVertical = new Line(0, 0, 0, 0);
            Pane p = new Pane();
            p.setPrefWidth(1600);
            p.setPrefHeight(500);

            //        p.setCursor(Cursor.NONE);
            p.getChildren().addAll(lineChart, lineHorizontal, lineVertical, lineAvg);
            lineChart.setPrefWidth(1600);
            lineChart.setPrefHeight(500);
            borderPane.setCenter(p);

            //new Thread(handler).start();
            log.info("RUN");
            
//            Task task = new Task() {
//            @Override
//            protected Void call() {
//                for(int i=0;i<100;i++){
//                   System.out.println("LKOGOWANIE");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        java.util.logging.Logger.getLogger(ChartViewController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                return null;
//            }};

           new Thread(handler).start();
              
            
            final Node chartBackground = lineChart.lookup(".chart-plot-background");
            addDrawChartLine3(lineChart);
             //addDrawChartBackground(chartBackground);

            addDrawLineHorizontal();

          
            
        } catch (Exception ex) {
            log.error(ex);
            System.exit(0);
        }

    }

    private void addDrawLineHorizontal() {
        lineHorizontal.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //log.info("LINE EVENT X " + mouseEvent.getX() + " y " + mouseEvent.getY());
                //handler.push(mouseEvent);
                call(mouseEvent);
            }
        });
    }

    int count = 0;

    private void addDrawChartLine3(Node line) {                
        
        line.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //log.info("Chart EVENT X " + mouseEvent.getX() + " y " + mouseEvent.getY());
                call(mouseEvent);
                //handler.push(mouseEvent);
                //redrawLine(mouseEvent);
            }
        });
    }
    
    
    
    @Override
    public void call(MouseEvent mouseEvent) {
        log.info("CALLBACK "+(++count));
       
        long time = System.currentTimeMillis();
        double xMouse = mouseEvent.getX();
        double yMouse = mouseEvent.getY();

        ObservableList<Axis.TickMark<?>> tickMarkList = lineChart.getXAxis().getTickMarks();
        Axis.TickMark<?> tickMark = tickMarkList.get(0);
        double jump = (tickMarkList.get(1).getPosition() - tickMark.getPosition());
        double leftMovement = lineChart.getXAxis().getLayoutX() + lineChart.getPadding().getLeft();
        double topMovement = lineChart.getYAxis().getLayoutY() + lineChart.getPadding().getTop();

        int xValue = (int) ((xMouse - leftMovement - tickMark.getPosition() + jump / 2) / jump);

        Object v = lineChart.getXAxis().getValueForDisplay(xMouse - leftMovement);
        //log.info("Value "+v);
        double xPos = lineChart.getXAxis().getDisplayPosition(v);

        if (xValue < 0) {
            xValue = 0;
        }
        int dataSize = lineChart.getXAxis().getChildrenUnmodifiable().size();
        if (xValue > dataSize - 3) {
            xValue = dataSize - 3;
        }

        double maxY = lineChart.getHeight() - lineChart.getXAxis().getHeight() - topMovement - 2 * lineChart.getYAxis().getBaselineOffset();
        if (yMouse > maxY) {
            yMouse = maxY;
        }
        if (yMouse < topMovement) {
            yMouse = topMovement;
        }

        if (true) {
            lineHorizontal.setStartX(leftMovement);
            lineHorizontal.setStartY(yMouse);
            lineHorizontal.setEndX(lineChart.getWidth() - lineChart.getBaselineOffset() + lineChart.getPadding().getLeft());
            lineHorizontal.setEndY(lineHorizontal.getStartY());
        }

        //Object yValue = lineChart.getYAxis().getValueForDisplay(Math.floor(yMouse) - topMovement);
        //log.info("yValue "+yValue +" yMouse "+yMouse + " topMovement "+topMovement);
        //lineVertical.setStartX(xPos + leftMovement); //to //Object yValue = lineChart.getYAxis().getValueForDisplay(Math.floor(yMouse) - topMovement);
        lineVertical.setStartX(xValue * jump + tickMark.getPosition() + leftMovement);
        lineVertical.setStartY(topMovement);
        lineVertical.setEndX(lineVertical.getStartX());
        lineVertical.setEndY(lineChart.getHeight() - lineChart.getXAxis().getHeight() - topMovement - 2 * lineChart.getYAxis().getBaselineOffset()); //115

//                lineChart.setCursor(Cursor.NONE);
//                lineHorizontal.toFront();                                
        lineAvg.setStartX(leftMovement);
        lineAvg.setStartY(lineChart.getYAxis().getDisplayPosition(27));
        lineAvg.setEndX(lineChart.getWidth() - lineChart.getBaselineOffset() + lineChart.getPadding().getLeft());
        lineAvg.setEndY(lineAvg.getStartY());

         if(true){
            return;
        }
        
        //log.info("TIME ["+(System.currentTimeMillis()-time));
        //log.info("Count "+count++);
    }

    public ObservableList<XYChart.Data<String, Number>> plot(String company) {

        List<DayCompanyQuote> dcqList = QuoteServiceFactory.instance().getQuoteService().getLastQuote("ZEPAK", 20);

        final ObservableList<XYChart.Data<String, Number>> dataset = FXCollections.observableArrayList();

        for (DayCompanyQuote dcq : dcqList) {
            final XYChart.Data<String, Number> data = new XYChart.Data<>("" + dcq.getDateStr(), dcq.getCourse());
            dataset.add(data);
//            data.setNode(
//                    new HoveredThresholdNode(
//                            "", "" + 2 + " 12"
//                    )
//            );

        }

//        for (int i = 12; i < 32; i++) {
//            final XYChart.Data<String, Number> data = new XYChart.Data<>("" + i, i);
//            dataset.add(data);
//            data.setNode(
//                    new HoveredThresholdNode(
//                            "", "" + i + " 12"
//                    )
//            );
//        }
        return dataset;
    }

    private void addDrawChartBackground(Node chartBackground) {

        chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                log.info("MouseX [" + mouseEvent.getX() + "]");
                double xMouse = mouseEvent.getX();
                double yMouse = mouseEvent.getY();
                double maxX = lineChart.getWidth() - lineChart.getBaselineOffset() + lineChart.getPadding().getLeft();

                ObservableList<Axis.TickMark<?>> tickMarkList = lineChart.getXAxis().getTickMarks();
                Axis.TickMark<?> tickMark = tickMarkList.get(0);
                Axis.TickMark<?> tickMark1 = tickMarkList.get(1);

                double jump = (tickMark1.getPosition() - tickMark.getPosition());
                double x = ((int) (xMouse / jump)) * jump + tickMark.getPosition();

                double leftMovement = lineChart.getXAxis().getLayoutX() + lineChart.getPadding().getLeft();
                double topMovement = lineChart.getYAxis().getLayoutY() + lineChart.getPadding().getTop();
                if (x >= maxX - leftMovement) {
                    x = maxX - leftMovement - tickMark.getPosition();
                }

                if (false) {
                    lineHorizontal.setStartX(leftMovement);
                    lineHorizontal.setStartY(yMouse + topMovement);
                    lineHorizontal.setEndX(maxX);
                    lineHorizontal.setEndY(lineHorizontal.getStartY());
                }

                lineVertical.setStartX(leftMovement + x);
                lineVertical.setStartY(topMovement);
                lineVertical.setEndX(lineVertical.getStartX());
                lineVertical.setEndY(lineChart.getHeight() - 65);

                lineChart.setCursor(Cursor.TEXT);
                lineHorizontal.toFront();

                Object yValue = lineChart.getYAxis().getValueForDisplay(yMouse);

                lineAvg.setStartX(leftMovement);
                lineAvg.setStartY(lineChart.getYAxis().getDisplayPosition(22));
                lineAvg.setEndX(maxX);
                lineAvg.setEndY(lineAvg.getStartY());
                //log.info("Y "+yValue+" domena "+yValue);

                log.info("xMouse [" + xMouse + "] x [" + x + "]");
            }
        });
    }

   

    class HoveredThresholdNode extends StackPane {

        HoveredThresholdNode(String priorValue, String value) {
            setPrefSize(6, 6);

            final Label label = createDataThresholdLabel(priorValue, value);

//            setOnMouseEntered(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    Circle circle = new Circle(50,Color.BLUE);
//                    //circle.relocate(20, 20);
//                    label1.setText("DATA ["+value+"]");
//                    log.info("Component ["+this+"]");
//                    getChildren().setAll(circle);
//                    
//                    setCursor(Cursor.NONE);
//                    toFront();
//                    toFront();
//
//                }
//            });
//            setOnMouseExited(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    getChildren().clear();
//                    setCursor(Cursor.CROSSHAIR);
//                }
//            });
        }

        private Label createDataThresholdLabel(String priorValue, String value) {
            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 10; -fx-font-weight: normal;");
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

}
