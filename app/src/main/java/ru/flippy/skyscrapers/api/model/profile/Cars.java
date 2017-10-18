package ru.flippy.skyscrapers.api.model.profile;

import java.util.List;

public class Cars {

    private int count, stars;
    private List<Integer> ids;

    public Cars(int count, int stars, List<Integer> ids) {
        this.count = count;
        this.stars = stars;
        this.ids = ids;
    }

    public int getCount() {
        return count;
    }

    public int getStars() {
        return stars;
    }

    public List<Integer> getIds() {
        return ids;
    }
}
