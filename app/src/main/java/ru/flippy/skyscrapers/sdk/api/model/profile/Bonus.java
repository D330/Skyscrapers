package ru.flippy.skyscrapers.sdk.api.model.profile;

public class Bonus {

    private int marketing, relations, currentRevenue, recordRevenue;
    private String managerTime;

    public void setMarketing(int marketing) {
        this.marketing = marketing;
    }

    public void setRelations(int relations) {
        this.relations = relations;
    }

    public void setCurrentRevenue(int currentRevenue) {
        this.currentRevenue = currentRevenue;
    }

    public void setRecordRevenue(int recordRevenue) {
        this.recordRevenue = recordRevenue;
    }

    public void setManagerTime(String managerTime) {
        this.managerTime = managerTime;
    }

    public int getMarketing() {
        return marketing;
    }

    public int getRelations() {
        return relations;
    }

    public int getCurrentRevenue() {
        return currentRevenue;
    }

    public int getRecordRevenue() {
        return recordRevenue;
    }

    public String getManagerTime() {
        return managerTime;
    }
}
