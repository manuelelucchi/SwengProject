package org.manuelelucchi.models;

public class Transaction {
    public Transaction(Grip grip, Rental rental) {
        this.grip = grip;
        this.rental = rental;
    }

    private Grip grip;
    private Rental rental;

    public Grip getGrip() {
        return grip;
    }

    public Rental getRental() {
        return rental;
    }
}
