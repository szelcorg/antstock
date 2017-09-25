package org.szelc.app.antstock.collection.transactiondefined;

import org.szelc.app.antstock.data.transactiondefined.TransactionDefined;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szelc.org
 */
public class TransactionDefinedContainer {
    
    private List<TransactionDefined> transactionDefinedList = new ArrayList();

    public void addTransactionData(TransactionDefined data) {
        transactionDefinedList.add(data);
    }

    public List<TransactionDefined> getTransactionDefinedList() {
        return transactionDefinedList;
    }

    
}
