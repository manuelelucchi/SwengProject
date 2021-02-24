package org.manuelelucchi.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Subscription")
public class Subscription {
    public Subscription() {
    }

    @DatabaseField(columnName = "cost")
    protected double cost;

    public double getCost() {
        return cost;
    }

    @DatabaseField(columnName = "time")
    protected Duration time;

    public Duration getTime() {
        return time;
    }

    @DatabaseField(columnName = "startTime")
    private LocalDateTime startTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void activate() {
        this.startTime = LocalDateTime.now();
    }

    public boolean isExpired() {
        return Duration.between(startTime, LocalDateTime.now()).minus(time).isZero();
    }
}
