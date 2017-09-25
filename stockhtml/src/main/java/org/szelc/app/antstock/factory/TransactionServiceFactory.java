package org.szelc.app.antstock.factory;

import org.szelc.app.antstock.service.TransactionService;

/**
 *
 * @author szelc.org
 */
public class TransactionServiceFactory {

    private static TransactionServiceFactory instance;

    private TransactionServiceFactory() {

    }

    public synchronized static TransactionServiceFactory instance() {
        if (instance == null) {
            instance = new TransactionServiceFactory();
        }
        return instance;
    }

    public TransactionService getTransactionService() {
        return TransactionService.instance();
    }

   
}
