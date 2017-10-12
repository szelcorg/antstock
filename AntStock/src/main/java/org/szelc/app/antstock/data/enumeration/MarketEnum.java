package org.szelc.app.antstock.data.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by marcin.szelc on 2017-10-12.
 */
public enum MarketEnum {

    NOT_DEFINED, GPW, NC;

    public static final List<String> valuesString(){
        List<String> result = new ArrayList();
        MarketEnum [] en = values();
        for(MarketEnum  se: en){
            result.add(se.name());
        }
        return result;
    }
}
