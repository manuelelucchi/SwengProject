package org.manuelelucchi.models;

import java.time.Duration;
import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.manuelelucchi.common.DateUtils;

@DatabaseTable(tableName = "subscription")
public class Subscription {
    public Subscription() {
    }

    public Subscription(String password, SubscriptionType type, boolean isStudent) {
        this.type = type;
        this.password = password;
        this.isStudent = isStudent;
    }

    @DatabaseField(generatedId = true)
    private int code;

    public int getCode() {
        return code;
    }

    @DatabaseField
    private String password;

    public String getPassword() {
        return password;
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

    @DatabaseField
    private boolean isStudent;

    public boolean isStudent() {
        return isStudent;
    }

    public boolean isAdmin() {
        return type == SubscriptionType.admin;
    }

    @DatabaseField(foreign = true)
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @DatabaseField
    private int numberOfExceed = 0;

    public int getNumberOfExceed() {
        return numberOfExceed;
    }

    public void setNumberOfExceed(int numberOfExceed) {
        this.numberOfExceed = numberOfExceed;
    }

    public boolean isTerminated() {
        return numberOfExceed >= 3;
    }

    @ForeignCollectionField
    ForeignCollection<Rental> rentals;

    public ForeignCollection<Rental> getRentals() {
        return rentals;
    }

    public void activate() {
        this.startTime = DateUtils.now();
    }

    public boolean isExpired() {
        if (startTime == null) {
            return false;
        }
        var actualTime = DateUtils.now();
        var difference = actualTime.getTime() - startTime.getTime() - getTime().toMillis();
        return difference <= 0;
    }

    public double getCost() {
        switch (type) {
        case day:
            return 4.5;
        case week:
            return 9;
        case year:
            return 36;
        case admin:
            return 0;
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
        case admin:
            return Duration.ofDays(1000);
        default:
            return Duration.ofDays(0);
        }
    }
}
