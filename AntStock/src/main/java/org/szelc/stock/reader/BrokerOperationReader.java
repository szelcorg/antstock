package org.szelc.stock.reader;

import org.szelc.app.antstock.statistic.StockTransactions;
import org.szelc.stock.bean.StockDividens;

import java.io.FileNotFoundException;

/**
 * @author by marcin.szelc on 2017-10-16.
 */
public abstract class BrokerOperationReader {

    public abstract StockTransactions readStockTransactionFromCsvFile(String fullPath);

    public abstract StockDividens readStockDividendsFromCsvFile(String path, int from, int to) throws FileNotFoundException;

    public abstract StockDividens readStockDividendFromCsvFile(String fullPath) throws FileNotFoundException;


}
