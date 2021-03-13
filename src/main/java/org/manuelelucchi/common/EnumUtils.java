package org.manuelelucchi.common;

import org.manuelelucchi.models.BikeType;

public class EnumUtils {
    public static String toString(BikeType type) {
        if (type == null)
            return "";
        switch (type) {
        case standard:
            return "Standard";
        case electric:
            return "Electric";
        case electricBabySeat:
            return "Electric with baby seat";
        default:
            return "";
        }
    }
}
