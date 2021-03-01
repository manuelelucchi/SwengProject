package org.manuelelucchi.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.manuelelucchi.common.DateUtils;

@DatabaseTable(tableName = "rental")
public class Rental {

    public Rental() {

    }

    public Rental(Subscription subscription, Bike bike) {
        this.subscription = subscription;
        this.bike = bike;
        start = DateUtils.now();
    }

    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    @DatabaseField
    private Date start;

    public Date getStart() {
        return start;
    }

    @DatabaseField
    private Date end;

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @DatabaseField(foreign = true)
    private Bike bike;

    public Bike getBike() {
        return bike;
    }

    @DatabaseField(foreign = true)
    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }
}
