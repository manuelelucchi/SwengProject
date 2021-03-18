package org.manuelelucchi.models;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "card")
public class Card {
    public Card() {
    }

    public Card(long code, Date expirDate) {
        this.code = code;
        this.expireDate = expirDate;
    }

    @DatabaseField(id = true)
    private long code;

    public long getCode() {
        return code;
    }

    @DatabaseField
    private Date expireDate;

    public Date getExpireDate() {
        return expireDate;
    }

    public boolean pay(double amount) {
        // Simulated
        return true;
    }

    @ForeignCollectionField
    private ForeignCollection<Subscription> subscriptions;

    public ForeignCollection<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
