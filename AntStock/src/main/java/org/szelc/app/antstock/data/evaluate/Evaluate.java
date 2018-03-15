package org.szelc.app.antstock.data.evaluate;

import java.util.List;

import org.szelc.app.antstock.data.enumeration.MarketEnum;
import org.szelc.app.antstock.data.enumeration.RatingEnum;
import org.szelc.app.antstock.data.enumeration.SectorEnum;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;

/**
 *
 * @author szelc.org
 */

public class Evaluate {

    private Integer order;
    private String companyName;
    private float requiredPriceToBuy;
    private float requiredPriceToSell;
    private float priceToBookValue;
    private float priceToEarning;
    private RatingEnum rating;
    private float Zscore;
    private float priceWhenEvaluatePEPBV;
    private float dividendInZL;
    private String dividendDay;
    private String dividendPaymentDay;
    private SectorEnum sector;
    private MarketEnum market;
    private float course = -1;
    private float changeCourse = -1000;
    private String dateNextUpdateBuySell;
    private String dateEarliestBuySell;
    private String dateLatestBuySell;
    private String dateChangeBuySell = "";
    private String dateChangePointer = "";

    private Float profitIn4Q;
    private Long numberOfShares;
    private Float bookValuePerShare;

 
    
    public Evaluate(String companyName, float requiredPriceToBuy, float requiredPriceToSell, float priceToBookValue,
            float priceToEarning, RatingEnum rating, float Zscore, float priceWhenEvaluatePEPBV, float dividendInZL,
            SectorEnum sector, MarketEnum market, String dateChangeBuySell, String dateChangePointer, String dateNextUpdateBuySell, String dateEarliestBuySell,
            String dateLatestBuySell, String dividendDay, String dividendPaymentDay ) {
        this.companyName = companyName;
        this.requiredPriceToBuy = requiredPriceToBuy;
        this.requiredPriceToSell = requiredPriceToSell;
        this.priceToBookValue = priceToBookValue;
        this.priceToEarning = priceToEarning;
        this.rating = rating;
        this.Zscore = Zscore;
        this.priceWhenEvaluatePEPBV = priceWhenEvaluatePEPBV;
        this.dividendInZL = dividendInZL;
        this.sector = sector;
        this.market = market;
        this.dateChangeBuySell = dateChangeBuySell;
        this.dateChangePointer = dateChangePointer;
        this.dateNextUpdateBuySell = dateNextUpdateBuySell;
        this.dateEarliestBuySell = dateEarliestBuySell;
        this.dateLatestBuySell = dateLatestBuySell;        
        this.dividendDay = dividendDay;
        this.dividendPaymentDay = dividendPaymentDay;
    }


    public Float getProfitIn4Q() {
        return profitIn4Q;
    }

    public void setProfitIn4Q(Float profitIn4Q) {
        this.profitIn4Q = profitIn4Q;
    }

    public Long getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Float getBookValuePerShare() {
        return bookValuePerShare;
    }

    public void setBookValuePerShare(Float bookValuePerShare) {
        this.bookValuePerShare = bookValuePerShare;
    }

    public Evaluate(String companyName){
        this(companyName, 0, 0, 0,
                0, RatingEnum.RatingNotDefined, 0, 0, 0, SectorEnum.NOT_DEFINED, MarketEnum.NOT_DEFINED, "", "", "", "", "", "", "");
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getRequiredPriceToBuy() {
        return requiredPriceToBuy;
    }

    public String getRequiredPriceToBuyStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(requiredPriceToBuy);
    }

    public void setRequiredPriceToBuy(float requiredPriceToBuy) {
        this.requiredPriceToBuy = requiredPriceToBuy;
    }

    public float getRequiredPriceToSell() {
        return requiredPriceToSell;
    }

