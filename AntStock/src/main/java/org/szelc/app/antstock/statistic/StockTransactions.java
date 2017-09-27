package org.szelc.app.antstock.statistic;


import java.util.*;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.swl.session.util.DateUtil;

/**
 *
 * @author ms
 */
public class StockTransactions implements Transactions {

    private List<Transaction> transactions = new LinkedList();
    private final String dateFormat = "dd-MM-yy";
    
    private final Map<String, Integer> companyOpenTransaction = new LinkedHashMap();

    public StockTransactions() {
    }

    public StockTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void addRevertTransaction(Transaction transaction) {
        transactions.add(0, transaction);
        Integer openTransaction = companyOpenTransaction.get(transaction.getCompany());

        if (openTransaction == null) {
            openTransaction = 0;
        }
        if (transaction.isPurchase()) {
            companyOpenTransaction.put(transaction.getCompany(), openTransaction + transaction.getNumberShares());
        } else if (transaction.isSell()) {
            companyOpenTransaction.put(transaction.getCompany(), openTransaction - transaction.getNumberShares());
        }

    }

//    @Override
//    public Companies getCompaniesWithOpenTransactions() {
//        Companies companies = new StockCompanies();
//        Iterator<String> it = companyOpenTransaction.keySet().iterator();
//        int counter = 0;
//        while (it.hasNext()) {
//            String company = it.next();
//            if (companyOpenTransaction.get(company) > 0) {
//                //log.info("------------>0  Company [" + company.getName() + "] numberShares [" + companyOpenTransaction.get(company) + "]");
//                counter++;
//                companies.addCompany(company);
//            } else {
//                //  log.info("Company <0 ["+company.getName()+"] numberShares ["+companyOpenTransaction.get(company)+"]");
//            }
//        }
//        //log.info("COUNTER ["+counter+"]");
//        return companies;
//    }
//
//    @Override
//    public Companies getCompaniesWithOpenOrCloseTransaction() {
//        Companies companies = new StockCompanies();
//        Iterator<String> it = companyOpenTransaction.keySet().iterator();
//        int counter = 0;
//        while (it.hasNext()) {
//            String company = it.next();
//            companies.addCompany(company);
//        }
//        //log.info("COUNTER ["+counter+"]");
//        return companies;
//    }

    @Override
    public void addTransaction(Transaction transaction) {
        //log.info("ADdTrans ["+transaction.getDate()+"]");
        transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions(){
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactions(String dateFrom, String dateTo) {
        List<Transaction> results = new LinkedList<Transaction>();
        Iterator<Transaction> it = transactions.iterator();
        //log.info("*******GetTransactions ["+transactions.size()+"]");
        while (it.hasNext()) {
            Transaction transaction = it.next();
            DateUtil.RESULT res1 = DateUtil.compareDate(dateFrom, transaction.getDayStr(), dateFormat);
            DateUtil.RESULT res2 = DateUtil.compareDate(dateTo, transaction.getDayStr(), dateFormat);
            if ((res1.equals(DateUtil.RESULT.FIRST_DATE_BEFORE)
                    || res1.equals(DateUtil.RESULT.DATE_EQUAL))
                    && (res2.equals(DateUtil.RESULT.FIRST_DATE_AFTER)
                    || res2.equals(DateUtil.RESULT.DATE_EQUAL))) {
                results.add(transaction);
            }
        }
        return results;
    }

    @Override
    public List<Transaction> getTransactions(String company) {
        List<Transaction> result = new LinkedList<Transaction>();
        Iterator<Transaction> it = transactions.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();
            if (transaction.getCompany().equalsIgnoreCase(company)) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> getTransactions(String company, TransactionType type) {
        List<Transaction> results = new LinkedList<Transaction>();
        Iterator<Transaction> it = transactions.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();
            if (transaction.getCompany().equalsIgnoreCase(company) && transaction.getTransactionType().equals(type)) {
                results.add(transaction);
            }
        }
        return results;
    }

    @Override
    public List<Transaction> getTransactions(String company, String dateFrom, String dateTo) {
        //log.info("Loading trans company ["+company+"] dateFrom ["+dateFrom+"] dateTo ["+dateTo+"]");
        List<Transaction> results = new LinkedList<Transaction>();
        List<Transaction> st = getAllTransactions();
        if (st == null || st.isEmpty()) {
            return null;
        }
        Iterator<Transaction> it = st.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();
            //log.info("Compare id ["+transaction.getCompanyId()+"] id2 ]"+company.getId()+"]");
            if (transaction.getCompany().equalsIgnoreCase(company)){
                results.add(transaction);
            }
        }
        return results;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public List<Transaction> getSellTransactions() {
        List<Transaction> results = new LinkedList<Transaction>();
        Iterator<Transaction> it = transactions.iterator();
        while (it.hasNext()) {
            Transaction trans = it.next();
            if (trans.isSell()) {
                results.add(trans);
            }
        }
        return results;
    }

    @Override
    public int getNumberOfSellShares() {
        List<Transaction> ts = getSellTransactions();
        Iterator<Transaction> it = ts.iterator();
        int result = 0;
        while (it.hasNext()) {

            result += it.next().getNumberShares();
            //log.debug("RESULT = "+result);
        }
        return result;
    }

    @Override
    public List<Transaction> getPurchaseTransactions() {
        List<Transaction> results = new LinkedList<>();
        Iterator<Transaction> it = transactions.iterator();
        while (it.hasNext()) {
            Transaction trans = it.next();
            if (trans.isPurchase()) {
                results.add(trans);
            }
        }
        return results;
    }

    @Override
    public Float getTotalMoneyProwizja() {
        Iterator<Transaction> it = transactions.iterator();
        Float result = 0f;
        while (it.hasNext()) {
            Transaction trans = it.next();
            result += trans.getMoneyProvision();
        }
        return result;
    }

    @Override
    public Float getTotalMoneyValue() {
        Iterator<Transaction> it = transactions.iterator();
        Float result = 0f;
        while (it.hasNext()) {
            Transaction trans = it.next();
            result += trans.getPriceTotal();
        }
        return result;
    }

    @Override
    public Integer getNumberTransactions() {
        if (transactions == null) {
            return 0;
        }
        return transactions.size();
    }
    
    @Override
    public Integer getNumberShares(String company, String dateFrom, String dateTo){
        List<Transaction> trans = getTransactions(company, dateFrom, dateTo);
        int result=0;
        for(Transaction t: trans){
            if(t.isPurchase()){
                result+=t.getNumberShares();
            }
            else if(t.isSell()){
                result-=t.getNumberShares();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Transaction> it = transactions.iterator();
        while (it.hasNext()) {
            sb = sb.append(it.next().toString()).append("\n");
        }
        return sb.toString();
    }

  
}