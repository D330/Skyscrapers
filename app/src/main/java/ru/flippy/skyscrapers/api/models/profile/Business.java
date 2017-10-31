package ru.flippy.skyscrapers.api.models.profile;

public class Business {

    private long id;
    private String name;
    private int bonus;

    public Business(long id, String name, int bonus) {
        this.id = id;
        this.name = name;
        this.bonus = bonus;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBonus() {
        return bonus;
    }
}
