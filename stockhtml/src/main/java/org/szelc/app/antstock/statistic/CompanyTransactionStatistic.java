package org.szelc.app.antstock.statistic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.service.QuoteService;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.writer.CSVWriter;


/**
 *
 * @author ms
 */
public class CompanyTransactionStatistic {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private int order;
    
    private final String company;
    private final String dateFrom;
    private final String dateTo;
    private List<Transaction> stockTransactions; //for one company
    private Transactions transactionsActiveShares;
    private Transactions transactionsNoActiveShares;

    private int numberOfShares = 0;
    private float avgPriceForShare = 0f;
    private float priceForActiveShare = 0f;
    private float priceForNoActiveShare = 0f;
    private float actualPrice;
    private float totalActualPrice;
    private float percentProfit;
    private float moneyProfit;
    private int numberOfSharesFullTransaction = 0;
    private float avgPriceFullTransaction = 0f;
    private float priceTotalFullTransaction = 0f;
    private float sellTotalFullTransaction = 0f;
    private float payMoneyProvisionActiveShares = 0f;
    private float payMoneyProvisionNoActiveShares = 0f;
    private float payMoneyProvisionBuyNoActiveShares = 0f;
    private float payMoneyProvisionSellNoActiveShares = 0f;

    public static float totalCourseActiveShares;

