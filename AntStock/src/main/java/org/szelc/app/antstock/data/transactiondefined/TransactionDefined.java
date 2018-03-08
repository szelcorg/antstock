package org.szelc.app.antstock.data.transactiondefined;

import java.util.Date;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;

/**
 *
 * @author szelc.org
 */
public class TransactionDefined implements Cloneable {
    
    public static final float TO_ACTION_PERCENT_NOT_DEFINED = 1000;
    
    private String company;
    private TransactionType transactionType;
    private Float priceToAction;
    private String dateEffectiveFrom;
    private String createdTime;
    private String bank;
    //private TransactionDefinedData definedData;
    private TransactionDefinedActivity definedActivity;
    private float course = -1;
    private float toActionPercent = TO_ACTION_PERCENT_NOT_DEFINED;

     public TransactionDefined(String company, TransactionType transactionType){

         this(company, transactionType, 0f, Settings.TRANSACTION_DEFINED_DATE_FORMAT_IN_FILE.format(new Date()),  TransactionDefinedActivity.HAND, "");
     }
    public TransactionDefined(String company, TransactionType transactionType, Float priceToBuy, String dateEffectiveFrom, TransactionDefinedActivity definedActivity, String bank) {
        this.company = company;
        this.transactionType = transactionType;
        this.priceToAction = priceToBuy;
        this.dateEffectiveFrom = dateEffectiveFrom;
        this.createdTime = Settings.TRANSACTION_DEFINED_DATE_FORMAT_IN_FILE.format(new Date());
       // this.definedData = definedData;
        this.definedActivity = definedActivity;
        this.bank = bank;
    }


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Float getPriceToAction() {
        return priceToAction;
    }

    public String getPriceToActionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceToAction());
    }
      
    public void setPriceToAction(Float priceToBuy) {
        this.priceToAction = priceToBuy;
    }

    public String getDateEffectiveFrom() {
        return dateEffectiveFrom;
    }

    public void setDateEffectiveFrom(String dateEffectiveFrom) {
        this.dateEffectiveFrom = dateEffectiveFrom;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    
    public float getCourse() {
        if (course == -1f) {
            course = QuoteServiceFactory.instance().getQuoteService().getCourse(company);
            if (course == 0) {
                course = QuoteServiceFactory.instance().getQuoteService().getLastCourse(company);
            }
        }
        return course;
    }

    public String getCourseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getCourse());
    }

    public float getToActionPercent() {
        if (toActionPercent == TO_ACTION_PERCENT_NOT_DEFINED) {
            if (transactionType.equals(TransactionType.S)) {
                toActionPercent = 100 * ( priceToAction - getCourse() ) / getCourse();
            } else if (transactionType.equals(TransactionType.K)) {
                toActionPercent = 100 * ( priceToAction - getCourse()) / getCourse();
            } else {
                toActionPercent = 0;
            }
        }
        return toActionPercent;
    }

    public String getToActionPercentStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getToActionPercent());
    }

    public void setToActionPercent(float toActionPercent) {
        this.toActionPercent = toActionPercent;
    }
    
    

//    public TransactionDefinedData getDefinedData() {
//        return definedData;
//    }
//
//    public void setDefinedData(TransactionDefinedData definedData) {
//        this.definedData = definedData;
//    }

    public TransactionDefinedActivity getDefinedActivity() {
        return definedActivity;
    }

    public void setDefinedActivity(TransactionDefinedActivity definedActivity) {
        this.definedActivity = definedActivity;
    }

    public CSVRecord getCSVRecord(String separator) {
        CSVRecord rec = new CSVRecord();
        rec.addField(company);
        rec.addField(transactionType.toString());
        rec.addField(priceToAction);
        rec.addField(dateEffectiveFrom);
        rec.addField(definedActivity.toString());
        rec.addField(createdTime);
        rec.addField(bank);
        return rec;
//        StringBuilder sb = new StringBuilder();
//        sb.append(company);
//        sb.append(";");
//        sb.append(transactionType.toString());
//        sb.append(";");
//        sb.append(priceToBuy);
//        sb.append(";");
//        sb.append("2015.0.27");
//        sb.append(";");
//        sb.append(definedActivity.toString());
//        sb.append(";");
//        sb.append("2015.0.28");
//        sb.append(";");
//        
//        return sb.toString();
        
    }
    
    
    
    
}
