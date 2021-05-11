package org.manuelelucchi.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "totem")
public class Totem {
    public Totem() {

    }

    public Totem(String address, int x, int y) {
        this.address = address;
        this.x = x;
        this.y = y;
    }

    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    @DatabaseField
    private int x;

    public int getX() {
        return x;
    }

    @DatabaseField
    private int y;

    public int getY() {
        return y;
    }

    @DatabaseField
    private /* @ non_null @ */ String address;

    public String getAddress() {
        return address;
    }

    @ForeignCollectionField
    ForeignCollection<Grip> grips;

    public ForeignCollection<Grip> getGrips() {
        return grips;
    }

    @ForeignCollectionField
    ForeignCollection<Rental> rentals;

    public ForeignCollection<Rental> getRentals() {
        return this.rentals;
    }

}
