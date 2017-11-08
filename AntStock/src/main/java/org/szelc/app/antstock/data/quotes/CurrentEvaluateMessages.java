package org.szelc.app.antstock.data.quotes;

/**
 * @author by marcin.szelc on 2017-11-06.
 */
public class CurrentEvaluateMessages {

    StringBuilder buySbNew= new StringBuilder();
    StringBuilder sellSbNew= new StringBuilder();

    StringBuilder buySbLast= new StringBuilder();
    StringBuilder sellSbLast= new StringBuilder();

    public StringBuilder getBuySbNew() {
        return buySbNew;
    }

    public void setBuySbNew(StringBuilder buySbNew) {
        this.buySbNew = buySbNew;
    }

    public StringBuilder getSellSbNew() {
        return sellSbNew;
    }

    public void setSellSbNew(StringBuilder sellSbNew) {
        this.sellSbNew = sellSbNew;
    }

    public StringBuilder getBuySbLast() {
        return buySbLast;
    }

    public void setBuySbLast(StringBuilder buySbLast) {
        this.buySbLast = buySbLast;
    }

    public StringBuilder getSellSbLast() {
        return sellSbLast;
    }

    public void setSellSbLast(StringBuilder sellSbLast) {
        this.sellSbLast = sellSbLast;
    }
}
