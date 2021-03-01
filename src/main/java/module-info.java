module org.openjfx {
    requires javafx.controls;
    requires ormlite.core;
    requires ormlite.jdbc;
    requires java.sql;
    requires sqlite.jdbc;
    requires javafx.base;
    requires javafx.fxml;

    exports org.manuelelucchi;
    exports org.manuelelucchi.models;
    exports org.manuelelucchi.data;
    exports org.manuelelucchi.controllers;
    exports org.manuelelucchi.common;

    opens org.manuelelucchi.models;
    opens org.manuelelucchi.data;
    opens org.manuelelucchi.controllers;
    opens org.manuelelucchi.common;
}