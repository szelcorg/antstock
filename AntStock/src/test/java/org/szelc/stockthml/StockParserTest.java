package org.szelc.stockthml;

import org.junit.Assert;
import org.junit.Test;
import org.szelc.stock.bean.StockDividens;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class StockParserTest {
    private static System.Logger LOGGER1 = System.getLogger("MyLogger");

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
        LOGGER1.log(System.Logger.Level.INFO, "display");
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