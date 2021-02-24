package org.manuelelucchi.domain;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
    public User() {
    }

    public User(String name, String surname, String password, boolean isStudent) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.isStudent = isStudent;
    }

    @DatabaseField(generatedId = true)
    private UUID id;

    public UUID getId() {
        return id;
    }

    @DatabaseField(columnName = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DatabaseField(columnName = "surname")
    private String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @DatabaseField(columnName = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DatabaseField(columnName = "isStudent")
    private boolean isStudent;

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public boolean isStudent() {
        return isStudent;
    }
}
