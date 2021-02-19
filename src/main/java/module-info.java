module org.openjfx {
    requires javafx.controls;
    requires ormlite.core;
    requires ormlite.jdbc;
    requires java.sql;
    requires sqlite.jdbc;
    requires javafx.base;

    exports org.manuelelucchi;
    exports org.manuelelucchi.domain;

    opens org.manuelelucchi.domain;
}