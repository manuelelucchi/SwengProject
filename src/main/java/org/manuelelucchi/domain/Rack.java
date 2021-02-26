package org.manuelelucchi.domain;

import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "rack")
public class Rack {

    @DatabaseField(generatedId = true)
    private UUID id;

    public UUID getId() {
        return id;
    }

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Bike> bikes;

    public ForeignCollection<Bike> getBikes() {
        return bikes;
    }
}
