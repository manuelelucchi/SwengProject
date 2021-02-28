package org.manuelelucchi.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "totem")
public class Totem {
    public Totem() {

    }

    public Totem(String address) {
        this.address = address;
    }

    @DatabaseField
    private int id;

    public int getId() {
        return id;
    }

    @DatabaseField
    private String address;

    public String getAddress() {
        return address;
    }

    @ForeignCollectionField
    ForeignCollection<Grip> grips;

    public ForeignCollection<Grip> getGrips() {
        return grips;
    }

}
