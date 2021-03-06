package org.manuelelucchi.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "bike")
public class Bike {

    public Bike() {

    }

    public Bike(BikeType type) {
        this.type = type;
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

    @DatabaseField(foreign = true)
    private Grip grip;

    public Grip getGrip() {
        return grip;
    }

    public void setGrip(Grip grip) {
        this.grip = grip;
    }

    @ForeignCollectionField
    private ForeignCollection<Rental> rentals;

    public ForeignCollection<Rental> getRentals() {
        return rentals;
    }

    @DatabaseField
    boolean isBroken;

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public double getCost(long halfHours) {
        if (type == BikeType.standard) {
            if (halfHours == 1) {
                return 0;
            } else if (halfHours <= 4) {
                return (0.5 * (halfHours - 1));
            } else {
                return 1.5 + (2 * halfHours - 4);
            }
        } else {
            switch ((int) halfHours) {
            case 1:
                return 0.25;
            case 2:
                return 0.50;
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return 4 * (halfHours - 4) + 2;
            }
        }
    }
}
