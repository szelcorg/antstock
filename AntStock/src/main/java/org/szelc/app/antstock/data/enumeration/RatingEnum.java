package org.szelc.app.antstock.data.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szelc.org
 */
public enum RatingEnum {

    RatingAAAminus(1, "AAA"),
    RatingAAplus(2, "AA+"),
    RatingAA(3, "AA"),
    RatingAAminus(4, "AA-"),
    RatingAplus(5, "A+"),
    RatingA(5, "A"),
     RatingAminus(5, "A-"),
    RatingBBBplus(6, "BBB+"),
    RatingBBB(7, "BBB"),
    RatingBBBminus(8, "BBB-"),
    RatingBBplus(9, "BB+"),
    RatingBB(10, "BB"),
    RatingBBminus(11, "BB-"),
    RatingBplus(12, "B+"),
    RatingB(13, "B"),
    RatingBminus(14, "B-"),
    RatingCCCplus(15, "CCC+"),
    RatingCCC(16, "CCC"),
    RatingCCCminus(17, "CCC-"),
    RatingCCplus(18, "CC+"),
    RatingCC(19, "CC"),
    RatingCCminus(20, "CC-"),
    RatingCplus(21, "C+"),
    RatingC(22, "C"),
    RatingCminus(23, "C-"),
    RatingDDDplus(24, "DDD+"),
    RatingDDD(25, "DDD"),
    RatingDDDminus(26, "DDD-"),
    RatingDDplus(27, "DD+"),
    RatingDD(28, "DD"),
    RatingDDminus(29, "DD-"),
    RatingDplus(30, "D+"),
    RatingD(31, "D"),
    RatingDminus(32, "D-"),
    RatingNotDefined(100, "ND"),
    RatingXXX(100, "XXX");
    private static Exception NullPointerException(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private final Integer number;
    private final String name;

    private RatingEnum(int number, String name) {
        this.number = number;
        this.name = name;
    }
    
    public static final List<String> valuesString(){
        List<String> result = new ArrayList();
       RatingEnum[] en = values();
       for(RatingEnum se: en){
           result.add(se.getName());
       }
       return result;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public static RatingEnum fromName(String ratingName) {
        for (RatingEnum value : values()) {
            if (value.getName().equals(ratingName)) {
                return value;
            }
        }
        //throw new NullPointerException("");
        return null;
    }

}
