package org.manuelelucchi.models;

import java.time.Duration;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.manuelelucchi.common.DateUtils;

@DatabaseTable(tableName = "subscription")
public class Subscription {
    public Subscription() {
    }

    public Subscription(User user, Card card, SubscriptionType type) {
        this.card = card;
        this.user = user;
        this.type = type;
    }

    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public double getCost() {
        switch (type) {
            case day:
                return 4.5;
            case week:
                return 9;
            case year:
                return 36;
            default:
                return 0;
        }
    }

    public Duration getTime() {
        switch (type) {
            case day:
                return Duration.ofDays(1);
            case week:
                return Duration.ofDays(7);
            case year:
                return Duration.ofDays(365);
            default:
                return Duration.ofDays(0);
        }
    }

    @DatabaseField
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    @DatabaseField
    private SubscriptionType type;

    public SubscriptionType getType() {
        return type;
    }

    @DatabaseField(foreign = true)
    public User user;

    public User getUser() {
        return user;
    }

    public void activate() {
        this.startTime = DateUtils.now();
    }

    public boolean isExpired() {
        var actualTime = DateUtils.now();
        var difference = actualTime.getTime() - startTime.getTime() - getTime().toMillis();
        return difference <= 0;
    }

    @DatabaseField(foreign = true)
    private Card card;

    public Card getCard() {
        return card;
    }

    @DatabaseField
    private int numberOfExceed = 0;

    public int getNumberOfExceed() {
        return numberOfExceed;
    }

    public void setNumberOfExceed(int numberOfExceed) {
        this.numberOfExceed = numberOfExceed;
    }
}
