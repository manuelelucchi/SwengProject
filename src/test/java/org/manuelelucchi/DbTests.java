package org.manuelelucchi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;

public class DbTests {

    private static DbManager db = DbManager.getInstance();

    @BeforeAll
    public static void setup() {
        File file = new File("./data.db");
        file.delete();

        db.ensureCreated();
    }

    @DisplayName("Test Login")
    @Test
    void testLogin() {

        Subscription s = db.login(1, "admin");

        assertNotNull(s);
    }

    @DisplayName("Test Register")
    @Test
    void testRegister() {
        assertNotNull(db.register("psd", SubscriptionType.day, false, 12345, DateUtils.oneYear()));
    }

    @DisplayName("Test Report")
    @Test
    void testReport() {
        assertTrue(db.reportBroken(1));
    }
}
