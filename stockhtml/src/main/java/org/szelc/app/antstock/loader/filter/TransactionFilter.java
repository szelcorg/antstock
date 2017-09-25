package org.szelc.app.antstock.loader.filter;

import org.szelc.app.antstock.data.TransactionType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author szelc.org
 */
public class TransactionFilter {

    private static final Logger log = Logger.getLogger(TransactionFilter.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String companyName;

    public Date dateFrom;
    public Date dateTo;
    public TransactionType typeOfTransaction;

    public TransactionFilter() {

    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setDateFrom(String dateFrom) {
        try {
            this.dateFrom = dateFormat.parse(dateFrom);
        } catch (ParseException ex) {
            log.error(ex);
            System.exit(0);
        }
    }

    public void setDateTo(String dateTo) {
        try {
            this.dateTo = dateFormat.parse(dateTo);
        } catch (ParseException ex) {
            log.error(ex);
            System.exit(0);
        }
    }

    public void setTypeOfTransaction(TransactionType typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
    }

    public TransactionFilter(String companyName,
            Date dayFromOfTransaction,
            Date dayToOfTransaction,
            TransactionType typeOfTransaction) {
        if (companyName == null || companyName.isEmpty()) {
            this.companyName = null;
        } else {
            this.companyName = companyName;
        }
        this.dateFrom = dayFromOfTransaction;
        this.dateTo = dayToOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
    }

    public boolean filterCompanyName(String companyName) {
        if (this.companyName == null) {
            return true;
        }
        return this.companyName.equalsIgnoreCase(companyName);
    }

    public boolean filterDayFromOfTransaction(Date dayFromOfTransaction) {
        if (this.dateFrom == null) {
            return true;
        }
        return !(dayFromOfTransaction.before(this.dateFrom));
    }

    public boolean filterDayToOfTransaction(Date dayToOfTransaction) {
        if (this.dateTo == null) {
            return true;
        }
        return !(dayToOfTransaction.after(this.dateTo));
    }

    public boolean filterTypeOfTransaction(TransactionType typeOfTransaction) {
        if (this.typeOfTransaction == null) {
            return true;
        }
        return this.typeOfTransaction.equals(typeOfTransaction);
    }
}
