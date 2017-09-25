package org.szelc.app.antstock.loader.transaction;

import org.szelc.app.antstock.data.enumeration.BankEnum;

/**
 *
 * @author mszelc
 */
public class TransactionUtil {

    public static Float getPercentProvision(BankEnum bank) {

        switch (bank) {
            case DB:
                return 0.19f;
            case Alior:
                return 0.21f;
            case BOS_IKE:             
            case BOS_IKZE:
                return 0.29f;
            default:
                return 0.39f;
        }
    }

    public static Float calculateProvision(BankEnum bank, Float provisionMoney) {
        switch (bank) {
            case DB:
                if (provisionMoney < 1.9) {
                    provisionMoney = 1.9f;
                }
                break;
            case Alior:
                if(provisionMoney < 3.0){
                    return 3.0f;
                }
                break;
            case MBank:
                if (provisionMoney < 3.0) {
                    provisionMoney = 3.0f;
                }
                break;
            case BOS_IKE:
            case BOS_IKZE:
                if (provisionMoney < 5.0) {
                    provisionMoney = 5.0f;
                }
                break;
            default:
                break;
        }
        return provisionMoney;
    }
}
