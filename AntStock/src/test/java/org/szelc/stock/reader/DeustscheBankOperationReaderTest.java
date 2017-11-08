package org.szelc.stock.reader;

import org.junit.Assert;
import org.junit.Test;
import org.szelc.stock.bean.StockDividens;
import org.szelc.stockthml.StockParser;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author by marcin.szelc on 2017-10-17.
 */
public class DeustscheBankOperationReaderTest {
    @Test
    public void readStockTransactionFromCsvFile() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void readStockDividendFromCsvFile() throws Exception {
        String fullPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\DB\\TransakcjeGieldaFinansowe\\DBFinansowa2013.csv";
        DeutscheBankOperationReader reader = new DeutscheBankOperationReader();
        reader.readStockDividendFromCsvFile(fullPath);
        Assert.assertTrue(true);
    }

    @Test
    public void areadStockDividendFromCsvFileRange() throws Exception {
        int from = 2013;
        int to = 2016;
        String chunkPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\DB\\TransakcjeGieldaFinansowe\\DBFinansowa";
        DeutscheBankOperationReader reader = new DeutscheBankOperationReader();
        StockDividens dividens = reader.readStockDividendsFromCsvFile(chunkPath, from, to);

        Map<String, String> codeCompanyMap = StockParser.getCompanyCodesMap();

        Assert.assertTrue(dividens.display(codeCompanyMap));

    }

}