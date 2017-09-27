package org.szelc.app.antstock.factory;

import org.szelc.app.antstock.service.EvaluateService;

/**
 *
 * @author szelc.org
 */
public class EvaluateServiceFactory {

    private static EvaluateServiceFactory instance;

    private EvaluateServiceFactory() {

    }

    public synchronized static EvaluateServiceFactory instance() {
        if (instance == null) {
            instance = new EvaluateServiceFactory();
        }
        return instance;
    }

    public EvaluateService getEvaluateService() {
        return EvaluateService.instance();
    }
}
