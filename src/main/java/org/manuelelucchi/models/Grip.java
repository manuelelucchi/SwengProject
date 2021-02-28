package org.manuelelucchi.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "grip")
public class Grip {
    public Grip() {

    }

    public Grip(BikeType type, int position) {
        this.type = type;
        this.position = position;
    }

    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    @DatabaseField
    private BikeType type;

    public BikeType getType() {
        return type;
    }

    @DatabaseField
    private int position;

    public int getPosition() {
        return position;
    }

    @DatabaseField(foreign = true)
    private Bike bike;

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }
}
