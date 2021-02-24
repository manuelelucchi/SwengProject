package org.manuelelucchi.domain;

import java.util.Random;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "card")
public class Card {
    public Card() {
    }

    public Card(long id) {
        this.id = id;
        this.content = new Random().nextInt(100000);
    }

    @DatabaseField(columnName = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @DatabaseField(columnName = "content")
    private double content;

    public double getContent() {
        return content;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public boolean canPay(double amount) {
        return content >= amount;
    }

    public void pay(double amount) {
        this.content -= amount;
    }
}
