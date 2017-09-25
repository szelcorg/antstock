package org.szelc.app.antstock.view.listener;

import org.apache.log4j.Logger;
import org.szelc.app.antstock.view.transaction.TransactionViewController;
import org.szelc.app.antstock.view.wallet.WalletViewController;

/**
 *
 * @author szelc.org
 */
public class SystemListener {

    private static final Logger log = Logger.getLogger(SystemListener.class);
    private static SystemListener systemListener;
    private WalletViewController walletViewController;
    private TransactionViewController transactionViewController;
    
    private SystemListener() {
    }
    
    public static SystemListener getInstance() {
        if (systemListener == null) {
            systemListener = new SystemListener();
        }
        return systemListener;
    }
    
    public void addTransactionEvent() {
        log.info("addTransactionEvent");
        walletViewController.reload(true);
    }
    
    public void setWalletViewController(WalletViewController walletViewController) {
        this.walletViewController = walletViewController;
    }
    
    public void setTransactionViewController(TransactionViewController transactionViewController) {
        this.transactionViewController = transactionViewController;
    }
    
}
