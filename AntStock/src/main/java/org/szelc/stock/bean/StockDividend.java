package org.szelc.stock.bean;

import java.time.LocalDate;
/**
 * @author by marcin.szelc on 2017-10-16.
 */
public class StockDividend {

    String company;
    LocalDate dividendDay;
    LocalDate dividenPayment;

    Float grossMoney;
    Float taxOfMoney;

    public StockDividend(String companyCode, Float grossMoney, Float taxOfMoney) {
        this.company = companyCode;
        this.grossMoney = grossMoney;
        this.taxOfMoney = taxOfMoney;
    }

    public StockDividend(String company, LocalDate dividendDay, LocalDate dividenPayment, Float grossMoney, Float taxOfMoney) {
        this.company = company;
        this.dividendDay = dividendDay;
        this.dividenPayment = dividenPayment;
        this.grossMoney = grossMoney;
        this.taxOfMoney = taxOfMoney;
    }



    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getDividendDay() {
        return dividendDay;
    }

    public void setDividendDay(LocalDate dividendDay) {
        this.dividendDay = dividendDay;
    }

    public LocalDate getDividenPayment() {
        return dividenPayment;
    }

    public void setDividenPayment(LocalDate dividenPayment) {
        this.dividenPayment = dividenPayment;
    }

    public Float getGrossMoney() {
        return grossMoney;
    }

    public void setGrossMoney(Float grossMoney) {
        this.grossMoney = grossMoney;
    }

    public Float getTaxOfMoney() {
        return taxOfMoney;
    }

    public void setTaxOfMoney(Float taxOfMoney) {
        this.taxOfMoney = taxOfMoney;
    }

    public Float getNet() {
        return grossMoney + taxOfMoney;
    }
}
