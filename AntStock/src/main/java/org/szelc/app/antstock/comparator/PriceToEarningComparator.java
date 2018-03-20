package org.szelc.app.antstock.comparator;

import java.util.Comparator;

public class PriceToEarningComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

    Float f1 = Float.valueOf(o1.toString().replace(",", "").replace(".", "").replace(" ", ""));
    Float f2 = Float.valueOf(o2.toString().replace(",", "").replace(".", "").replace(" ", ""));
        if(f1<0 && f2 >0) {
            return -1;
        }
        if(f2<0 && f1 > 0 ){
            return 1;
        }

         if(f1>0 && f2>0){
            return (int)(100f * f2 - 100f * f1);
         }

         return (int)(100f * Math.abs(f1) - 100f * Math.abs(f2));


    }
}