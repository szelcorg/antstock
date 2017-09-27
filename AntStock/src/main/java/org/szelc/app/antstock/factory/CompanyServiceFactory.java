package org.szelc.app.antstock.factory;

import org.szelc.app.antstock.service.CompanyService;

/**
 *
 * @author szelc.org
 */
public class CompanyServiceFactory {

    private static CompanyServiceFactory instance;

    private final CompanyService companyService = new CompanyService();

    private CompanyServiceFactory() {
    }

    public synchronized static CompanyServiceFactory instance() {
        if (instance == null) {
            instance = new CompanyServiceFactory();
        }
        return instance;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }
}
