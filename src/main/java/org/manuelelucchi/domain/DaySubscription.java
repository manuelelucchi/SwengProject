package org.manuelelucchi.domain;

import java.time.Duration;

public class DaySubscription extends Subscription {
    public DaySubscription() {
        this.cost = 4.5;
        this.time = Duration.ofDays(1);
    }
}
