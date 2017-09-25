package org.szelc.app.antstock.util.stock;

import org.szelc.app.antstock.repository.EvaluateRepository;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.repository.TransactionRepository;
import org.szelc.app.antstock.collection.transactiondefined.TransactionDefinedContainer;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefinedActivity;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import org.szelc.app.antstock.persistence.transactiondefined.TransactionDefinedPersistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedManager {

    private Logger log = Logger.getLogger(TransactionDefinedManager.class.toString());

    private EvaluateRepository evaluateStockDataContainer;
    private TransactionRepository transactionDataContainer;
    private QuoteRepository quotesDataContainer;
    private TransactionDefinedContainer transactionDefinedContainer;

    public TransactionDefinedManager(EvaluateRepository evaluateStockDataContainer, TransactionRepository transactionDataContainer, QuoteRepository quotesDataContainer, TransactionDefinedContainer transactionDefinedContainer) {
        this.evaluateStockDataContainer = evaluateStockDataContainer;
        this.transactionDataContainer = transactionDataContainer;
        this.quotesDataContainer = quotesDataContainer;
        this.transactionDefinedContainer = transactionDefinedContainer;
    }

    public void addTransactionHandle(Transaction transaction) {

    }

    public List<TransactionDefined> calculateTransactionDefinedToDeleteFromEvaluate() {
        List<TransactionDefined> result = new ArrayList();
        List<Evaluate> evaluateList = evaluateStockDataContainer.getEvaluateStockDataList();
        String company;
        List<TransactionDefined> tddList = transactionDefinedContainer.getTransactionDefinedList();
        DayCompanyQuote lastQuote;
        float lastCourse;
        for (TransactionDefined tdd : tddList) {
            lastQuote = quotesDataContainer.getLastQuotes(tdd.getCompany());
            lastCourse = lastQuote.getCourse();
            if (tdd.getTransactionType() == TransactionType.K) {
                if (tdd.getPriceToAction() > lastCourse + (0.02f * lastCourse)) {
                    log.info("TransactionDefinedData to delete company [" + tdd.getCompany() + "] transactionType [" + tdd.getTransactionType() + "]");
                }
            } else if (tdd.getTransactionType() == TransactionType.S) {
                if (tdd.getPriceToAction() < lastCourse - (0.02f * lastCourse)) {
                    log.info("TransactionDefinedData to delete company [" + tdd.getCompany() + "] transactionType [" + tdd.getTransactionType() + "]");
                }
            }
        }

        return result;
    }

    public List<TransactionDefined> calculateTransactionDefinedToCreateFromEvaluate() {
        List<TransactionDefined> result = new ArrayList();
        List<String> companyList = evaluateStockDataContainer.getCompanies();
        if (companyList == null) {
            return null;
        }
        for (String company : companyList) {
            result.addAll(calculateCompanyTransactionDefinedToCreateFromEvaluate(company, TransactionType.K));
            result.addAll(calculateCompanyTransactionDefinedToCreateFromEvaluate(company, TransactionType.S));
        }
        return result;
    }

    public List<TransactionDefined> calculateCompanyTransactionDefinedToCreateFromEvaluate(String company) {
        List<TransactionDefined> result = new ArrayList();
        result.addAll(calculateCompanyTransactionDefinedToCreateFromEvaluate(company, TransactionType.K));
        result.addAll(calculateCompanyTransactionDefinedToCreateFromEvaluate(company, TransactionType.S));
        return result;
    }

    public List<TransactionDefined> calculateCompanyTransactionDefinedToCreateFromEvaluate(String company, TransactionType transactionType) {
        log.info("******************** Calculate defined  company [" + company + "] transactionType [" + transactionType + "]");
        List<TransactionDefined> result = new ArrayList();

        Evaluate evaluate = evaluateStockDataContainer.getEvaluateData(company);
        if (evaluate == null) {
            log.info("Doesn't exist evaluate for company [" + company + "]");
            return result;
        }

        DayCompanyQuote lastQuote = quotesDataContainer.getLastQuotes(company);
        float lastCloseCourse = lastQuote.getCourse();

        log.info("Close [" + lastCloseCourse + "]");

        if (transactionType == TransactionType.K) {
            log.info("Search BUY");
            float priceToBuy = evaluate.getRequiredPriceToBuy();
            if (priceToBuy == 0) {
                return result;
            }
            float lastLowCourse = lastQuote.getLow();
            float currentPriceToBuy = Math.min(priceToBuy, (lastLowCourse + (lastLowCourse * 0.02f)));
            log.info("toBuy [" + priceToBuy + "]  currentPriceToBuy [" + currentPriceToBuy + "] low [" + lastLowCourse + "]");
            TransactionDefined persistTDD = null;
            if (lastCloseCourse <= currentPriceToBuy) {
                log.info("Founded K TransactionDefined to create");
                if ((persistTDD = getTransactionDefined(company, transactionType)) == null) {
                    log.info("Must create TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "]"
                            + " price [" + currentPriceToBuy + "]");
                    String dateEffective = Settings.TRANSACTION_DEFINED_DATE_FORMAT_IN_FILE.format(new Date());
                    TransactionDefined tdd = new TransactionDefined(company, transactionType,
                            priceToBuy, dateEffective, TransactionDefinedActivity.LONG);
                    transactionDefinedContainer.addTransactionData(tdd);
                    result.add(tdd);
                } else {
                    log.info("Must update TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "] price [" + currentPriceToBuy + "]");
                }
            } else if ((persistTDD = getTransactionDefined(company, transactionType)) != null) {
                if (lastCloseCourse > currentPriceToBuy + (currentPriceToBuy * 0.03f)) {
                    log.info("Must delete TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "]");
                }
            }
        } else if (transactionType == TransactionType.S) {
            log.info("Search SELL");
            float priceToSell = evaluate.getRequiredPriceToSell();
            if (priceToSell == 0) {
                return result;
            }
            float lastHightCourse = lastQuote.getHigh();
            float currentPriceToSell = Math.max(priceToSell, (lastHightCourse - (lastHightCourse * 0.02f)));
            log.info("toSell [" + priceToSell + "]  currentPriceToSell [" + currentPriceToSell + "] low [" + lastHightCourse + "]");
            TransactionDefined persistTDD = null;
            if (lastCloseCourse >= currentPriceToSell) {
                log.info("Founded S TransactionDefined to create");

                if ((persistTDD = getTransactionDefined(company, transactionType)) == null) {
                    log.info("Must create TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "] price [" + currentPriceToSell + "]");
                    String dateEffective = Settings.TRANSACTION_DEFINED_DATE_FORMAT_IN_FILE.format(new Date());
                    TransactionDefined tdd = new TransactionDefined(company, transactionType, priceToSell, dateEffective,  TransactionDefinedActivity.LONG);
                    transactionDefinedContainer.addTransactionData(tdd);
                    result.add(tdd);
                } else {
                    log.info("Must update TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "] price [" + currentPriceToSell + "]");
                }
            } else if ((persistTDD = getTransactionDefined(company, transactionType)) != null) {
                if (lastCloseCourse < currentPriceToSell - (currentPriceToSell * 0.03f)) {
                    log.info("M"
                            + "ust delete TransactionDefinedData company [" + company + "] transactionType [" + transactionType + "]");
                }
            }
        }
        return result;
    }

    public void updateTransactionDefined(List<TransactionDefined> dataList) {
        TransactionDefinedPersistence persistence = new TransactionDefinedPersistence();
        persistence.updateTransactionFile(dataList);
    }

    private TransactionDefined getTransactionDefined(String company, TransactionType transactionType) {
        List<TransactionDefined> tddList = transactionDefinedContainer.getTransactionDefinedList();
        if (tddList == null) {
            return null;
        }
        for (TransactionDefined tdd : tddList) {
            if (tdd.getCompany().equals(company) && tdd.getTransactionType() == transactionType) {
                return tdd;
            }
        }
        return null;
    }
}
