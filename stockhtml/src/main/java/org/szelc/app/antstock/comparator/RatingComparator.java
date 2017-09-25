package org.szelc.app.antstock.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.szelc.app.antstock.data.enumeration.RatingEnum;

/**
 *
 * @author szelc.org
 */
public class RatingComparator implements Comparator {

    private static final Map<String, Integer> map = new HashMap();

    static {
        map.put("AAA", 300);
        map.put("AAA-", 297);
        map.put("AA+", 295);
        map.put("AA", 290);
        map.put("AA-", 285);
        map.put("A+", 270);
        map.put("A", 265);
        map.put("A-", 260);
        map.put("BBB+", 255);
        map.put("BBB", 240);
        map.put("BBB-", 238);
        map.put("BB+", 235);
        map.put("BB", 230);
        map.put("BB-", 220);
        map.put("B+", 210);
        map.put("B", 200);
        map.put("B-", 190);
        map.put("CCC+", 185);
        map.put("CCC", 180);
        map.put("CCC-", 175);
        map.put("CC+", 170);
        map.put("CC", 160);
        map.put("CC-", 155);
        map.put("C+", 150);
        map.put("C", 140);
        map.put("C-", 130);
        map.put("DDD+", 125);
        map.put("DDD", 120);
        map.put("DDD-", 115);
        map.put("DD+", 110);
        map.put("DD", 100);
        map.put("DD-", 95);
        map.put("D+", 90);
        map.put("D", 80);
        map.put("D-", 70);
        map.put("XXX", 65);
        map.put("0", 60);
        map.put("ND", 50);
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            }
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        try {
            
            return map.get(((RatingEnum) o1).getName()) - map.get(((RatingEnum) o2).getName());
        } catch (Exception e) {
            e.printStackTrace();
           
            System.exit(0);
            return 0;
        }

    }
}
