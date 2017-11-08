package org.szelc.stock.reader;

import org.junit.Assert;
import org.junit.Test;
import org.szelc.stock.bean.StockDividens;
import org.szelc.stockthml.StockParser;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author by marcin.szelc on 2017-10-16.
 */
public class MBankOperationReaderTest {
    @Test
    public void readStockTransactionFromCsvFile() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void readStockDividendFromCsvFile() throws Exception {
        String fullPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\MBank\\TransakcjeGieldaFinansowe\\MBankFinansowa2010.csv";
        MBankOperationReader reader = new MBankOperationReader();
        reader.readStockDividendFromCsvFile(fullPath);
        Assert.assertTrue(true);
    }

    @Test
    public void areadStockDividendFromCsvFileRange() throws Exception {
        int from = 2010;
        int to = 2016;
        String chunkPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\MBank\\TransakcjeGieldaFinansowe\\MBankFinansowa";
        MBankOperationReader reader = new MBankOperationReader();
        StockDividens dividens = reader.readStockDividendsFromCsvFile(chunkPath, from, to);

        Map<String, String> codeCompanyMap = StockParser.getCompanyCodesMap();

        Assert.assertTrue(dividens.display(codeCompanyMap));

    }
}