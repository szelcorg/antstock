package org.szelc.app.antstock.settings;

import java.io.File;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.szelc.app.antstock.comparator.FloatFormatComparator;
import org.szelc.app.antstock.comparator.IntegerFormatComparator;

/**
 *
 * @author szelc.org
 */
public class Settings {

    public static final String APP_HOME_PATH = Paths.get("").toAbsolutePath().toString() + File.separator;
    public static final String STORAGE_PATH = APP_HOME_PATH + "Storage" + File.separator;
    public static final String CACHE_PATH = STORAGE_PATH + "cache"+ File.separator;
    
    public static final IntegerFormatComparator COMPARATOR_INT_FORMAT= new IntegerFormatComparator();
    public static final FloatFormatComparator COMPARATOR_FLOAT_FORMAT= new FloatFormatComparator();
    
    public static final SimpleDateFormat TRANSACTION_DATE_FORMAT_IN_FILE = new SimpleDateFormat("yyyy-MM-dd");
    public static final String TRANSACTION_FILE_PATH = STORAGE_PATH + "Transaction.csv";
    public static final String TRANSACTION_FILE_CSV_DELIMITER = ";";
    
    public static final String TRANSACTION_TO_REALIZE_FILE = STORAGE_PATH + "Transaction_to_realize.csv";
      public static final String TRANSACTION_TO_REALIZE_FILE_COPY = STORAGE_PATH + "Transaction_to_realize_COPY.csv";
    
     public static final SimpleDateFormat TRANSACTION_DEFINED_DATE_FORMAT_IN_FILE = new SimpleDateFormat("yyyy-MM-dd");

    public static final String QUOTE_DOWNLOANDED_FILE_ZIP = STORAGE_PATH + "stockQuotes" + File.separator + "mstall.zip";
    public static final String QUOTE_FOR_DOWNLOAD_FILE_ZIP_URL = "http://bossa.pl/pub/metastock/mstock/mstall.zip";
    public static final String QUOTE_FOLDER_FOR_UNPACK_ZIP =  STORAGE_PATH + "stockQuotes" + File.separator + "mstall" + File.separator;

    public static final SimpleDateFormat QUOTE_TABLE_VIEW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat QUOTE_FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat TODAY_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat TODAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final String EVALUATE_OWN_FILE_CSV = STORAGE_PATH + "BUY_LONGTERM.csv";
    public static final String EVALUATE_FILE_CSV_DELIMITER = ";";
    public static final SimpleDateFormat EVALUATE_TABLE_VIEW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DecimalFormat DECIMAL_FORMAT_INTEGER = new DecimalFormat();
    public static final DecimalFormat DECIMAL_FORMAT_FLOAT = new DecimalFormat();
    public static final DecimalFormat DECIMAL_FORMAT_FLOAT_4F = new DecimalFormat();

    public static final String SESSION_FREE_DAYS_PATH_FILE_CSV = STORAGE_PATH + "sessionDaysFree.csv";
    public static final String QUOTE_LAST_DAY_SESSION_TEST_COMPANY= "TAURONPE";
    
    public static final SimpleDateFormat QUOTE_CHART_VIEW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String CHART_VIEW_TITLE_PRICE = "Candle, 2015";
    public static final String CHART_VIEW_TITLE_CANDLE = "Price, 2015";
        
    public static final String COMPANY_FILE_FROM_TRANSACTION = CACHE_PATH + "companies" + File.separator + "companiesFromTransaction.txt";
    public static final String COMPANY_FILE_FROM_EVALUATE = CACHE_PATH + "companies" + File.separator + "companiesFromEvaluate.txt";
    public static final String COMPANY_CIAGLE_BOSSA_URL = "http://bossa.pl/pub/metastock/sesjacgl.prn";
    
    public static final String STATISTIC_TABLE_VIEW_FXML = "/org/szelc/app/antstock/view/statistic/table/StatisticTableView.fxml";
    public static final SimpleDateFormat STATISTIC_VIEW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String STATISTIC_START_DATE_STR = "2015-01-01";

    static {
        DECIMAL_FORMAT_INTEGER.setGroupingSize(3);

        DECIMAL_FORMAT_FLOAT.setGroupingSize(3);
        DECIMAL_FORMAT_FLOAT.setMinimumFractionDigits(2);
        DECIMAL_FORMAT_FLOAT.setMaximumFractionDigits(2);

        DECIMAL_FORMAT_FLOAT_4F.setGroupingSize(3);
        DECIMAL_FORMAT_FLOAT_4F.setMinimumFractionDigits(4);
        DECIMAL_FORMAT_FLOAT_4F.setMaximumFractionDigits(4);

    }
}
