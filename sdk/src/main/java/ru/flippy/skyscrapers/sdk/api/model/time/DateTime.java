package ru.flippy.skyscrapers.sdk.api.model.time;

public class DateTime {

    private Date date;
    private Time time;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
}
