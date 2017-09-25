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
public class FloatFormatComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
       if(o1.toString().equals("∞")){
           return 1;
       }
       else if(o2.toString().equals("∞")){
           return -1;
       }
        Float f1 = Float.valueOf(o1.toString().replace(",", "").replace(".", "").replace(" ", ""));
        Float f2 = Float.valueOf(o2.toString().replace(",", "").replace(".", "").replace(" ", ""));
        if (f2 > f1) {
            return -1;
        }
        if (f1 > f2) {
            return 1;
        }
        return 0;
    }
}
