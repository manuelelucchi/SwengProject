package org.manuelelucchi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.data.GripManager;
import org.manuelelucchi.models.Bike;
import org.manuelelucchi.models.BikeType;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DomainTests {
    @DisplayName("Bike cost")
    @Test
    void testBikeCost() {
        Bike b1 = new Bike(BikeType.standard);
        Bike b2 = new Bike(BikeType.electric);
        Bike b3 = new Bike(BikeType.electricBabySeat);

        assertTrue(b1.getCost(1) == 0);
        assertTrue(b1.getCost(4) == 1.5);
        assertTrue(b1.getCost(5) == 3.5);

        assertTrue(b2.getCost(1) == 0.25);
        assertTrue(b2.getCost(3) == 1);
        assertTrue(b2.getCost(5) == 6);

        assertTrue(b3.getCost(2) == 0.5);
        assertTrue(b3.getCost(4) == 2);
        assertTrue(b3.getCost(6) == 10);
    }

    @DisplayName("Subscription expiration")
    @Test
    void testSubscriptionExpiration() {
        Subscription s = new Subscription("test", SubscriptionType.day, false);
        s.setNumberOfExceed(3);
        assertTrue(s.isExpired());
    }

    @DisplayName("Subscription cost")
    @Test
    void testSubscriptionCost() {
        assertTrue(new Subscription("test", SubscriptionType.day, false).getCost() == 4.5);
        assertTrue(new Subscription("test", SubscriptionType.week, false).getCost() == 9);
        assertTrue(new Subscription("test", SubscriptionType.year, false).getCost() == 36);
        assertTrue(new Subscription("test", SubscriptionType.admin, false).getCost() == 0);
        assertTrue(new Subscription("test", SubscriptionType.day, true).getCost() == 0);
    }
}