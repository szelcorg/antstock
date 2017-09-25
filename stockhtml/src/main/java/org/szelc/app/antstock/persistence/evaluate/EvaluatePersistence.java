package org.szelc.app.antstock.persistence.evaluate;

import org.szelc.app.antstock.a.AntStock;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.persistence.AbstractStockPersistence;
import org.szelc.app.antstock.persistence.trasaction.TransactionPersistence;
import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.writer.CSVWriter;

/**
 *
 * @author szelc.org
 */
public class EvaluatePersistence extends AbstractStockPersistence{

    private static final Logger log = Logger.getLogger(TransactionFilter.class);
    
    public void update(List<Evaluate> dataList) {
        String fileInput = Settings.STORAGE_PATH+"BUY_LONGTERM.csv";
        String fileCopy = Settings.STORAGE_PATH+"BUY_LONGTERM_COPY.csv";
        File src = new File(fileInput);
        File dst = new File(fileCopy);
        CSVRecords records = new CSVRecords(separator);
        dataList.stream().forEach((data) -> {
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
}
