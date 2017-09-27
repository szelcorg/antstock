package org.szelc.app.antstock.factory;

import org.szelc.app.antstock.service.QuoteService;

/**
 *
 * @author szelc.org
 */
public class QuoteServiceFactory {

    private static QuoteServiceFactory instance;

    private QuoteServiceFactory() {

    }

    public synchronized static QuoteServiceFactory instance() {
        if (instance == null) {
            instance = new QuoteServiceFactory();
        }
        return instance;
    }

    public QuoteService getQuoteService() {
        return QuoteService.instance();
    }

}
