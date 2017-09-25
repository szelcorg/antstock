package org.szelc.app.antstock.view.transaction.comparator;

import org.szelc.app.antstock.data.Transaction;
import java.util.Comparator;

/**
 *
 * @author szelc.org
 */
public class TransactionDateComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        return ((Transaction)o1).getDay().compareTo(((Transaction)o2).getDay());
    }

}
