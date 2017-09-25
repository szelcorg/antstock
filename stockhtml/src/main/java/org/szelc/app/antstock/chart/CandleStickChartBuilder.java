package org.szelc.app.antstock.chart;

import java.util.List;

public class CandleStickChartBuilder {

    private final String title;

    public CandleStickChartBuilder(String title) {
        this.title = title;
    }

    public CandleStickChart buildStock(final List<BarData> bars) throws Exception {
        CandleStickChart candleStickChart = new CandleStickChart(title, bars);
        candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
        return candleStickChart;
    }
}
