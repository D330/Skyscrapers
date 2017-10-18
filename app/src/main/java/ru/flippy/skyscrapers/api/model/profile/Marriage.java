package ru.flippy.skyscrapers.api.model.profile;

import ru.flippy.skyscrapers.api.model.User;

public class Marriage extends User {

    private boolean newlyweds;

    public void setNewlyweds(boolean newlyweds) {
        this.newlyweds = newlyweds;
    }

    public boolean isNewlyweds() {
        return newlyweds;
    }
}
