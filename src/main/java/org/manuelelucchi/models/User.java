package org.manuelelucchi.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
    public User() {
    }

    public User(String password, boolean isStudent, boolean isAdmin) {
        this.password = password;
        this.isStudent = isStudent;
        this.isAdmin = isAdmin;
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

    public void setPassword(String password) {
        this.password = password;
    }

    @DatabaseField
    private boolean isStudent;

    public boolean isStudent() {
        return isStudent;
    }

    @DatabaseField
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    @DatabaseField(foreign = true)
    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @ForeignCollectionField
    ForeignCollection<Rental> rentals;

    public ForeignCollection<Rental> getRentals() {
        return rentals;
    }
}
