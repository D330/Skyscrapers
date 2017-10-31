package ru.flippy.skyscrapers.api.models.profile;

public class Personal {

    private int happy, specialists, population;

    public Personal(int happy, int specialists, int population) {
        this.happy = happy;
        this.specialists = specialists;
        this.population = population;
    }

    public int getHappy() {
        return happy;
    }

    public int getSpecialists() {
        return specialists;
    }

    public int getPopulation() {
        return population;
    }
}
