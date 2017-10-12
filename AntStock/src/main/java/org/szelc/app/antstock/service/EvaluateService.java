package org.szelc.app.antstock.service;

import org.szelc.app.antstock.data.enumeration.MarketEnum;
import org.szelc.app.antstock.repository.EvaluateRepository;
import org.szelc.app.antstock.data.enumeration.RatingEnum;
import org.szelc.app.antstock.data.enumeration.SectorEnum;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.loader.filter.TransactionFilter;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.reader.CSVReader;

/**
 *
 * @author szelc.org
 */
public final class EvaluateService {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EvaluateService.class.toString());
    private EvaluateRepository evaluateRepository;
    private static EvaluateService instance;

    private EvaluateService() {
        long time1 = System.currentTimeMillis();
        boolean loadEvaluateFromCSVFile = loadEvaluateFromCSVFile(null);
        log.debug("TIME [loading evaluate from file] miliseconds [" + (System.currentTimeMillis() - time1 + "]"));
    }

    public static EvaluateService instance() {
        if (instance == null) {
            instance = new EvaluateService();
        }
        return instance;
    }

    public EvaluateRepository getEvaluateRepository() {
        return evaluateRepository;
    }

    private boolean loadEvaluateFromCSVFile(TransactionFilter filter) {
        evaluateRepository = new EvaluateRepository();
        CSVRecords records;
        try {
            records = CSVReader.getRecordsFromFile(Settings.EVALUATE_OWN_FILE_CSV, Settings.EVALUATE_FILE_CSV_DELIMITER);
        } catch (FileNotFoundException ex) {

            log.error(ex);
            System.exit(0);
            return false;
        }
        Iterator<CSVRecord> recIt = records.getRecords().iterator();
        while (recIt.hasNext()) {
            CSVRecord rec = recIt.next();
            List<String> f = rec.getFields();
            String companyName = f.get(0);
            float requiredPriceToBuy = Float.valueOf(f.get(1));
            float requiredPriceToSell = Float.valueOf(f.get(2));
            float pricetToBookValue = Float.valueOf(f.get(3));
            float priceToEarnings = Float.valueOf(f.get(4));
            RatingEnum rating = RatingEnum.fromName(f.get(5));
            float Zscore = Float.valueOf(f.get(6));
            float priceWhenEvaluatePEPBV = Float.valueOf(f.get(7));
            float dividendInZL = Float.valueOf(f.get(8));
            SectorEnum sector = SectorEnum.valueOf(f.get(9));
            String datePriceBuy = "";
            String datePointer = "";
            String dateNextUpdateBuySell = "";
            String dateEarliestBuySell = "";
            String dateLatestBuySell = "";
            String dividendDay = "";
            String dividendPaymentDay = "";
            MarketEnum market = MarketEnum.NOT_DEFINED;
            if (f.size() > 10) {
                datePriceBuy = f.get(10);
                if (f.size() > 11) {
                    datePointer = f.get(11);
                    if (f.size() > 12) {
                        dateNextUpdateBuySell = f.get(12);
                        if (f.size() > 13) {
                            dateEarliestBuySell = f.get(13);
                            if (f.size() > 14) {
                                dateLatestBuySell = f.get(14);
                                if (f.size() > 15) {
                                    dividendDay = f.get(15);
                                    if (f.size() > 16) {
                                        dividendPaymentDay = f.get(16);
                                        if(f.size()>17){
                                            market = MarketEnum.valueOf(f.get(17));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            evaluateRepository.addData(new Evaluate(companyName, requiredPriceToBuy, requiredPriceToSell, pricetToBookValue, priceToEarnings,
                    rating, Zscore, priceWhenEvaluatePEPBV, dividendInZL, sector, market, datePriceBuy, datePointer, dateNextUpdateBuySell, dateEarliestBuySell,
                    dateLatestBuySell, dividendDay, dividendPaymentDay));
        }
        return true;
    }

    public List<Evaluate> filter(List<Evaluate> dataList, TransactionFilter filter) {
        System.out.println("EvaluateDataList.size [" + dataList.size() + "] ");
        return dataList;
    }
}
