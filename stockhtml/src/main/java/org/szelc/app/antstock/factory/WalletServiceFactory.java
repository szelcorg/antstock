package org.szelc.app.antstock.factory;

import org.szelc.app.antstock.service.WalletService;

/**
 *
 * @author szelc.org
 */
public class WalletServiceFactory {

    private static WalletServiceFactory instance;

    private final WalletService companyService = new WalletService();

    private WalletServiceFactory() {
    }

    public synchronized static WalletServiceFactory instance() {
        if (instance == null) {
            instance = new WalletServiceFactory();
        }
        return instance;
    }

    public WalletService getWalletService() {
        return companyService;
    }
}
