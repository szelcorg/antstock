package org.szelc.stockthml;

import org.junit.Assert;
import org.junit.Test;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.financial.report.Report;
import org.szelc.financial.report.reader.ReportSqliteReader;
import org.szelc.financial.report.writer.ReportSqliteWriter;
import org.szelc.logger.LOG;
import org.szelc.sqlite.SQLiteJDBCDriverConnection;
import org.szelc.stock.bean.StockDividens;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StockParserTest {
    //private static System.Logger LOGGER1 = System.getLogger("MyLogger");
    private static String URL_GPW_QUOTES_BANKIER = "http://www.bankier.pl/gielda/notowania/akcje";
    @Test
    public void sqlite() {

        List<DayCompanyQuote> dcqList = StockParser.displayQuotesFromBankier(URL_GPW_QUOTES_BANKIER);
        List<String> companyList = new ArrayList<>();
        for(DayCompanyQuote dcq: dcqList){
            String company = dcq.getCompanyName();
            LOG.i(company);
            companyList.add(dcq.getCompanyName());



        }
        SQLiteJDBCDriverConnection.insertCompany(companyList);
        LOG.i("Count ["+dcqList.size()+"]");

        //SQLiteJDBCDriverConnection.connect();
      //  String dbname = "stock2.db";
        //SQLiteJDBCDriverConnection.createNewDatabase(dbname);
        //SQLiteJDBCDriverConnection.createTableCompany(dbname);

        /**
        try {
            SQLiteJDBCDriverConnection.select(dbname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
         */
    }


    private List<String> loadCompanies(){
        List<DayCompanyQuote> dcqList = StockParser.displayQuotesFromBankier(URL_GPW_QUOTES_BANKIER);
        List<String> companyList = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for(DayCompanyQuote dcq: dcqList) {
            String company = dcq.getCompanyName();
            result.add(company);
        }
        return result;
    }

    @Test
    public void displayFinancialReportLast4HForCompany(){
        Long companyId = 221l;
        ReportSqliteReader.report4QForCompany(companyId);

    }

    @Test
    public void displayReportFromBankier(){
        String kghm = "KGHM";
        List<String> companyList = new ArrayList();
        companyList.add(kghm);
        int countPage = 1;
        List<Report> reportList = StockParser.loadQuartalReportFromBankier(companyList, countPage);
        long t1 = System.currentTimeMillis();
        for(Report report : reportList){
            LOG.i(""+report);
        }

        LOG.i("Processing time ["+(System.currentTimeMillis() - t1)+"]");

        ReportSqliteWriter.writetToDB(reportList);

        //StockParser.displayHalfReportFromBankier(companyList, pageList);
        //StockParser.displayYearReportFromBankier(companyList, pageList);
    }


    @Test
    public void displayQuotesGpwFromBankier() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void displayQuotesNewConnectFromBankier() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void displayQuotesFromBankier() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void displayMultiplePageMesssageOnet() throws Exception {
        //LOGGER1.log(System.Logger.Level.INFO, "display");
        Assert.assertTrue(true);
        StockParser stockParser = new StockParser();
        boolean onlyToday = false;
        //type
        //0 - evaluate
        //1 - portfel
        //2 - transactioned
        List<String> companies = getCompanyList(0);
        Assert.assertFalse(companies.isEmpty());
/**
        List<String> result= stockParser.displayMultiplePageMesssageOnet(10, onlyToday,
                companies);
 */
    }

    @Test
    public void displayMessageOnet() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void displayDividendBossa() throws Exception {
        Assert.assertTrue(true);
    }


    //0 - evaluate
    //1 - portfel
    //2 - transactioned
    private static List getCompanyList(int type){
        String[] fileNames = new String[]{"companiesFromEvaluate", "companiesFromWallet", "companiesFromTransaction"};
        String fileName = "C:\\WORKSPACE\\AntStock3\\Storage\\cache\\companies\\"+fileNames[type]+".txt";
        try {
            return Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }

    }

    @Test
    public void getCompanyCodesMap(){
        StockParser.getCompanyCodesMap();
    }

}