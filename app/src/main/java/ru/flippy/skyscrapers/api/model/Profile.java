package ru.flippy.skyscrapers.api.model;

import java.util.List;

import ru.flippy.skyscrapers.api.model.profile.Award;
import ru.flippy.skyscrapers.api.model.profile.Bonus;
import ru.flippy.skyscrapers.api.model.profile.Business;
import ru.flippy.skyscrapers.api.model.profile.Cars;
import ru.flippy.skyscrapers.api.model.profile.CityInfo;
import ru.flippy.skyscrapers.api.model.profile.Experience;
import ru.flippy.skyscrapers.api.model.profile.Marriage;
import ru.flippy.skyscrapers.api.model.profile.Personal;

public class Profile extends User {

    private String about;
    private int floorCount, cupCount, giftCount, gameDayCount;
    private double floorStars;
    private boolean friend, birthday;

    private List<Award> awards;
    private Date lastEntrance;
    private Cars cars;
    private CityInfo cityInfo;
    private Bonus bonus;
    private Experience experience;
    private Personal personal;
    private List<Business> business;
    private Marriage marriage;

    public void setAbout(String about) {
        this.about = about;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }

    public void setCupCount(int cupCount) {
        this.cupCount = cupCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public void setGameDayCount(int gameDayCount) {
        this.gameDayCount = gameDayCount;
    }

    public void setFloorStars(double floorStars) {
        this.floorStars = floorStars;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public void setLastEntrance(Date lastEntrance) {
        this.lastEntrance = lastEntrance;
    }

    public void setCars(Cars cars) {
        this.cars = cars;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public void setBusiness(List<Business> business) {
        this.business = business;
    }

    public void setMarriage(Marriage marriage) {
        this.marriage = marriage;
    }

    public String getAbout() {
        return about;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public int getCupCount() {
        return cupCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public int getGameDayCount() {
        return gameDayCount;
    }

    public double getFloorStars() {
        return floorStars;
    }

    public boolean isFriend() {
        return friend;
    }

    public boolean celebrateBirthday() {
        return birthday;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public Date getLastEntrance() {
        return lastEntrance;
    }

    public boolean hasCars() {
        return cars != null;
    }

    public Cars getCars() {
        return cars;
    }

    public boolean hasCity() {
        return cityInfo != null;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public boolean experienceIsAvailable() {
        return experience != null;
    }

    public Experience getExperience() {
        return experience;
    }

    public Personal getPersonal() {
        return personal;
    }

    public boolean hasBusiness() {
        return business != null;
    }

    public List<Business> getBusiness() {
        return business;
    }

    public boolean isMarried() {
        return marriage != null;
    }

    public Marriage getMarriage() {
        return marriage;
    }
}