package ru.flippy.skyscrapers.sdk.api.model;

public class SmsDonate {

    private int number, dollars;
    private String price, currency;
    private boolean approximately, nds;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isApproximately() {
        return approximately;
    }

    public void setApproximately(boolean approximately) {
        this.approximately = approximately;
    }

    public boolean withNds() {
        return nds;
    }

    public void setNds(boolean nds) {
        this.nds = nds;
    }
}
