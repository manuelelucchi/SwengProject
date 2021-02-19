package org.manuelelucchi.domain;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
    public User() {
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @DatabaseField(generatedId = true)
    private UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "surname")
    private String surname;

    @DatabaseField(columnName = "password")
    private String password;
}
