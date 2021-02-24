package org.manuelelucchi.domain;

import java.time.Duration;

public class WeekSubscription extends Subscription {
    public WeekSubscription() {
        this.cost = 9;
        this.time = Duration.ofDays(7);
    }
}
