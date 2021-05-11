package org.manuelelucchi.data;

import java.util.Date;

import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.models.Card;
import org.manuelelucchi.models.SubscriptionType;

public class CardManager {
    private static CardManager _instance;

    private CardManager() {
    }

    public static CardManager getInstance() {
        if (_instance == null) {
            _instance = new CardManager();
        }
        return _instance;
    }

    // @requires expireDate != null
    // @requires cvv > 100
    // @requires cvv < 999
    // @requires number.length == 16
    public boolean isValidCard(String number, Date expireDate, int cvv, SubscriptionType type) {
        var isValid = true; // Simulated
        boolean isExpired;

        switch (type) {
            case day:
                isExpired = DateUtils.oneDay().getTime() > expireDate.getTime();
                break;
            case week:
                isExpired = DateUtils.oneWeek().getTime() > expireDate.getTime();
                break;
            case year:
                isExpired = DateUtils.oneYear().getTime() > expireDate.getTime();
                break;
            default:
                isExpired = true;
        }

        return isValid && !isExpired;
    }

    // @requires card != null
    // @requires amount >0
    public boolean pay(Card card, double amount) {
        return true;
    }
}
