package org.szelc.app.antstock.service;

import org.szelc.app.antstock.repository.TransactionRepository;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.data.enumeration.BankEnum;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.reader.CSVReader;

/**
 *
 * @author mszelc
 */
public final class TransactionService {
    private static final Logger log = Logger.getLogger(TransactionService.class.toString());
    private TransactionRepository transactionRepository;
    private static TransactionService instance;
    
    private TransactionService(){
        transactionRepository = getTransactionRepository();
    }
    
    public static TransactionService instance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }
    
    public TransactionRepository getTransactionRepository(boolean reload) {
        if (reload || transactionRepository == null) {   
            long time1 = System.currentTimeMillis();
            loadTransactionsFromCSVFile(null);
            log.debug("TIME [loading transactions from file] miliseconds ["+(System.currentTimeMillis() - time1+"]"));
        }
        return transactionRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return getTransactionRepository(false);
    }

    public void addTransactionData(Transaction transactionData) {
        transactionRepository.addTransactionData(transactionData);
    }
    
    public void removeTransactionData(Transaction transactionData){
        transactionRepository.removeTransactionData(transactionData);
    }

    public TransactionRepository getTransactionRepository(TransactionFilter filter) {
        TransactionRepository result = new TransactionRepository();
        List<Transaction> transactionList = getTransactionList(filter);
        for (Transaction transaction : transactionList) {
            result.addTransactionData(transaction);
        }
        return result;
    }

    public List<Transaction> getTransactionList(TransactionFilter filter) {
        List result = new ArrayList();
        for (Transaction data : transactionRepository.getTransactionDataList()) {
            if (!filter.filterCompanyName(data.getCompanyName())) {
                continue;
            }
            if (!filter.filterDayFromOfTransaction(data.getDay())) {
                continue;
            }
            if (!filter.filterDayToOfTransaction(data.getDay())) {
                continue;
            }
            if (!filter.filterTypeOfTransaction(data.getTransactionType())) {
                continue;
            }
            result.add(data);
        }
        return result;
    }

    private boolean loadTransactionsFromCSVFile(TransactionFilter filter) {        
        log.info("LoadTransactionFromFile transactionFilePath ["+Settings.TRANSACTION_FILE_PATH+"] delimiter ["+Settings.TRANSACTION_FILE_CSV_DELIMITER+"]");
        transactionRepository = new TransactionRepository();
        Date dayOfTransaction;
        String companyName;
        TransactionType typeOfTransaction;
        int numberOfShares;

        try {
            CSVRecords records = CSVReader.getRecordsFromFile(Settings.TRANSACTION_FILE_PATH, Settings.TRANSACTION_FILE_CSV_DELIMITER);
            Iterator<CSVRecord> recIt = records.getRecords().iterator();
            while (recIt.hasNext()) {
                CSVRecord rec = recIt.next();
                List<String> f = rec.getFields();

                companyName = f.get(1);
                if (filter != null) {
                    if (!filter.filterCompanyName(companyName)) {
                        continue;
                    }
                    dayOfTransaction = Settings.TRANSACTION_DATE_FORMAT_IN_FILE.parse(f.get(0));
                    if (!filter.filterDayFromOfTransaction(dayOfTransaction)) {
                        continue;
                    }
                    if (!filter.filterDayToOfTransaction(dayOfTransaction)) {
                        continue;
                    }
                    typeOfTransaction = TransactionType.valueOf(f.get(2));
                    if (!filter.filterTypeOfTransaction(typeOfTransaction)) {
                        continue;
                    }
                } else {
                    dayOfTransaction = Settings.TRANSACTION_DATE_FORMAT_IN_FILE.parse(f.get(0));
                    typeOfTransaction = TransactionType.valueOf(f.get(2));
                }

                numberOfShares = Integer.valueOf(f.get(3));

                Transaction data = new Transaction(dayOfTransaction,
                        companyName, typeOfTransaction, numberOfShares,
                        Float.valueOf(f.get(4)),  BankEnum.valueOf(f.get(6)), Float.valueOf(f.get(7)),
                        Float.valueOf(f.get(8)));                
                transactionRepository.addTransactionData(data);
            }
            log.info("Transaction loaded from file [" + Settings.TRANSACTION_FILE_PATH + "]");
        } catch (FileNotFoundException | ParseException e) {
            log.error("Error when loading transactions from file ["+Settings.TRANSACTION_FILE_PATH+"] delimiter ["+Settings.TRANSACTION_FILE_CSV_DELIMITER+"]"
                    + "MESSAGE ["+e.getMessage()+"]");
            log.error(e);
            System.exit(0);
            return false;
        }
        return true;        
    }
}
