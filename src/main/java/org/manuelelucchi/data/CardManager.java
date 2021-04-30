package org.manuelelucchi.data;

import java.util.Date;

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

    public boolean isValidCard(int number, Date expireDate, int cvv, SubscriptionType type) {
        return true; // Simulated
    }

    public boolean pay(Card card, double amount) {
        return true;
    }
}
