
package org.szelc.app.antstock.data.enumeration;

/**
 *
 * @author szelc.org
 */
public enum BankEnum {
    
    Alior, MBank, DB, RESPLIT, SPLIT, BOS_IKE, BOS_IKZE;
    
    @Override
    public String toString() {
        return name();
    }
}
