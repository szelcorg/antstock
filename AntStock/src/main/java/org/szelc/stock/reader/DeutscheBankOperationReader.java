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

import static org.szelc.csv.reader.CSVReader.getRecordsFromFile;

/**
 * @author by marcin.szelc on 2017-10-17.
 */
public class DeutscheBankOperationReader extends BrokerOperationReader{
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
        if(token.contains("LU0327357389")){
            return "KERNEL";
        }
        if(token.contains("CZ0005112300")){
            return "CEZ";
        }

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

        //2014-11-18;Obciążenia;Pobranie podatku od dywidendy, PW: PLKGHM000017;;-21,00 PLN;1 842,54 PLN;
        //2014-11-18;Uznania;Wyp. dywidendy, PLKGHM000017; DP: 2014-07-08;;107,50 PLN;1 863,54 PLN;

        boolean isNextOperation = true;
        StockDividens result = new StockDividens();

        CSVRecords recs = getRecordsFromFile(fullPath, DEFAULT_CSV_SEPARATOR);

        Iterator<CSVRecord> it = recs.getRecords().iterator();
        while(it.hasNext()){
            CSVRecord recTax =  it.next();
            if(isNextOperation){
                if(recTax.toString().contains("Wyp. dywidendy") || recTax.toString().contains("ata dywidendy")){
                    CSVRecord recDividendGross  = recTax;
                    LOG.i("RECTAX ["+recTax+"]");

                    String recTabGross[] = recDividendGross.toString().split(";");
                    String companyCodeGross = getCompanyCode(recTabGross[2]);
                    Float grossMoney = Float.valueOf(recTabGross[5].replace(",", ".")
                            .replace(" PLN", "").replace(" ", ""));
                    if(companyCodeGross.contains("Wyp")){
                        LOG.i("RECT TA"+recTax);
                    }

                    result.add(new StockDividend(companyCodeGross, grossMoney,0f));
                }
                else if(recTax.toString().contains("dywidendy") ) {
                    String recTaxTab[] = recTax.toString().split(";");
                    String companyCode = getCompanyCode(recTaxTab[2]);

                    CSVRecord recDividendGross = it.next();
                    String recTabGross[] = recDividendGross.toString().split(";");
                    String companyCodeGross = getCompanyCode(recTabGross[2]);
                    LOG.i("company ["+recTabGross[2]+"] ["+recTaxTab[2]+"]");
                    LOG.i("c2 ["+companyCode+"] ["+companyCodeGross+"]");
                    if(!companyCodeGross.equals(companyCode)){
                        throw new NullPointerException("Niezgodny company names");
                    }

                    Float grossMoney = Float.valueOf(recTabGross[5].replace(",", ".")
                            .replace(" PLN", "").replace(" ", ""));

                    /**LOG.i("0 "+recTax.toString());
                     LOG.i("1 "+recTaxTab[0]);
                     LOG.i("2 "+recTaxTab[1]);
                     LOG.i("2 "+recTaxTab[2]);
                     */

                    String tax;
                    if(recTaxTab[4].toString().isEmpty()){
                        tax = recTaxTab[5].toString().replace(" PLN", "");//.isEmpty() ? recTaxTab[3] : recTaxTab[2];
                    }
                    else {
                        tax = recTaxTab[4].toString().replace(" PLN", "");//.isEmpty() ? recTaxTab[3] : recTaxTab[2];
                    }


                    //LOG.i("2 "+recTaxTab[3]);
                    result.add(new StockDividend(companyCode, grossMoney,
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
