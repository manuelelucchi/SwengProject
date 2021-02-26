package org.manuelelucchi.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "subscription")
public class Subscription {
    public static Subscription DaySubscription(User user) {
        var subscription = new Subscription();
        subscription.user = user;
        subscription.cost = 4.5;
        subscription.time = Duration.ofDays(1).getSeconds();
        return subscription;
    }

    public static Subscription WeekSubscription(User user) {
        var subscription = new Subscription();
        subscription.user = user;
        subscription.cost = 9;
        subscription.time = Duration.ofDays(7).getSeconds();
        return subscription;
    }

    public static Subscription YearSubscription(User user) {
        var subscription = new Subscription();
        subscription.user = user;
        subscription.cost = 36;
        subscription.time = Duration.ofDays(365).getSeconds();
        return subscription;
    }

    public Subscription() {
    }

    @DatabaseField(generatedId = true)
    private UUID id;

    public UUID getId() {
        return id;
    }

    @DatabaseField(columnName = "cost")
    protected double cost;

    public double getCost() {
        return cost;
    }

    @DatabaseField(columnName = "time")
    protected long time;

    public Duration getTime() {
        return Duration.ofSeconds(time);
    }

    @DatabaseField(columnName = "startTime")
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    @DatabaseField(foreign = true)
    public User user;

    public User getUser() {
        return user;
    }

    public void activate() {
        this.startTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean isExpired() {
        var actualTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        var difference = actualTime.getTime() - startTime.getTime() - getTime().toMillis();
        return difference <= 0;
    }
}
