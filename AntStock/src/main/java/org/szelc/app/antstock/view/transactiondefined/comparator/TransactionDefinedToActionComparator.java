package org.szelc.app.antstock.view.transactiondefined.comparator;

import java.util.Comparator;
import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedToActionComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        float p1 = Math.abs(Float.valueOf(o1.toString().replace(",", ".")));
        float p2 = Math.abs(Float.valueOf(o2.toString().replace(",", ".")));
        if (p1 > p2) {
            return -1;
        } else if (p2 > p1) {
            return 1;
        }
        return 0;
    }
}