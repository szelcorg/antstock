package org.szelc.app.antstock.view.quotes;

import java.util.Date;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class QuotesStatistic {

    private Integer order;
    private String company;
    private Date dateFrom;
    private Date dateTo;
    private Integer numbers;
    private Integer up;
    //private Integer upPercent;
    private Integer down;
    //private Integer downPercent;
    private Integer equal;
    //private Integer equalPercent;
    private Float minClose;
    private Float maxClose;
    private Float firstClose;
    private Float lastClose;
    private Float maxToLastPercentSumMinToLastPercent;
    private Float currentClose;
    
    private float avgArithmeticClose;
    private Float avgMedianClose;

    public QuotesStatistic(String company, Date dateFrom, Date dateTo, int numbers,
            Integer up, Integer down, Integer equal, Float minClose, Float maxClose,
            Float firstClose, Float lastClose, Float currentClose) {
        this.company = company;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.numbers = numbers;
        this.up = up;
        this.down = down;
        this.equal = equal;
        this.minClose = minClose;
        this.maxClose = maxClose;
        this.firstClose = firstClose;
        this.lastClose = lastClose;
        this.currentClose = currentClose;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateFromStr() {
        return Settings.STATISTIC_VIEW_DATE_FORMAT.format(dateFrom);
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateToStr() {
        return Settings.STATISTIC_VIEW_DATE_FORMAT.format(dateTo);
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }
    
     public String getNumbersStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(numbers);
    }

    public Integer getUp() {
        return numbers > 0 ? up : 0 ;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public String getUpStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(getUp());
    }

    public Integer getDown() {
        return numbers > 0 ? down : 0;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public String getDownStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(getDown());
    }

    public Integer getEqual() {
        return numbers > 0 ? equal : 0;
    }

    public void setEqual(Integer equal) {
        this.equal = equal;
    }

    public String getEqualStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(getEqual());
    }

    public Float getMinClose() {
        return minClose;
    }

    public void setMinClose(Float minClose) {
        this.minClose = minClose;
    }

    public String getMinCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(minClose);
    }

    public Float getMaxClose() {
        return maxClose;
    }

    public void setMaxClose(Float maxClose) {
        this.maxClose = maxClose;
    }

    public String getMaxCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(maxClose);
    }

    public Float getMinToMaxPercent(){
        return minClose > 0 ? (100f * (maxClose - minClose)) / minClose : 0;
    }
    
       public String getMinToMaxPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMinToMaxPercent());
    }
    
    public Float getUpPercent() {
        return numbers > 0 ? 100f * up / numbers : 0;
    }

    public String getUpPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getUpPercent());
    }

    public Float getDownPercent() {
        return numbers > 0 ? 100f * down / numbers : 0;
    }

    public String getDownPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getDownPercent());
    }

    public Float getEqualPercent() {
        return numbers > 0 ? 100f * equal / numbers : 0;
    }

    public String getEqualPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getEqualPercent());
    }

    public Float getFirstClose() {
        return firstClose;
    }

    public String getFirstCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getFirstClose());
    }

    public Float getLastClose() {
        return lastClose;
    }

    public String getLastCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getLastClose());
    }
    
    public Float getMaxToLastPercent(){
        return maxClose > 0 ? (100f * (lastClose - maxClose)) / maxClose : 0;
    }
    
    public String getMaxToLastPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMaxToLastPercent());
    }
    
     public Float getMinToLastPercent(){
        return minClose > 0 ? (100f * (lastClose - minClose)) / minClose : 0;
    }
    
    public String getMinToLastPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMinToLastPercent());
    }
    
    
    public Float getMaxToLastPercentSumMinToLastPercent(){
        return getMinToLastPercent() + getMaxToLastPercent();
    }
    
    public String getMaxToLastPercentSumMinToLastPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMaxToLastPercentSumMinToLastPercent());
    }
    
    
    
     public Float getAvgToLastPercent(){
        return avgArithmeticClose > 0 ? (100f * (lastClose - avgArithmeticClose)) / avgArithmeticClose : 0;
    }
    
    public String getAvgToLastPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getAvgToLastPercent());
    }

    public Float getCurrentClose() {
        return currentClose;
    }

    public String getCurrentCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getCurrentClose());
    }
    
    public Float getFirstToCurrentPercent(){
        return firstClose > 0 ? (100f * (currentClose - firstClose)) / firstClose : 0;
    }
    
    public String getFirstToCurrentPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getFirstToCurrentPercent());
    }

    public float getAvgArithmeticClose() {
        return avgArithmeticClose;
    }

    public void setAvgArithmeticClose(float avgArithmeticClose) {
        this.avgArithmeticClose = avgArithmeticClose;
    }
    
    public String getAvgArithmeticCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getAvgArithmeticClose());
    }

    public Float getAvgMedianClose() {
        return avgMedianClose;
    }

    public void setAvgMedianClose(float avgMedianClose) {
        this.avgMedianClose = avgMedianClose;
    }

    public String getAvgMedianCloseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getAvgMedianClose());
    }
    
}
