package org.szelc.app.antstock.comparator;


/**
 *
 * @author szelc.org
 */
import java.util.Comparator;

/**
 *
 * @author szelc.org
 */
public class IntegerFormatComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        Integer f1 = Integer.valueOf(o1.toString().replace(",", "").replace(".", "").replace(" ", ""));
        Integer f2 = Integer.valueOf(o2.toString().replace(",", "").replace(".", "").replace(" ", ""));
        if (f2 > f1) {
            return -1;
        }
        if (f1 > f2) {
            return 1;
        }
        return 0;
    }
}
