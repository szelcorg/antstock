package org.szelc.app.antstock.persistence.trasaction;

import org.szelc.app.antstock.persistence.AbstractStockPersistence;
import org.szelc.app.antstock.data.Transaction;
import java.io.BufferedReader;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.writer.CSVWriter;

/**
 *
 * @author szelc.org
 */
public class TransactionPersistence extends AbstractStockPersistence {

    private static final Logger log = Logger.getLogger(TransactionFilter.class);

    public void updateTransactionFile(List<Transaction> transactionList) {
        System.out.println("ZAPIS PLIUKU");

        String fileInput = Settings.STORAGE_PATH+"Transaction.csv";
        String fileCopy = Settings.STORAGE_PATH+"Transaction_COPY.csv";
        File src = new File(fileInput);
        File dst = new File(fileCopy);

        CSVRecords records = new CSVRecords(separator);
        transactionList.stream().forEach((data) -> {
            records.addRecord(data.getCSVRecord(separator));
        });
        CSVWriter writer = new CSVWriter();
        try {
            writer.save(fileCopy, records, true);
            System.out.println("New file saved");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            System.exit(0);
        } catch (IOException ex) {
            log.error(ex);
            System.exit(0);
        }

        try {
            copyFileUsingStream(dst, src);
        } catch (IOException ex) {
            log.error(ex);
            System.exit(0);
        }
    }

    public void convertTransactionFromTxtToCSV() throws FileNotFoundException,
            IOException {
        String fileInput = Settings.STORAGE_PATH+"Transaction.txt";
        String fileOutput = Settings.STORAGE_PATH+"Transaction.csv";

        String line;
        BufferedReader br = new BufferedReader(new FileReader(fileInput));

        CSVRecords records = new CSVRecords(separator);
        String data;
        SimpleDateFormat dfTxt = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dfCSV = new SimpleDateFormat("yyyy-MM-dd");
        while ((line = br.readLine()) != null) {
            CSVRecord rec = new CSVRecord();
            for (int i = 0; i < 10; i++) {

                if (i == 0) {
                    try {
                        data = dfCSV.format(dfTxt.parse(br.readLine()));
                    } catch (ParseException ex) {
                        data = "";
                        log.error(ex);
                        System.exit(0);
                    }
                } else {
                    data = br.readLine();
                }
                rec.addField(data);
            }
            records.addRecord(rec);
        }
        CSVWriter writer = new CSVWriter();
        try {
            records.reverse();
            writer.save(fileOutput, records, true);
            System.out.println("New file saved");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            System.exit(0);
        } catch (IOException ex) {
            log.error(ex);
            System.exit(0);
        }

    }
}
