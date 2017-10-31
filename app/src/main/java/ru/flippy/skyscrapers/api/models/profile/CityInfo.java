package ru.flippy.skyscrapers.api.models.profile;

public class CityInfo {

    private long id;
    private String name, role, status, experience;
    private int dayCount, budgetCoins, budgetDollars;
    private boolean canChangeRole, guildInvite, birthday;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public void setBudgetCoins(int budgetCoins) {
        this.budgetCoins = budgetCoins;
    }

    public void setBudgetDollars(int budgetDollars) {
        this.budgetDollars = budgetDollars;
    }

    public void setCanChangeRole(boolean canChangeRole) {
        this.canChangeRole = canChangeRole;
    }

    public void setGuildInvite(boolean guildInvite) {
        this.guildInvite = guildInvite;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getExperience() {
        return experience;
    }

    public int getDayCount() {
        return dayCount;
    }

    public int getBudgetCoins() {
        return budgetCoins;
    }

    public int getBudgetDollars() {
        return budgetDollars;
    }

    public boolean canChangeRole() {
        return canChangeRole;
    }

    public boolean canGuildInvite() {
        return guildInvite;
    }

    public boolean celebrateBirthday() {
        return birthday;
    }
}
