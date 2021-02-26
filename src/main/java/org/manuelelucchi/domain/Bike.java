package org.manuelelucchi.domain;

import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "bicycle")
public class Bike {

    public static Bike StandardBike() {
        Bike b = new Bike();
        b.childSeat = false;
        b.isElectric = false;
        b.returned = null; // Da vedere
        b.withdrawal = null;
        return b;
    }

    public static Bike ElectricBike() {
        Bike b = new Bike();
        b.childSeat = false;
        b.isElectric = true;
        b.returned = null; // Da vedere
        b.withdrawal = null;
        return b;
    }

    public static Bike ElectricChildSeatBike() {
        Bike b = ElectricBike();
        b.childSeat = true;
        return b;
    }

    @DatabaseField(generatedId = true)
    private UUID id;

    public UUID getId() {
        return id;
    }

    @DatabaseField(columnName = "withdrawal")
    private Date withdrawal;

    public Date getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Date withdrawal) {
        this.withdrawal = withdrawal;
    }

    @DatabaseField(columnName = "returned")
    private Date returned;

    public Date getReturned() {
        return returned;
    }

    public void setReturned(Date returned) {
        this.returned = returned;
    }

    @DatabaseField(columnName = "gripType")
    private int gripType;

    public int getGripType() {
        return gripType;
    }

    @DatabaseField(columnName = "childSeat")
    private boolean childSeat;

    public boolean hasChildSeat() {
        return childSeat;
    }

    @DatabaseField(columnName = "isElectric")
    private boolean isElectric;

    public boolean isElectric() {
        return isElectric;
    }

    @DatabaseField(columnName = "rackPosition")
    private int rackPosition = -1;

    public int getRackPosition() {
        return rackPosition;
    }

    public void setRackPosition(int rackPosition) {
        this.rackPosition = rackPosition;
    }

    @DatabaseField(foreign = true)
    Rack rack;

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }
}
