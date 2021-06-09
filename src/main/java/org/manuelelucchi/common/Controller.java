package org.manuelelucchi.common;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Totem;

public abstract class Controller {
    public void init() {

    }

    public void onNavigateFrom(Controller sender, Object parameter) {

    }

    public int getTotemId() {
        return App.getTotemId();
    }

    public void setTotemId(int id) {
        App.setTotemId(id);
    }

    public boolean isFirstHomeLoad() {
        return !App.homeLoaded;
    }

    public void navigate(String view, Object parameter) {
        try {
            App.navigate(this, view, parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigate(String view) {
        navigate(view, null);
    }
}
