package ru.flippy.skyscrapers.sdk.api.model;

public class PaginationItem {

    private int page, index;
    private long wicket;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getWicket() {
        return wicket;
    }

    public void setWicket(long wicket) {
        this.wicket = wicket;
    }
}
