package org.szelc.app.antstock.loader.transactiondefined;

import org.szelc.app.antstock.collection.transactiondefined.TransactionDefinedContainer;
import org.szelc.app.antstock.data.TransactionType;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefinedActivity;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
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
 * @author szelc.org
 */
public class TransactionDefinedLoader {
    private Logger log = Logger.getLogger(TransactionDefinedLoader.class.toString());
    private static final String delimiter = ";";
    private static final String transactionPath = Settings.TRANSACTION_TO_REALIZE_FILE;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TransactionDefinedContainer loadTransactionsFromCSVFile(TransactionFilter filter) {
        TransactionDefinedContainer transactionDefinedContainer = new TransactionDefinedContainer();
        String company;
        String bank;
        TransactionType transactionType;
        Float priceToBuy;
        String dateEffectiveFrom;
        String createdTime;
        TransactionDefinedActivity definedActivity;
        try {

            CSVRecords records = CSVReader.getRecordsFromFile(transactionPath, delimiter);
            Iterator<CSVRecord> recIt = records.getRecords().iterator();
            while (recIt.hasNext()) {

                CSVRecord rec = recIt.next();
                List<String> f = rec.getFields();

                company = f.get(0);
                transactionType = TransactionType.valueOf(f.get(1));
                priceToBuy = Float.valueOf(f.get(2));
                dateEffectiveFrom = f.get(3);
                definedActivity = TransactionDefinedActivity.valueOf(f.get(4));
                createdTime = dateFormat.format(new Date());
                bank = f.get(6);

                TransactionDefined data = new TransactionDefined(company, transactionType, priceToBuy, dateEffectiveFrom, definedActivity, bank);
                transactionDefinedContainer.addTransactionData(data);
            }
            log.info("Transaction loaded from file [" + transactionPath + "]");
        } catch (FileNotFoundException ex) {
           log.error("File transaction not found ["+ex.getMessage()+"}");
           log.error(ex);
            System.exit(0);
        }
        return transactionDefinedContainer;
    }
}
