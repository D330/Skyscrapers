package ru.flippy.skyscrapers.sdk.api.model;

public class Pagination {

    public static class Type {
        public static final int WICKET = 0;
        public static final int NORMAL = 1;
    }

    private int pageCount;
    private int currentPage;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