    public CompanyTransactionStatistic(String company, String dateFrom, String dateTo) {
        this.company = company;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void build(List<Transaction> stockTransactions, QuoteRepository quoteRepository) {
        this.stockTransactions = stockTransactions;
        transactionsActiveShares = new StockTransactions();
        Iterator<Transaction> it = stockTransactions.iterator();
        while (it.hasNext()) {
            try {
                Transaction st = it.next();
                transactionsActiveShares.addTransaction((Transaction) st.clone());
            } catch (CloneNotSupportedException e) {
                log.error(e);
            System.exit(0);
            }
        }
        buildTransactionsActiveShares();

        transactionsNoActiveShares = new StockTransactions();
        it = stockTransactions.iterator();
        while (it.hasNext()) {
            try {
                Transaction st = it.next();
                transactionsNoActiveShares.addTransaction((Transaction) st.clone());
            } catch (CloneNotSupportedException e) {
                  log.error(e);
            System.exit(0);
            }
        }
        transactionsNoActiveShares = buildTransactionsNoActive();

    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    

    public synchronized int getNumberShares() {
        if (numberOfShares == 0) {
            Iterator<Transaction> it = transactionsActiveShares.getPurchaseTransactions().iterator();
            while (it.hasNext()) {
                Transaction transaction = it.next();
                numberOfShares += transaction.getNumberShares();
            }
        }
        return numberOfShares;
    }

    public String getNumberSharesStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(getNumberShares());
    }

    public float getAvgPriceForShare() {
        if (getNumberShares() == 0) {
            return 0f;
        }
        if (avgPriceForShare == 0) {
            avgPriceForShare = getPriceForActiveShare() / getNumberShares();
        }
        return avgPriceForShare;
    }

    public String getAvgPriceForShareStr() {
        return Settings.DECIMAL_FORMAT_FLOAT_4F.format(getAvgPriceForShare());

    }

    public synchronized float getPriceForActiveShare() {
        if (priceForActiveShare != 0) {
            return priceForActiveShare;
        }
        Iterator<Transaction> it = transactionsActiveShares.getPurchaseTransactions().iterator();
 
        while (it.hasNext()) {
            Transaction purchaseTrans = it.next();
            priceForActiveShare += purchaseTrans.getPriceOneShare() * purchaseTrans.getNumberShares();
        }
        return priceForActiveShare;
    }
    
    public synchronized float debugGetPriceForActiveShare() {
        if (priceForActiveShare != 0) {
            return priceForActiveShare;
        }
        Iterator<Transaction> it = transactionsActiveShares.getPurchaseTransactions().iterator();
        CSVWriter writer = new CSVWriter();
        CSVRecords records = new CSVRecords(";");
        while (it.hasNext()) {
            Transaction purchaseTrans = it.next();

            priceForActiveShare += purchaseTrans.getPriceOneShare() * purchaseTrans.getNumberShares();
            CSVRecord rec = new CSVRecord();
            rec.addField(priceForActiveShare);
            rec.addField(purchaseTrans.getCompany());
            rec.addField(purchaseTrans.getPriceOneShare());
            rec.addField(purchaseTrans.getNumberShares());
            records.addRecord(rec);
            //log.info("" +  priceForActiveShare +" PriceForActiveShare "+purchaseTrans.getCompany()+"; "+purchaseTrans.getPriceOneShare()+"; "+ purchaseTrans.getNumberShares() +"; ");
        }
        try {
            if(priceForActiveShare>0)
                writer.save("/home/mszelc/TransactionHigh/"+company+"---"+priceForActiveShare+"---.txt", records, true);
        } catch (UnsupportedEncodingException e) {
              log.error(e);
            System.exit(0);
        } catch (IOException e) {
              log.error(e);
            System.exit(0);
        }
        return priceForActiveShare;
    }

    public synchronized float getPriceForNoActiveShare() {
        if (priceForNoActiveShare != 0) {
            return priceForNoActiveShare;
        }
        Iterator<Transaction> it = transactionsNoActiveShares.getPurchaseTransactions().iterator();
        while (it.hasNext()) {
            Transaction purchaseTrans = it.next();

            priceForNoActiveShare += purchaseTrans.getPriceOneShare() * purchaseTrans.getNumberShares();
        }
        return priceForNoActiveShare;
    }

    public String getPriceForActiveShareStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceForActiveShare());
    }

    public synchronized float getActualPrice() {
        if (actualPrice == 0) {
            QuoteService quoteService = QuoteServiceFactory.instance().getQuoteService();
            List<DayCompanyQuote> list = quoteService.getQuoteRepository().getCompanyQuotesMap().get(company);
            try {
                actualPrice = list.get(list.size() - 1).getCourse();
            } catch (NullPointerException e) {
                actualPrice = quoteService.getLastCourse(company);
            }
        }
        return actualPrice;
    }

    public String getActualPriceStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getActualPrice());
    }

    public float getLastNotZeroPrice(String companyName) {
        List<DayCompanyQuote> list = QuoteService.instance().getQuoteRepository().getCompanyQuotesMap().get(companyName);
        return list.get(list.size() - 1).getCourse();
    }

    public String getLastNotZeroPriceStr(String companyName) {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getLastNotZeroPrice(companyName));
    }

    public float getMoneyProfit() {
        if (moneyProfit == 0) {
            moneyProfit = getTotalActualPrice() - getPriceForActiveShare();
        }
        return moneyProfit;
    }

    public String getMoneyProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMoneyProfit());
    }

    public float getPercentProfit() {
        if (getPriceForActiveShare() == 0) {
            return 0f;
        }
        if (percentProfit == 0) {
            percentProfit = 100 * (getTotalActualPrice() - getPriceForActiveShare()) / getPriceForActiveShare();
        }
        return percentProfit;
    }

    public String getPercentProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentProfit());
    }

    public float getTotalActualPrice() {
        if (totalActualPrice == 0) {
            totalActualPrice = getNumberShares() * getActualPrice();
        }
        return totalActualPrice;
    }

    public String getTotalActualPriceStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalActualPrice());
    }

    private void buildTransactionsActiveShares() {
        List<Transaction> sellTransactions = transactionsActiveShares.getSellTransactions();
        int numberOfSell = transactionsActiveShares.getNumberOfSellShares();
        List<Transaction> purchaseCompanyTransactions
                = transactionsActiveShares.getTransactions(company, TransactionType.K);
        Iterator<Transaction> itPur = purchaseCompanyTransactions.iterator();
        while (itPur.hasNext()) {
            Transaction purchaseTrans = itPur.next();
            if (purchaseTrans.getNumberShares() >= numberOfSell) {
                purchaseTrans.setNumberShares(purchaseTrans.getNumberShares() - numberOfSell);
                break;
            } else {
                numberOfSell -= purchaseTrans.getNumberShares();
                purchaseTrans.setNumberShares(0);
            }

        }

    }

    private Transactions buildTransactionsNoActive() {
        Transactions results = new StockTransactions(transactionsNoActiveShares.getSellTransactions());

        List<Transaction> purTransactions = transactionsNoActiveShares.getPurchaseTransactions();
        Iterator<Transaction> it = purTransactions.iterator();
        int numberSellShares = transactionsNoActiveShares.getNumberOfSellShares();
        while (it.hasNext()) {
            Transaction purTrans = it.next();
            if (purTrans.getNumberShares() >= numberSellShares) {
                purTrans.setNumberShares(numberSellShares);
                results.addTransaction(purTrans);
                break;
            } else {
                results.addTransaction(purTrans);
                numberSellShares = numberSellShares - purTrans.getNumberShares();
            }
        }
        return results;

    }

    public int getNumberSharesFullTransaction() {
        if (numberOfSharesFullTransaction != 0) {
            return numberOfSharesFullTransaction;
        }

        numberOfSharesFullTransaction = transactionsNoActiveShares.getNumberOfSellShares();

        return numberOfSharesFullTransaction;
    }

    public String getNumberSharesFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(getNumberSharesFullTransaction());
    }

    public float getAvgPriceFullTransaction() {
        if (getNumberSharesFullTransaction() == 0) {
            return 0f;
        }
        if (avgPriceFullTransaction == 0) {
            avgPriceFullTransaction = getPriceTotalFullTransaction() / getNumberSharesFullTransaction();
        }
        return avgPriceFullTransaction;
    }

    public String getAvgPriceFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getAvgPriceFullTransaction());
    }

    public float getAvgSellFullTransaction() {
        if (getNumberSharesFullTransaction() == 0) {
            return 0f;
        }
        return getSellTotalFullTransaction() / getNumberSharesFullTransaction();
    }

    public String getAvgSellFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getAvgSellFullTransaction());
    }

    public float getPriceTotalFullTransaction() {
        if (priceTotalFullTransaction != 0) {
            return priceTotalFullTransaction;
        }
        List<Transaction> payTrans = transactionsNoActiveShares.getPurchaseTransactions();
        Iterator<Transaction> it = payTrans.iterator();
        while (it.hasNext()) {

            Transaction tran = it.next();

            priceTotalFullTransaction += tran.getPriceOneShare() * tran.getNumberShares();

        }
        return priceTotalFullTransaction;
    }

    public String getPriceTotalFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPriceTotalFullTransaction());
    }

    public float getSellTotalFullTransaction() {
        if (sellTotalFullTransaction != 0) {
            return sellTotalFullTransaction;
        }
        List<Transaction> sellTrans = transactionsNoActiveShares.getSellTransactions();
        Iterator<Transaction> it = sellTrans.iterator();
        while (it.hasNext()) {
            Transaction tran = it.next();
            sellTotalFullTransaction += tran.getPriceOneShare() * tran.getNumberShares();
        }
        return sellTotalFullTransaction;
    }

    public String getSellTotalFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getSellTotalFullTransaction());
    }

    public float getPercentProfitFullTransaction() {
        if (getPriceTotalFullTransaction() == 0) {
            return 0f;
        }
        return 100 * (getSellTotalFullTransaction() - getPriceTotalFullTransaction()) / getPriceTotalFullTransaction();
    }

    public String getPercentProfitFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentProfitFullTransaction());
    }

    public float getMoneyProfitFullTransaction() {

        return getSellTotalFullTransaction() - getPriceTotalFullTransaction();
    }

    public String getMoneyProfitFullTransactionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMoneyProfitFullTransaction());
    }

    public float getPercentEvaluateProfit() {
        float pay = getPriceTotalFullTransaction() + getPriceForActiveShare();
        float sell = getSellTotalFullTransaction() + getTotalActualPrice();

        return 100 * (sell - pay) / pay;
    }
    
    public float  getPercentValueForTotal(){        
        return 100 * getTotalActualPrice()/totalCourseActiveShares;                
    }
    
     public String getPercentValueForTotalStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentValueForTotal());
    }

    public String getPercentEvaluateProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentEvaluateProfit());
    }

    public float getMoneyEvaluateProfit() {
        return getMoneyProfitFullTransaction() + getMoneyProfit();
    }

    public String getMoneyEvaluateProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getMoneyEvaluateProfit());
    }

    public String getCompany() {
        return company;
    }

    public Float getEvaluatePercentProfit() {
        float result;

        float profit = getMoneyProfit() + getMoneyProfitFullTransaction();
        float pay = getPriceForActiveShare() + (getNumberSharesFullTransaction() * getAvgPriceFullTransaction());
        result = 100 * (profit) / pay;

        return result;
    }

    public String getEvaluatePercentProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getEvaluatePercentProfit());
    }

    public Float getEvaluateMoneyProfit() {
        return getMoneyProfit() + getMoneyProfitFullTransaction();
    }

    public String getEvaluateMoneyProfitStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getEvaluateMoneyProfit());
    }

    public Float getPayMoneyProvisionActiveShares() {
        if (payMoneyProvisionActiveShares == 0) {
            for (Transaction t : transactionsActiveShares.getTransactions()) {
                if (t.getNumberShares() > 0 && t.isPurchase()) {
                    //log.info("TRANSACTION_MONEY_PROV "+t.toString());
                    payMoneyProvisionActiveShares += t.getMoneyProvision();
                }
            }
        }
        return payMoneyProvisionActiveShares;
    }

    public Float getPayMoneyProvisionNoActiveShares() {
        if (payMoneyProvisionNoActiveShares == 0) {
            for (Transaction t : transactionsNoActiveShares.getTransactions()) {
                payMoneyProvisionNoActiveShares += t.getMoneyProvision();
            }
            return payMoneyProvisionNoActiveShares;
        }
        return payMoneyProvisionNoActiveShares;
    }

    public Float getPayMoneyProvisionBuyNoActiveShares() {
        if (payMoneyProvisionBuyNoActiveShares == 0) {
            for (Transaction t : transactionsNoActiveShares.getTransactions()) {
                if (t.isPurchase()) {
                    payMoneyProvisionBuyNoActiveShares += t.getMoneyProvision();
                }
            }
        }
        return payMoneyProvisionBuyNoActiveShares;
    }

    public Float getPayMoneyProvisionSellNoActiveShares() {
        if (payMoneyProvisionSellNoActiveShares == 0) {

            for (Transaction t : transactionsNoActiveShares.getTransactions()) {
                if (t.isSell()) {
                    payMoneyProvisionSellNoActiveShares += t.getMoneyProvision();
                }
            }
        }
        return payMoneyProvisionSellNoActiveShares;
    }

}
