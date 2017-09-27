
package org.szelc.app.antstock.view.quotes.chart;

import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.service.QuoteService;
import org.szelc.app.antstock.chart.BarData;
import org.szelc.app.antstock.chart.CandleStickChartBuilder;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.view.statistic.StatisticViewController;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class QuotesChartViewController implements Initializable {

    private static final Logger log = Logger.getLogger(QuotesChartViewController.class.toString());

    @FXML
    private BorderPane chartBorderPane;

    @FXML
    private GridPane chartGridPane;

    private TextField companyField;
    
    private LineChart lineChart;
    
      @FXML
    private DatePicker dateFromDP;

    @FXML
    private DatePicker dateToDP;

    @FXML
    private Button showBtn;
    
    private int numberPointOnChart = 1000;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String defaultCompany = "TAURONPE";
        setCompanies(CompanyServiceFactory.instance().getCompanyService().getCompanyTransactionedOrEvaluated(true));
        showCandleStickChart(defaultCompany);
        showPriceLineChart(defaultCompany);
        
        showBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

            }
        });
    }

    private void showPriceLineChart(String company) {
        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(false);
            //xAxis.setLabel("Number of Month");
            if(lineChart!=null){
               
                chartGridPane.getChildren().remove(lineChart);
            }
            
            lineChart = new LineChart(
                    xAxis, yAxis,
                    FXCollections.observableArrayList(
                            new XYChart.Series(
                                    "Kurs",
                                    FXCollections.observableArrayList(
                                            plot(company)
                                    )
                            )
                    )
            );
            lineChart.setCursor(Cursor.CROSSHAIR);
            chartGridPane.add(lineChart, 0, 0);
        } catch (Exception ex) {
            log.error(ex);
            System.exit(0);
        }
    }

    private void showCandleStickChart(String company) {
        CandleStickChartBuilder builder = new CandleStickChartBuilder(company);
        Date from = null;
        Date to = null;
        try {
            from = Settings.QUOTE_CHART_VIEW_DATE_FORMAT.parse("2015-01-02");
            to = Settings.QUOTE_CHART_VIEW_DATE_FORMAT.parse("2015-01-21");
        } catch (ParseException ex) {
            log.error(ex);
            System.exit(0);
        }
        try {

            chartGridPane.add(builder.buildStock(buildBars(company, from, to)), 0, 1);
        } catch (Exception ex) {
            log.error(ex);
            System.exit(0);
        }
    }

    public XYChart.Series buildPriceXYSeries(String company, Date from, Date to) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        QuoteService quoteService = QuoteServiceFactory.instance().getQuoteService();
        QuoteRepository quoteRepository;
        try {
            quoteRepository = quoteService.getQuoteRepository();
        } catch (NullPointerException ex) {
            log.warn("Can't load QUOTES [" + ex.getMessage() + "]");
            System.exit(0);
            return null;
        }
        List<DayCompanyQuote> dayCompanyQuoteList = quoteRepository.getDayQuotesList(company);
        int size = dayCompanyQuoteList.size();
        int countLastRecords = numberPointOnChart;
        dayCompanyQuoteList = dayCompanyQuoteList.subList(size - countLastRecords, size);
        double previousClose = 1850;
        SimpleDateFormat sdf = new SimpleDateFormat();
        GregorianCalendar now = new GregorianCalendar();
        for (int i = 0; i < dayCompanyQuoteList.size(); i++) {
            DayCompanyQuote dcq = dayCompanyQuoteList.get(i);

            double close = dcq.getCourse();
            previousClose = close;
            Date date = dcq.getDate();
            series.getData().add(new XYChart.Data(dcq.getDateStr(), close));
        }
        return series;
    }

    public List<BarData> buildBars(String company, Date from, Date to) {
        QuoteService quoteService = QuoteServiceFactory.instance().getQuoteService();
        QuoteRepository quoteRepository;
        try {
            quoteRepository = quoteService.getQuoteRepository();
        } catch (NullPointerException ex) {
            log.warn("Can't load QUOTES [" + ex.getMessage() + "]");       
            System.exit(0);
            return null;
        }
        List<DayCompanyQuote> dayCompanyQuoteList = quoteRepository.getDayQuotesList(company);
        if(dayCompanyQuoteList==null || dayCompanyQuoteList.isEmpty()){
            dayCompanyQuoteList = quoteService.getLastQuote(company, 10);
        }
        int size = dayCompanyQuoteList.size();
        int countLastRecords = size > numberPointOnChart ? numberPointOnChart: size;
        dayCompanyQuoteList = dayCompanyQuoteList.subList(size - countLastRecords, size);
        double previousClose = 1850;
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final List<BarData> bars = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (int i = 0; i < dayCompanyQuoteList.size(); i++) {
            DayCompanyQuote dcq = dayCompanyQuoteList.get(i);
            double open = dcq.getOpen();//getNewValue(previousClose);
            double close = dcq.getCourse();//getNewValue(open);
            double high = dcq.getHigh();//Math.max(open + getRandom(), close);
            double low = dcq.getLow();//Math.min(open - getRandom(), close);

            previousClose = close;
            Date date = dcq.getDate();
            BarData bar = new BarData(date, open, high, low, close, 1);
            bars.add(bar);
        }
        return bars;
    }

    protected double getNewValue(double previousValue) {
        int sign;

        if (Math.random() < 0.5) {
            sign = -1;
        } else {
            sign = 1;
        }
        return getRandom() * sign + previousValue;
    }

    protected double getRandom() {
        double newValue = 0;
        newValue = Math.random() * 10;
        return newValue;
    }

    public void setCompanies(Set<String> companies) {
/**
        //companyField = TextFields.createClearableTextField();
        TextFields.bindAutoCompletion(companyField, companies);

        FlowPane pane = (FlowPane) chartBorderPane.topProperty().get();
        pane.getChildren().add(companyField);

        companyField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("ENTER");
                    showCandleStickChart(companyField.getText());
                    showPriceLineChart(companyField.getText());
                }
            }
        });
   */
    }

    public ObservableList<XYChart.Data<String, Number>> plot(String company) {
        final ObservableList<XYChart.Data<String, Number>> dataset = FXCollections.observableArrayList();

        QuoteService quoteService = QuoteServiceFactory.instance().getQuoteService();
        QuoteRepository quoteRepository;
        try {
            quoteRepository = quoteService.getQuoteRepository();
        } catch (NullPointerException ex) {
            log.warn("Can't load QUOTES [" + ex.getMessage() + "]");
            System.exit(0);
            return null;
        }
        List<DayCompanyQuote> dayCompanyQuoteList = quoteRepository.getDayQuotesList(company);
        if (dayCompanyQuoteList == null || dayCompanyQuoteList.isEmpty()) {
            dayCompanyQuoteList = quoteService.getLastQuote(company, numberPointOnChart);
        }
        int size = dayCompanyQuoteList.size();
       // int countLastRecords = size > 100 ? 100: size;
        int from = size-numberPointOnChart;
        if(from<1){
            from = 1;
        }
        log.error("FROM ["+from+"]");
        dayCompanyQuoteList = dayCompanyQuoteList.subList(from -1, size);
        double previousClose = 1850;
        SimpleDateFormat sdf = new SimpleDateFormat();
        GregorianCalendar now = new GregorianCalendar();
        for (int i = 0; i < dayCompanyQuoteList.size(); i++) {
            final XYChart.Data<String, Number> data = new XYChart.Data<>(dayCompanyQuoteList.get(i).getDateStr(), dayCompanyQuoteList.get(i).getCourse());
            data.setNode(
                    new HoveredThresholdNode(
                            
                            "", dayCompanyQuoteList.get(i).getDateStr() 
                                    + "\nPrice: " + dayCompanyQuoteList.get(i).getCourseStr()
                                    + "\nChange: " + dayCompanyQuoteList.get(i).getChangeStr()+"%"
                                    + "\nPrev: " + dayCompanyQuoteList.get(i).getReferenceStr()
                            + "\nLow: " + dayCompanyQuoteList.get(i).getLowStr()
                            + "\nHigh: " + dayCompanyQuoteList.get(i).getHighStr()
                            + "\nOpen: " + dayCompanyQuoteList.get(i).getOpenStr()
                            
                    )
            );

            dataset.add(data);
            i++;
        }

        return dataset;
    }

    class HoveredThresholdNode extends StackPane {

        HoveredThresholdNode(String priorValue, String value) {
            setPrefSize(6, 6);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                 
                    setCursor(Cursor.NONE);
                  toFront();
                  toFront();
                  
                  
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            }); 
        }

        private Label createDataThresholdLabel(String priorValue, String value) {
            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 10; -fx-font-weight: normal;");

//            if (priorValue == 0) {
//                label.setTextFill(Color.DARKGRAY);
//            } else if (value > priorValue) {
//                label.setTextFill(Color.FORESTGREEN);
//            } else {
//                label.setTextFill(Color.FIREBRICK);
//            }
          
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

}