    public String getRequiredPriceToSellStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(requiredPriceToSell);
    }

    public void setRequiredPriceToSell(float requiredPriceToSell) {
        this.requiredPriceToSell = requiredPriceToSell;
    }

    public RatingEnum getRating() {
        return rating;
    }

    public void setRating(RatingEnum rating) {
        this.rating = rating;
    }

    public float getZscore() {
        return Zscore;
    }

    public String getZscoreStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(Zscore);
    }

    public void setZscore(float Zscore) {
        this.Zscore = Zscore;
    }

    public float getPriceWhenEvaluatePEPBV() {
        return priceWhenEvaluatePEPBV;
    }

    public String getPriceWhenEvaluatePEPBVStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(priceWhenEvaluatePEPBV);
    }

    public void setPriceWhenEvaluatePEPBV(float priceWhenEvaluatePEPBV) {
        this.priceWhenEvaluatePEPBV = priceWhenEvaluatePEPBV;
    }

    public float getDividendInZL() {
        return dividendInZL;
    }

    public String getDividendInZLStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(dividendInZL);
    }
    
    public float getDividendPercent(){
        return 100f * getDividendInZL()/getCourse();
    }
    
    public String getDividendPercentStr(){
        return Settings.DECIMAL_FORMAT_FLOAT.format(getDividendPercent());
    }

    public void setDividendInZL(float dividendInZL) {
        this.dividendInZL = dividendInZL;
    }

    public String getDividendDay() {
        return dividendDay;
    }

    public void setDividendDay(String dividendDay) {
        this.dividendDay = dividendDay;
    }

    public String getDividendPaymentDay() {
        return dividendPaymentDay;
    }

    public void setDividendPaymentDay(String dividendPaymentDay) {
        this.dividendPaymentDay = dividendPaymentDay;
    }
    
    public float getPriceToBookValue() {
        return bookValuePerShare !=null ?  getCourse() / bookValuePerShare :
                priceWhenEvaluatePEPBV==0 ? 0 : priceToBookValue * getCourse() / priceWhenEvaluatePEPBV;
    }

    public String getPriceToBookValueStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceToBookValue());
    }

    public void setPriceToBookValue(float priceToBookValue) {
        this.priceToBookValue = priceToBookValue;
    }

    public float getPriceToEarning() {
        return bookValuePerShare!=null ? (getCourse() * (float)numberOfShares / profitIn4Q) :
            priceWhenEvaluatePEPBV == 0 ? 0 : priceToEarning * getCourse() / priceWhenEvaluatePEPBV;
    }

    public float getProfitPerShare(){
        return bookValuePerShare!=null ? profitIn4Q / numberOfShares :
                0f;
    }

    public String getProfitPerShareStr(){
        return Settings.DECIMAL_FORMAT_FLOAT.format(getProfitPerShare());
    }

    public String getPriceToEarningStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceToEarning());
    }

    public void setPriceToEarning(float priceToEarning) {
        this.priceToEarning = priceToEarning;
    }

    public SectorEnum getSector() {
        return sector;
    }

    public void setSector(SectorEnum sector) {
        this.sector = sector;
    }

    public MarketEnum getMarket() {
        return market;
    }

    public void setMarket(MarketEnum market) {
        this.market = market;
    }

    public float getCourse() {
        if(course== -1f || course == 0f){
            course =  QuoteServiceFactory.instance().getQuoteService().getCourse(companyName);        
            if(course==0){
                course = QuoteServiceFactory.instance().getQuoteService().getLastCourse(companyName);
            }
        }
        return course;
    }
    
    public String getCourseStr(){
        return Settings.DECIMAL_FORMAT_FLOAT.format(getCourse());
    }
    
    public float getChangeCourse() {
        if (changeCourse == -1000f) {
            List<DayCompanyQuote> dcqList = QuoteServiceFactory.instance().getQuoteService().getLastQuote(companyName, 2);
            if(dcqList==null){
                return 0;
            }
            float lastCourse = dcqList.get(dcqList.size() - 1).getCourse();
            if (dcqList.size() > 1) {
                float prevCourse = dcqList.get(dcqList.size() - 2).getCourse();
                this.changeCourse = 100 * (lastCourse - prevCourse) / prevCourse;

            } else {
                this.changeCourse = 0;
            }
        }
        return this.changeCourse;
    }
    
     public String getChangeCourseStr(){
        return Settings.DECIMAL_FORMAT_FLOAT.format(getChangeCourse());
    }

    public Float getPercentToBuy() {
        Float course = getCourse();
        if(requiredPriceToBuy==0){
            return -999f;
        }
        return (requiredPriceToBuy - course) * 100 / course;
    }

    public String getPercentToBuyStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentToBuy());
    }

    public Float getPercentToSell() {
        Float course = getCourse();
        if(requiredPriceToSell==0){
            return 999f;
        }
        return (requiredPriceToSell - course) * 100 / course;
    }

    public String getPercentToSellStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentToSell());
    }

    public String getDateChangeBuySell() {
        return dateChangeBuySell;
    }

    public void setDateChangeBuySell(String dateChangeBuySell) {
        this.dateChangeBuySell = dateChangeBuySell;
    }

    public String getDateChangePointer() {
        return dateChangePointer;
    }

    public void setDateChangePointer(String dateChangePointer) {
        this.dateChangePointer = dateChangePointer;
    }

    public String getDateNextUpdateBuySell() {
        return dateNextUpdateBuySell;
    }

    public void setDateNextUpdateBuySell(String dateNextUpdateBuySell) {
        this.dateNextUpdateBuySell = dateNextUpdateBuySell;
    }

    public String getDateEarliestBuySell() {
        return dateEarliestBuySell;
    }

    public void setDateEarliestBuySell(String dateEarliestBuySell) {
        this.dateEarliestBuySell = dateEarliestBuySell;
    }

    public String getDateLatestBuySell() {
        return dateLatestBuySell;
    }

    public void setDateLatestBuySell(String dateLatestBuySell) {
        this.dateLatestBuySell = dateLatestBuySell;
    }
   
    

    public CSVRecord getCSVRecord(String delimiter) {
        CSVRecord rec = new CSVRecord();

        rec.addField(companyName);
        rec.addField(requiredPriceToBuy);
        rec.addField(requiredPriceToSell);
        rec.addField(priceToBookValue);
        rec.addField(priceToEarning);
        rec.addField(String.valueOf(rating));
        rec.addField(Zscore);
        rec.addField(priceWhenEvaluatePEPBV);
        rec.addField(dividendInZL);
        rec.addField(sector.toString());
        rec.addField(dateChangeBuySell);
        rec.addField(dateChangePointer);
        rec.addField(dateNextUpdateBuySell);
        rec.addField(dateEarliestBuySell);
        rec.addField(dateLatestBuySell);
        rec.addField(dividendDay);
        rec.addField(dividendPaymentDay);
        rec.addField(market.toString());
        return rec;
    }

}
