
package org.szelc.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.szelc.app.antstock.statistic.CompanyTransactionStatistic;
import org.szelc.app.utill.csv.model.CSVRecords;

import org.szelc.app.util.csv.writer.CSVWriter;
import org.szelc.app.utill.csv.model.CSVRecords;
import org.szelc.app.utill.csv.model.CSVRecord;
/**
 *
 * @author marcin.szelc
 */
public class CsvUtil {
    
    public static void printToFile(String filename,  List<CompanyTransactionStatistic> ctsList){        
        CSVWriter writer = new CSVWriter();
        CSVRecords recs = new CSVRecords(";");
        for(CompanyTransactionStatistic cts : ctsList ){
            if(cts.getNumberShares()==0){
                continue;
            }
            CSVRecord rec = new CSVRecord();
            rec.addField(cts.getCompany());
            rec.addField(cts.getNumberShares());            
            recs.addRecord(rec);
        }
        try {
            writer.save(filename, recs, true);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CsvUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            
        }
    }
}
