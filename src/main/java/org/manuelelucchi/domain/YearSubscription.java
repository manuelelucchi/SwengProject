package org.manuelelucchi.domain;

import java.time.Duration;

public class YearSubscription extends Subscription {
    public YearSubscription() {
        this.cost = 36;
        this.time = Duration.ofDays(365);
    }
}
