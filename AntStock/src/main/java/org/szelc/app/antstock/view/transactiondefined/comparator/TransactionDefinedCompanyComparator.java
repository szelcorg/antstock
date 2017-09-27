package org.szelc.app.antstock.view.transactiondefined.comparator;

import java.util.Comparator;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedCompanyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        return ((TransactionDefined)o1).getCompany().compareTo(((TransactionDefined)o1).getCompany());
    }
}
