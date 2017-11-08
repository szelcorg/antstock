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
public class AliorOperationReaderTest {
    @Test
    public void readStockTransactionFromCsvFile() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void readStockDividendFromCsvFile() throws Exception {
        String fullPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\Alior\\TransakcjeGieldaFinansowe\\AkcjeAliorFinansowe2011.csv";
        AliorOperationReader reader = new AliorOperationReader();
        reader.readStockDividendFromCsvFile(fullPath);
        Assert.assertTrue(true);
    }

    @Test
    public void areadStockDividendFromCsvFileRange() throws Exception {
        int from = 2011;
        int to = 2016;
        String chunkPath = "C:\\github\\antstock\\AntStock\\Storage\\StockOperation\\Alior\\TransakcjeGieldaFinansowe\\AkcjeAliorFinansowe";
        AliorOperationReader reader = new AliorOperationReader();
        StockDividens dividens = reader.readStockDividendsFromCsvFile(chunkPath, from, to);

        Map<String, String> codeCompanyMap = StockParser.getCompanyCodesMap();

        Assert.assertTrue(dividens.display(codeCompanyMap));

    }

}