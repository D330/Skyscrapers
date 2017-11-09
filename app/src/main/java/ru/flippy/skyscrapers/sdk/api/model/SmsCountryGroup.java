package ru.flippy.skyscrapers.sdk.api.model;

import java.util.List;

public class SmsCountryGroup {
    private String country;
    private List<String> operators;
    private List<SmsDonate> donates;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public List<SmsDonate> getDonates() {
        return donates;
    }

    public void setDonates(List<SmsDonate> donates) {
        this.donates = donates;
    }
}
