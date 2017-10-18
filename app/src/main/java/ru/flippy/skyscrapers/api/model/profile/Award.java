package ru.flippy.skyscrapers.api.model.profile;

public class Award {

    public static final int TOUR_WINNER = 0;
    public static final int SILVER_CUP = 1;
    public static final int GOLD_CUP = 2;
    public static final int UNKNOWN = 3;

    private String name, url;
    private int marketing, relations, type;

    public Award(String name, String url, int marketing, int relations, int type) {
        this.name = name;
        this.url = url;
        this.marketing = marketing;
        this.relations = relations;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getMarketing() {
        return marketing;
    }

    public int getRelations() {
        return relations;
    }

    public int getType() {
        return type;
    }
}
