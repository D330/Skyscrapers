package ru.flippy.skyscrapers.sdk.api.model;

import java.util.List;

public class DonateInfo<T> {

    private int number;
    private List<T> donates;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<T> getDonates() {
        return donates;
    }

    public void setDonates(List<T> donates) {
        this.donates = donates;
    }
}
