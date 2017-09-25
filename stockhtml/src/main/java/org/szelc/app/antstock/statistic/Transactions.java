

package org.szelc.app.antstock.statistic;

import java.util.List;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.data.TransactionType;


/**
 *
 * @author ms
 */
public interface Transactions {

    void addRevertTransaction(Transaction transaction);

    void addTransaction(Transaction transaction);
    
//    Companies getCompaniesWithOpenTransactions();
//    Companies getCompaniesWithOpenOrCloseTransaction();

    List<Transaction> getTransactions();
    
    List<Transaction> getTransactions(String dateFrom, String dateTo);

    List<Transaction> getTransactions(String company);

    List<Transaction> getTransactions(String company, TransactionType type);

    List<Transaction> getTransactions(String company, String dateFrom, String dateTo);

    List<Transaction> getSellTransactions();

    int getNumberOfSellShares();

    List<Transaction> getPurchaseTransactions();
    Float getTotalMoneyProwizja();
    Integer getNumberTransactions();
    Float getTotalMoneyValue();
    
    Integer getNumberShares(String company, String dateFrom, String dateTo);
   
}
