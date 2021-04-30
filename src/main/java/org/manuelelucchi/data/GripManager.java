package org.manuelelucchi.data;

import java.util.function.Function;

import org.manuelelucchi.models.Grip;

/**
 * GripManager
 */
public class GripManager {
    private static GripManager _instance;

    private GripManager() {
    }

    public static GripManager getInstance() {
        if (_instance == null) {
            _instance = new GripManager();
        }
        return _instance;
    }

    public boolean blockGrip(Grip grip) {
        return true; // Simulation
    }

    public boolean unlockGrip(Grip grip) {
        return true; // Simulation
    }
}