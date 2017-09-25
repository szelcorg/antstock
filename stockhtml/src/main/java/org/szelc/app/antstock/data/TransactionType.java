package org.szelc.app.antstock.data;

/**
 *
 * @author szelc.org
 */
public enum TransactionType {
    K, S;
    
    
    @Override    
    public String toString() {
        return name();
    }
}
