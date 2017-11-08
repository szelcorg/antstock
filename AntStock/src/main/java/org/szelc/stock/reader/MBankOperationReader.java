package org.szelc.stock.reader;

import org.szelc.app.antstock.statistic.StockTransactions;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.logger.LOG;
import org.szelc.stock.bean.StockDividend;
import org.szelc.stock.bean.StockDividens;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.logging.Logger;

import static org.szelc.csv.reader.CSVReader.*;

/**
 * @author by marcin.szelc on 2017-10-16.
 */
public class MBankOperationReader extends BrokerOperationReader{
    private static final String DEFAULT_CSV_SEPARATOR = ";";
    private Logger log = Logger.getLogger("");
    @Override
    public StockTransactions readStockTransactionFromCsvFile(String fullPath) {
        return null;
    }

    @Override
    public StockDividens readStockDividendsFromCsvFile(String path, int from, int to) throws FileNotFoundException {
        StockDividens result = new StockDividens();
        for(int i=from; i<=to; i++){
            StockDividens dividens = readStockDividendFromCsvFile(path+i+".csv");
            result.add(dividens);
        }
        return result;
    }

    private String getCompanyCode(String token){
        int index = token.indexOf(" PL");
        String company = token.substring(index+1);
        if(company.contains(" ")){
            LOG.i("Trimuje ");
            company = company.substring(0, company.indexOf(" "));
        }
        return company;
    }

    @Override
    public StockDividens readStockDividendFromCsvFile(String fullPath) throws FileNotFoundException {

        boolean isNextOperation = false;
        StockDividens result = new StockDividens();

        CSVRecords recs = getRecordsFromFile(fullPath, DEFAULT_CSV_SEPARATOR);

        Iterator<CSVRecord> it = recs.getRecords().iterator();
        while(it.hasNext()){
            CSVRecord recTax =  it.next();
            if(isNextOperation){
                if(recTax.toString().contains("dywidendy")) {
                    String recTaxTab[] = recTax.toString().split(";");
                    String companyCode = getCompanyCode(recTaxTab[1]);

                    CSVRecord recDividendGross = it.next();
                    String recTabGross[] = recDividendGross.toString().split(";");
                    String companyCodeGross = getCompanyCode(recTabGross[1]);
                    if(!companyCodeGross.equals(companyCode)){
                        throw new NullPointerException("Niezgodny company names");
                    }

                    /**LOG.i("0 "+recTax.toString());
                     LOG.i("1 "+recTaxTab[0]);
                     LOG.i("2 "+recTaxTab[1]);
                     LOG.i("2 "+recTaxTab[2]);
                     */

                    String tax = recTaxTab[2].isEmpty() ? recTaxTab[3] : recTaxTab[2];
                    //LOG.i("2 "+recTaxTab[3]);
                    result.add(new StockDividend(companyCode, Float.valueOf(recTabGross[3].replace(",", ".")),
                            Float.valueOf(tax.replace(",", "."))));
                }

                continue;
            }
            if(recTax.toString().contains("Data operacji")){
                isNextOperation = true;
            }
        }

        return  result;
    }





}
