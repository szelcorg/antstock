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
public class AliorOperationReader extends BrokerOperationReader{
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

    private String getCompanyCode2(String token){
        int index = token.indexOf(" PL");
        if(index==-1){
            index = token.indexOf("dywidendy")+9;
        }
        String company = token.substring(index+1).replace("\"", "")
                .replace(".", "")
                .replace(",", "");
        LOG.i("COMPANY ["+company+"]");
        if(company.contains(" ")){
            LOG.i("Trimuje ");
            company = company.substring(0, company.indexOf(" "));
        }
        return company;
    }


    private String getCompanyCode(String token){
        String[] tab = token.split(" ");
        LOG.i("TOKEN ["+token+"]");
        if(token.contains("CEZ")){
            return "CEZ";
        }
        if(token.contains("PZU")){
            return "PLPZU0000011";
        }
        token = tab[tab.length-1].replace("\"", "")
                .replace(".", "")
                .replace(",", "");

        if(token.contains("DP:")){
            token = tab[tab.length-2].replace("\"", "")
                    .replace(".", "")
                    .replace(",", "");
        }
        return token;
    }

    @Override
    public StockDividens readStockDividendFromCsvFile(String fullPath) throws FileNotFoundException {

        boolean isNextOperation = false;
        StockDividens result = new StockDividens();

        CSVRecords recs = getRecordsFromFile(fullPath, DEFAULT_CSV_SEPARATOR);

        Iterator<CSVRecord> it = recs.getRecords().iterator();
        while(it.hasNext()){
            CSVRecord recGross =  it.next();
            String recGrossStr = recGross.toString().replaceAll("\"","");
            LOG.i(recGrossStr);

            if(isNextOperation){

                if((recGross.toString().contains("dywidendy") || recGross.toString().contains("Dywidenda"))
                        && !recGross.toString().contains("dywidendy z pw emitenta zagranicznego")) {
                    LOG.i("Dywidenda tekst "+recGross.toString());
                    String recGrossTab[] = recGross.toString().split(";");
                    String companyCode = getCompanyCode(recGrossTab[2]);
                  //  LOG.i("Dywidenda company ["+recGrossTab[2]+"]");

                    String tax;
                    String gross;
                    if(!recGrossTab[2].contains("netto dywidendy") && !recGrossTab[2].contains("kwota netto")
                            && !recGrossTab[2].contains("ki CEZ.")
                            ) {
                        if(recGrossTab[2].contains("bonusa do dywidendy") ){
                            gross = recGrossTab[3];
                        }
                        else {
                            gross = recGrossTab[4];
                        }

                        CSVRecord recTax = it.next();
                        LOG.i("Tax tekst" + recTax.toString());

                        String recTaxTab[] = recTax.toString().split(";");
                        String companyCodeGross = getCompanyCode(recTaxTab[2]);
//                        LOG.i("Dywidenda companyTax [" + recTaxTab[2] + "]");

                        if (!companyCodeGross.equals(companyCode)) {
                            LOG.i("[" + companyCodeGross + "] [" + companyCode + "]");
                            throw new NullPointerException("Niezgodny company names");
                        }

                        tax = recTaxTab[3].toString();
                        if(tax.equals("\"")){
                            tax = recTaxTab[4].toString();
                        }
                    }
                    else {
                        gross=recGrossTab[3];
                        tax = "0";
                    }

                    if(Float.valueOf(gross.replace(",", "."))==937.87f){
                        LOG.i("*****");
                    }
                    result.add(new StockDividend(companyCode,
                            Float.valueOf(gross.replace(",", ".")),
                            Float.valueOf(tax.replace(",", "."))

                    ));
                }

                continue;
            }
            if(recGross.toString().contains("Typ operacji")){
                isNextOperation = true;
            }
        }

        return  result;
    }

}