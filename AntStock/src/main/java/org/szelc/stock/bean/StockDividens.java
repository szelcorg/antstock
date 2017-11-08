package org.szelc.stock.bean;

import org.szelc.logger.LOG;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author by marcin.szelc on 2017-10-16.
 */
public class StockDividens {

    private List<StockDividend> stockDividendList = new LinkedList<>();

    public StockDividens() {
    }

    public StockDividens(List<StockDividend> stockDividendList) {
        this.stockDividendList = stockDividendList;
    }

    public List<StockDividend> getStockDividendList() {
        return stockDividendList;
    }

    public void setStockDividendList(List<StockDividend> stockDividendList) {
        this.stockDividendList = stockDividendList;
    }

    public void add(StockDividend dividend){
        stockDividendList.add(dividend);
    }

    public void clear(){
        stockDividendList.clear();
    }

    public void add(StockDividens dividens) {
        stockDividendList.addAll(dividens.getStockDividendList());
    }

    public boolean display(Map<String, String> codeCompanyMap) {
        float netProfit = 0f;
        LOG.i("DISPALY ALL stock size "+stockDividendList.size());
        for(StockDividend d: stockDividendList){
            LOG.i("["+d.company+"] [" +codeCompanyMap.get(d.company)+"] gross "+d.getGrossMoney()+" tax "+d.getTaxOfMoney()+" net "+d.getNet());
            netProfit +=d.getNet();
            if(codeCompanyMap.get(d.company)==null){
                LOG.i("COMPANY "+d.company);
                return false;
            }
        }
        LOG.i("");
        LOG.i("Dividend net profit ["+netProfit+"]");
        LOG.i("");
        return true;
    }
}
