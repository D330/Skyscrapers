package ru.flippy.skyscrapers.sdk.api.model;

public class Donate {

    public static class Type {
        public static final String MOBILE = "xMobilePayment/operator/megafon";
        public static final String QIWI = "xQiwi";
        public static final String YANDEX = "xYandex";
        public static final String WEBMONEY = "xWebmoney";
        public static final String WEBMONEY_INVOICE = "xWebmoneyInvoice";
        public static final String CARD = "xBank";
        public static final String TERMINALS = "xTerminals";
        public static final String QIWI_TERMINALS = "qTerminals";
        public static final String SMS = "smsMoney";
        public static final String SMS_OTHER = "smsOther";

        public static final int YANDEX_PID = 27;
        public static final int WEBMONEY_PID = 6;
        public static final int CARD_PID = 26;
    }

    private long id;
    private int price, dollars, bonus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
