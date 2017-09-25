package org.szelc.app.antstock.statistic;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.CompanyServiceFactory;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class CompanyTransactionStatisticSummary {

    private final Logger log = Logger.getLogger(CompanyTransactionStatisticSummary.class);
    private Float payActiveShare;
    private Float payNoActiveShare;
    private Float payProvisionActiveShares;
    private Float payProvisionNoActiveShares;
    private Float payProvisionBuyNoActiveShares;
    private Float payProvisionSellNoActiveShares;
    private Float courseActiveShares;
    private Float valueByCourseActiveShare;
    private Float courseNoActiveShares;
    private Float valueByCourseNoActiveShares;
    private Float selledNoActiveShares;
    private Float moneyProfitActiveShares;
    private Float evaluatePrivisionSellActiveShares;
    private Float totalActualPrice;
    private Float moneyProfitFullTransaction;
    private Float percentProfit;
    private Integer numberShares;

    private List<CompanyTransactionStatistic> ctsList;

    public CompanyTransactionStatisticSummary(List<CompanyTransactionStatistic> ctsList) {
        this.ctsList = ctsList;
    }

    private Set<String> getCompanies() {
        return CompanyServiceFactory.instance().getCompanyService().getCompanyTransactioned(true);
    }

    public float getTotalPayActiveShares() {
        return getPayActiveShares() + getPayProvisionActiveShares();
    }

    public String getTotalPayActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalPayActiveShares());
    }

    public float getTotalPayNoActiveShares() {
        return getPayNoActiveShares() + getPayProvisionNoActiveShares();
    }

    public String getTotalPayNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalPayNoActiveShares());
    }

    public float getTotalPayShares() {
        return getPayShares() + getPayBuyProvisionShares();
    }

    public String getTotalPaySharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalPayShares());
    }

    public float getTotalValueSellProvision() {
        return getPayProvisionSellNoActiveShares() + getEvaluatePrivisionSellActiveShares();
    }

    public String getTotalValueSellProvisionStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalValueSellProvision());
    }

    public float getPayActiveShares() {
        return getPayActiveShare(ctsList);
    }

    public String getPayActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayActiveShares());
    }

    private float getPayActiveShare(List<CompanyTransactionStatistic> ctsList) {
        if (payActiveShare == null) {
            payActiveShare = 0f;
            for (CompanyTransactionStatistic cts : ctsList) {
                payActiveShare += cts.getPriceForActiveShare();
            }
            //log.info("PAY ACTIVE SHARE ["+payActiveShare+"]");
        }
        return payActiveShare;
    }

    public float getPayNoActiveShares() {
        return getPayNoActiveShare(ctsList);
    }

    public String getPayNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayNoActiveShares());
    }

    private float getPayNoActiveShare(List<CompanyTransactionStatistic> ctsList) {

        if (payNoActiveShare == null) {
            payNoActiveShare = 0f;
            for (CompanyTransactionStatistic cts : ctsList) {
                payNoActiveShare += cts.getPriceForNoActiveShare();
            }
        }
        return payNoActiveShare;
    }

    public float getPayShares() {
        return getPayShare(ctsList);
    }

    public String getPaySharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayShares());
    }

    private float getPayShare(List<CompanyTransactionStatistic> ctsList) {
        return getPayNoActiveShare(ctsList) + getPayActiveShare(ctsList);
    }

    public float getPayProvisionActiveShares() {
        return getPayProvisionActiveShare(ctsList);
    }

    public String getPayProvisionActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayProvisionActiveShares());
    }

    private float getPayProvisionActiveShare(List<CompanyTransactionStatistic> ctsList) {
        if (payProvisionActiveShares == null) {

            payProvisionActiveShares = 0f;

            for (CompanyTransactionStatistic cts : ctsList) {
                payProvisionActiveShares += cts.getPayMoneyProvisionActiveShares();
            }
        }
        return payProvisionActiveShares;
    }

    public float getPayProvisionNoActiveShares() {
        return getPayProvisionNoActiveShares(ctsList);
    }

    public String getPayProvisionNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayProvisionNoActiveShares());
    }

    private float getPayProvisionNoActiveShares(List<CompanyTransactionStatistic> ctsList) {
        if (payProvisionNoActiveShares == null) {

            payProvisionNoActiveShares = 0f;

            for (CompanyTransactionStatistic cts : ctsList) {
                payProvisionNoActiveShares += cts.getPayMoneyProvisionNoActiveShares();
            }
        }
        return payProvisionNoActiveShares;
    }

    public float getPayProvisionBuyNoActiveShares() {
        return getPayProvisionBuyNoActiveShares(ctsList);
    }

    public String getPayProvisionBuyNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayProvisionBuyNoActiveShares());
    }

    private float getPayProvisionBuyNoActiveShares(List<CompanyTransactionStatistic> ctsList) {
        if (payProvisionBuyNoActiveShares == null) {

            payProvisionBuyNoActiveShares = 0f;

            for (CompanyTransactionStatistic cts : ctsList) {
                payProvisionBuyNoActiveShares += cts.getPayMoneyProvisionBuyNoActiveShares();
            }
        }
        return payProvisionBuyNoActiveShares;
    }

    public float getTotalPayBuyNoActiveShares() {
        return getPayNoActiveShares() + getPayProvisionBuyNoActiveShares();
    }

    public String getTotalPayBuyNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalPayBuyNoActiveShares());
    }

    public float getSelledMoneyBackNoActiveShares() {
        return getSelledNoActiveShares() - getPayProvisionSellNoActiveShares();
    }

    public String getSelledMoneyBackNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getSelledMoneyBackNoActiveShares());
    }

    public float getPayProvisionSellNoActiveShares() {
        return getPayProvisionSellNoActiveShares(ctsList);
    }

    public String getPayProvisionSellNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayProvisionSellNoActiveShares());
    }

    private float getPayProvisionSellNoActiveShares(List<CompanyTransactionStatistic> ctsList) {
        if (payProvisionSellNoActiveShares == null) {

            payProvisionSellNoActiveShares = 0f;

            for (CompanyTransactionStatistic cts : ctsList) {
                payProvisionSellNoActiveShares += cts.getPayMoneyProvisionSellNoActiveShares();
            }
        }
        return payProvisionSellNoActiveShares;
    }

    public float getPayProvisionShares() {
        return getPayProvisionShare(ctsList);
    }

    public String getPayProvisionSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayProvisionShares());
    }

    private float getPayProvisionShare(List<CompanyTransactionStatistic> ctsList) {
        return getPayProvisionNoActiveShares(ctsList) + getPayProvisionActiveShare(ctsList);
    }

    public float getPayBuyProvisionShares() {
        return getPayBuyProvisionShare(ctsList);
    }

    public String getPayBuyProvisionSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPayBuyProvisionShares());
    }

    private float getPayBuyProvisionShare(List<CompanyTransactionStatistic> ctsList) {
        return getPayProvisionBuyNoActiveShares() + getPayProvisionActiveShares();
    }

    public float getValueByCourseActiveShares() {
        return getValueByCourseActiveShare(getCompanies(), ctsList);
    }

    public String getValueByCourseActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getValueByCourseActiveShares());
    }

    private float getValueByCourseActiveShare(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (valueByCourseActiveShare == null) {
            valueByCourseActiveShare = 0f;
            for (CompanyTransactionStatistic cts : ctsList) {
                valueByCourseActiveShare += cts.getActualPrice() * cts.getNumberShares();
            }
        }
        return valueByCourseActiveShare;
    }

    public float getValueByCourseNoActiveShares() {
        return getValueByCourseNoActiveShare(getCompanies(), ctsList);
    }

    public String getValueByCourseNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getValueByCourseNoActiveShares());
    }

    private float getValueByCourseNoActiveShare(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (valueByCourseNoActiveShares == null) {

            valueByCourseNoActiveShares = 0f;
            for (CompanyTransactionStatistic cts : ctsList) {
                valueByCourseNoActiveShares += cts.getActualPrice() * cts.getNumberSharesFullTransaction();
            }
        }
        return valueByCourseNoActiveShares;
    }

    private float getSelledNoActiveShares(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (selledNoActiveShares == null) {

            selledNoActiveShares = 0f;
            for (CompanyTransactionStatistic cts : ctsList) {
                selledNoActiveShares += cts.getSellTotalFullTransaction();
            }
        }
        //return 100f;
        return selledNoActiveShares;
    }

    private float getSelledNoActiveShares() {
        return getSelledNoActiveShares(getCompanies(), ctsList);
    }

    public String getSelledNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getSelledNoActiveShares());
    }

    private float getTotalValueByCourseAndSelledShares() {
        return getSelledNoActiveShares() + getValueByCourseActiveShares();
    }

    public String getTotalValueByCourseAndSelledSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalValueByCourseAndSelledShares());
    }

    private float getTotalValueByCourseShares() {
        return getValueByCourseNoActiveShares() + getValueByCourseActiveShares();
    }

    public String getTotalValueByCourseSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalValueByCourseShares());
    }

    public float getProfitActiveShares() {
        return getEvaluateSellActiveShares() - getTotalPayActiveShares();
    }

    public String getProfitActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getProfitActiveShares());
    }

    public float getProfitNoActiveShares() {
        return getSelledMoneyBackNoActiveShares() - getTotalPayBuyNoActiveShares();
        //return getSelledNoActiveShares()- getTotalPayNoActiveShares();
    }

    public String getProfitNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getProfitNoActiveShares());
    }

    public float getProfitShares() {
        return getTotalSellMoneyBack() - getTotalPayShares();
    }

    public String getProfitSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getProfitShares());
    }

    public float getPercentProfitActiveShares() {
        return 100f * getProfitActiveShares() / getTotalPayActiveShares();
    }

    public String getPercentProfitActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentProfitActiveShares());
    }

    public float getPercentProfitNoActiveShares() {
        return 100f * getProfitNoActiveShares() / getTotalPayNoActiveShares();
    }

    public String getPercentProfitNoActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentProfitNoActiveShares());
    }

    public float getPercentProfitShares() {
        return 100f * getProfitShares() / getTotalPayShares();
    }

    public String getPercentProfitSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getPercentProfitShares());
    }

    private float getTotalActualPrice(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (totalActualPrice == null) {
            Iterator<String> it = companies.iterator();
            int rowIndex = 0;
            totalActualPrice = 0f;
            while (it.hasNext()) {
                if (rowIndex == ctsList.size()) {
                    return totalActualPrice;
                }
                CompanyTransactionStatistic statistic = ctsList.get(rowIndex++);
                if (statistic == null) {
                    continue;
                }
                totalActualPrice += statistic.getTotalActualPrice();
            }
        }
        return totalActualPrice;
    }

    private float getMoneyProfitFullTransaction(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (moneyProfitFullTransaction == null) {
            Iterator<String> it = companies.iterator();
            int rowIndex = 0;
            moneyProfitFullTransaction = 0f;
            while (it.hasNext()) {

                CompanyTransactionStatistic statistic = ctsList.get(rowIndex++);
                if (statistic == null) {
                    continue;
                }
                moneyProfitFullTransaction += statistic.getMoneyProfitFullTransaction();
            }

        }
        return moneyProfitFullTransaction;
    }

    public float getEvaluatePrivisionSellActiveShares() {
        return getEvaluatePrivisionSellActiveShares(getCompanies(), ctsList);
    }

    public String getEvaluatePrivisionSellActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getEvaluatePrivisionSellActiveShares());
    }

    public Float getEvaluatePrivisionSellActiveShares(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        if (evaluatePrivisionSellActiveShares == null) {

            evaluatePrivisionSellActiveShares = 0f;
            float provision = getPayProvisionActiveShares() / (getPayActiveShares());
            for (CompanyTransactionStatistic cts : ctsList) {

                evaluatePrivisionSellActiveShares += cts.getActualPrice() * cts.getNumberShares() * provision;
            }

        }
        return evaluatePrivisionSellActiveShares;
    }

    public float getEvaluateSellActiveShares() {
        return getEvaluateSellActiveShares(getCompanies(), ctsList);
    }

    public String getEvaluateSellActiveSharesStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getEvaluateSellActiveShares());
    }

    public Float getEvaluateSellActiveShares(Set<String> companies, List<CompanyTransactionStatistic> ctsList) {
        return getValueByCourseActiveShares() - getEvaluatePrivisionSellActiveShares();
    }

    public float getTotalSellMoneyBack() {

        return getTotalValueByCourseAndSelledShares() - getTotalValueSellProvision();
    }

    public String getTotalSellMoneyBackStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getTotalSellMoneyBack());
    }

    public String getTextClosedTransaction() {
        return "Closed";
    }

    public String getTextOpenTransaction() {
        return "Open";
    }

    public String getTextAllTransaction() {
        return "All";
    }
}
