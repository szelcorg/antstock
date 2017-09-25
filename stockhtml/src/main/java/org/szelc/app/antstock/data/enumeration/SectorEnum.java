package org.szelc.app.antstock.data.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szelc.org
 */
public enum SectorEnum {

    BANKI, BUDOWNICTWO, FINANSE, PRZEMYSŁ_LEKKI, DEVELOPERZY, ENERGETYKA, HANDEL_DETALICZNY, HANDEL_HURTOWY,
    INFORMATYKA, MEDIA, PRZEMYSŁ_CHEMICZNY, PRZEMYSŁ_DRZEWNY,
    PRZEMYSŁ_ELEKTROMASZ,PRZEMYSŁ_FARMACEUTYCZNY, PRZEMYSŁ_MAT_BUDOWL,PRZEMYSŁ_METALOWY,
    PRZEMYSŁ_MOTORYZACYJNY, PRZEMYSŁ_PALIWOWY, PRZEMYSŁ_SPOŻYWCZY, PRZEMYSŁ_SUROWCOWY, PRZEMYSŁ_TWORZYW_SZTUCZ,
    RYNEK_KAPITAŁOWY, TELEKOMUNIKACJA, USŁUGI_INNE, UBEZPIECZENIA, NOT_DEFINED;

   
    public static final List<String> valuesString(){
        List<String> result = new ArrayList();
       SectorEnum[] en = values();
       for(SectorEnum se: en){
           result.add(se.name());
       }
       return result;
    }
    
    @Override
    public String toString(){
        return name();
    }
}
