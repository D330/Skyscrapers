package ru.flippy.skyscrapers.api.model;

public class TotalData {

    private User authUserData;
    private long balanceCoins, balanceDollars;
    private int levelPercent, onlineCount;
    private Time time;

    public void setAuthUserData(User authUserData) {
        this.authUserData = authUserData;
    }

    public void setBalanceCoins(long balanceCoins) {
        this.balanceCoins = balanceCoins;
    }

    public void setBalanceDollars(long balanceDollars) {
        this.balanceDollars = balanceDollars;
    }

    public void setLevelPercent(int levelPercent) {
        this.levelPercent = levelPercent;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public User getAuthUserData() {
        return authUserData;
    }

    public long getBalanceCoins() {
        return balanceCoins;
    }

    public long getBalanceDollars() {
        return balanceDollars;
    }

    public int getLevelPercent() {
        return levelPercent;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public Time getTime() {
        return time;
    }
}
