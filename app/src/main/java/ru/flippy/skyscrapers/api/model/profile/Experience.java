package ru.flippy.skyscrapers.api.model.profile;

public class Experience {

    private String current, max;
    private int percent;

    public Experience(String current, String max, int percent) {
        this.current = current;
        this.max = max;
        this.percent = percent;
    }

    public String getCurrent() {
        return current;
    }

    public String getMax() {
        return max;
    }

    public int getPercent() {
        return percent;
    }
}
