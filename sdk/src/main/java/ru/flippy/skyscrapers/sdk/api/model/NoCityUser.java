package ru.flippy.skyscrapers.sdk.api.model;

public class NoCityUser extends User {
    private int daysInGame;
    private boolean freeForInvitation;

    public int getDaysInGame() {
        return daysInGame;
    }

    public void setDaysInGame(int daysInGame) {
        this.daysInGame = daysInGame;
    }

    public boolean isFreeForInvitation() {
        return freeForInvitation;
    }

    public void setFreeForInvitation(boolean freeForInvitation) {
        this.freeForInvitation = freeForInvitation;
    }
}
