package org.szelc.app.antstock.data;

import java.math.BigDecimal;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import java.util.Date;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;

/**
 *
 * @author szelc.org
 */
public class Transaction implements Cloneable {

    private Integer order;
    private Date day;
    private String companyName;
    private TransactionType transactionType;
    private int numberShares;
    private float priceOneShare;
    private BankEnum bankName;
    private float percentProvision;
    private float moneyProvision;
    private float totalProvision;
    

    public Transaction(Date day, String companyName, TransactionType transactionType, int numberShares,
            float priceOneShare,  BankEnum bankName, float percentProvision, float moneyProvision) {
        this.day = day;
        this.companyName = companyName;
        this.transactionType = transactionType;
        this.numberShares = numberShares;
        this.priceOneShare = priceOneShare;
  
        this.bankName = bankName;
        this.percentProvision = percentProvision;
        this.moneyProvision = moneyProvision;

    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getDay() {
        return day;
    }

    public String getDayStr() {
        return Settings.TRANSACTION_DATE_FORMAT_IN_FILE.format(day);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompany() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getNumberShares() {
        return numberShares;
    }

    public String getNumberSharesStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(numberShares);
    }

    public void setNumberShares(Integer numberShares) {
        this.numberShares = numberShares;
    }

    public String getPriceOneShareStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(priceOneShare);
    }

    public float getPriceOneShare() {
        return priceOneShare;
    }

    public void setPriceOneShare(float priceOneShare) {
        this.priceOneShare = priceOneShare;
    }

    public float getPriceAllShares() {
        return priceOneShare * numberShares;
    }

    public String getPriceAllSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceAllShares());
    }

    public BankEnum getBankName() {
        return bankName;
    }

    public void setBankName(BankEnum bankName) {
        this.bankName = bankName;
    }

    public float getPercentProvision() {
        return percentProvision;
    }

    public String getPercentProvisionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(percentProvision);
    }

    public void setPercentProvision(float percentProvision) {
        this.percentProvision = percentProvision;
    }

    public float getMoneyProvision() {
        return moneyProvision;
    }

    public String getMoneyProvisionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(moneyProvision);
    }

    public void setMoneyProvision(float moneyProvision) {
        this.moneyProvision = moneyProvision;
    }

    public float getPriceTotal() {
        BigDecimal priceOneShareBD = new BigDecimal(String.valueOf(priceOneShare));
        BigDecimal numberSharesBD = new BigDecimal(String.valueOf(numberShares));
        BigDecimal moneyProvisionBD = new BigDecimal(String.valueOf(moneyProvision));
        if (transactionType.equals(TransactionType.K)) {
            return priceOneShareBD.multiply(numberSharesBD).add(moneyProvisionBD).floatValue();
        }
        return priceOneShareBD.multiply(numberSharesBD).subtract(moneyProvisionBD).floatValue();
    }

    public String getPriceTotalStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceTotal());
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isPurchase() {
        return transactionType.equals(TransactionType.K);
    }

    public boolean isSell() {
        return transactionType.equals(TransactionType.S);
    }

    public CSVRecord getCSVRecord(String delimiter) {
        CSVRecord rec = new CSVRecord();
        rec.addField(Settings.TRANSACTION_DATE_FORMAT_IN_FILE.format(day));
        rec.addField(companyName);
        rec.addField(transactionType.name());
        rec.addField(String.valueOf(numberShares));
        rec.addField(String.valueOf(priceOneShare));
        rec.addField(String.valueOf(getPriceAllShares()));
        rec.addField(bankName.name());
        rec.addField(String.valueOf(percentProvision));
        rec.addField(String.valueOf(moneyProvision));
        rec.addField(String.valueOf(getPriceTotal()));
        return rec;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Transaction{" + "day=" + day + ", companyName=" + companyName + ", transactionType=" + transactionType + ", numberShares=" + numberShares + ", priceOneShare=" + priceOneShare + ", bankName=" + bankName + ", percentProvision=" + percentProvision + ", moneyProvision=" + moneyProvision + '}';
    }

    
}
