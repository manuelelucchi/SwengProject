package org.manuelelucchi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.data.GripManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;

public class DbTests {

    private static DbManager db = DbManager.getInstance();

    @BeforeAll
    public static void setup() {
        File file = new File("./data.db");
        file.delete();

        db.ensureCreated();
        db.createFakeData();
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
        assertNotNull(db.register("psd", SubscriptionType.day, false, "1111111111111111", DateUtils.oneYear(), 444));
    }

    @DisplayName("Test Report")
    @Test
    void testReport() {
        assertTrue(db.reportBroken(1));
    }

    @DisplayName("Test Make Operative")
    @Test
    void testMakeOperative() {

        assertTrue(db.makeBikeOperative(db.getBikes(1).get(0)));
    }

    @DisplayName("Test Unlock Grip")
    @Test
    void testUnlockGrip() {
        var grip = DbManager.getInstance().getTotem(1).getGrips().stream().collect(Collectors.toList()).get(0);
        assertTrue(GripManager.getInstance().unlockGrip(grip));
    }

    @DisplayName("Test Block Grip")
    @Test
    void testBlckGrip() {
        var grip = DbManager.getInstance().getTotem(1).getGrips().stream().collect(Collectors.toList()).get(0);
        assertTrue(GripManager.getInstance().blockGrip(grip));
    }
}
