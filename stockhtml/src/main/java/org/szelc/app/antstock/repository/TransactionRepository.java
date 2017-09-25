package org.szelc.app.antstock.repository;

import java.text.ParseException;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class TransactionRepository {

    private List<Transaction> transactionDataList = new ArrayList();
    private Map<String, List<Transaction>> companyTransactionDataMap = new LinkedHashMap();
    private final int numberFields = 11;

    public TransactionRepository() {
    }

    public TransactionRepository(List<Transaction> tdList) {
        init(tdList);
    }

    private void init(List<Transaction> tdList) {
        if (tdList == null || tdList.isEmpty()) {
            return;
        }
        tdList.stream().forEach((td) -> {
            addTransactionData(td);
        });
    }

    public void addTransactionData(Transaction transactionData) {
        transactionDataList.add(transactionData);
        if (companyTransactionDataMap.containsKey(transactionData.getCompanyName())) {
            companyTransactionDataMap.get(transactionData.getCompanyName()).add(transactionData);
        } else {
            List<Transaction> list = new ArrayList();
            list.add(transactionData);
            companyTransactionDataMap.put(transactionData.getCompanyName(), list);
        }
    }

    public void removeTransactionData(Transaction transactionData) {
        transactionDataList.remove(transactionData);
        companyTransactionDataMap.get(transactionData.getCompanyName()).remove(transactionData);
    }

    public List<Transaction> getTransactionDataList() {
        return transactionDataList;
    }

    public int getNumberOfTransactions() {
        if (transactionDataList == null) {
            return 0;
        }
        return transactionDataList.size();
    }

    public List<Transaction> getReverseTransactionDataList() {
        Collections.reverse(transactionDataList);
        return getTransactionDataList();
    }

    public List<Transaction> getTransactionList(String company) {
        return companyTransactionDataMap.get(company);
    }

    public void setTransactionDataList(List<Transaction> transactionDataList) {
        this.transactionDataList = transactionDataList;
    }

    public Map<String, List<Transaction>> getCompanyTransactionDataMap() {
        return companyTransactionDataMap;
    }

    public void setCompanyTransactionDataMap(Map<String, List<Transaction>> companyTransactionDataMap) {
        this.companyTransactionDataMap = companyTransactionDataMap;
    }

    public int getNumberFields() {
        return numberFields;
    }

    public Set<String> getCompaniesWithTransaction() {
        return companyTransactionDataMap.keySet();
    }

    public Float getTotalMoneyProvision() {
        Float result = 0f;
        for (Transaction td : transactionDataList) {
            result += td.getMoneyProvision();
        }
        return result;
    }

    public Set<String> getAllCompanies() {
        return getTransactionDataList().stream().map(Transaction::getCompanyName).distinct().collect(Collectors.toSet());
    }

    private Date addDay(Date date, int field, int value) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(field, value);
        return (Date) cal.getTime().clone();
    }

    public List<Transaction> getTransactionDataList(String companyName, final String bankName, final Date dateFrom, final Date dateTo) {
        Date dateFrom1 = addDay(dateFrom, Calendar.DAY_OF_MONTH, -1);
        Date dateTo1 = addDay(dateTo, Calendar.DAY_OF_MONTH, 1);
        return companyTransactionDataMap.get(companyName).stream().filter(ct -> ct.getBankName().equals(BankEnum.valueOf(bankName))
                && ct.getDay().after(dateFrom1) && ct.getDay().before(dateTo1)).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionDataList(final String companyName, Date dateFrom, Date dateTo) {
        final Date dateFrom1 = addDay(dateFrom, Calendar.DAY_OF_MONTH, -1);
        final Date dateTo1 = addDay(dateTo, Calendar.DAY_OF_MONTH, 1);

        return companyTransactionDataMap.get(companyName).stream()
                .filter(ct -> ct.getDay().after(dateFrom1) && ct.getDay().before(dateTo1)).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionDataList(String company, String dateFrom, String dateTo) {
        try {
            return getTransactionDataList(company, Settings.TRANSACTION_DATE_FORMAT_IN_FILE.parse(dateFrom),
                    Settings.TRANSACTION_DATE_FORMAT_IN_FILE.parse(dateFrom));
        } catch (ParseException ex) {            
            System.exit(0);
            return new ArrayList();
        }
    }

    public List<Transaction> getTransactionDataList(final String companyName, final TransactionType transactionType,
            final Date dateFrom, final Date dateTo) {
        final Date dateFrom1 = addDay(dateFrom, Calendar.DAY_OF_MONTH, -1);
        final Date dateTo1 = addDay(dateTo, Calendar.DAY_OF_MONTH, 1);

        return companyTransactionDataMap.get(companyName).stream().filter(ct -> ct.getTransactionType().equals(transactionType)
                && ct.getDay().after(dateFrom1) && ct.getDay().before(dateTo1)).collect(Collectors.toList());
    }

    public int numberOfShares(String companyName) {
        int result = 0;
        Iterator<Transaction> it = companyTransactionDataMap.get(companyName).iterator();
        while (it.hasNext()) {
            Transaction trans = it.next();
            if (trans.getTransactionType().equals(TransactionType.K)) {
                result += trans.getNumberShares();
            } else {
                result -= trans.getNumberShares();
            }
        }

        return result;
    }

    public int getNumberOfShares(String companyName, String bankName) {
        int result = 0;
        Iterator<Transaction> it = companyTransactionDataMap.get(companyName).iterator();
        while (it.hasNext()) {
            Transaction trans = it.next();
            if (trans.getBankName().equals(BankEnum.valueOf(bankName))) {
                if (trans.getTransactionType().equals(TransactionType.K)) {
                    result += trans.getNumberShares();
                } else {
                    result -= trans.getNumberShares();
                }
            }
        }
        return result;
    }

}
