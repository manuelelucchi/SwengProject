package org.manuelelucchi.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "grip")
public class Grip {
    public Grip() {

    }

    public Grip(Totem totem, BikeType type, int position) {
        this.type = type;
        this.position = position;
        this.totem = totem;
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

    // @ invariant position >= 0
    @DatabaseField
    private int position;

    // @ ensures position >= 0
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

    @DatabaseField(foreign = true)
    private Totem totem;

    public Totem getTotem() {
        return totem;
    }
}
