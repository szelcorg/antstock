
package org.szelc.app.antstock.data.transactiondefined;

/**
 *
 * @author mszelc
 */
public enum TransactionDefinedActivity {
    
    LONG, SHORT, HAND;
    
    @Override
    public String toString(){
        return name();
    }
}
