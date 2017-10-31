package ru.flippy.skyscrapers.api.models;

public class User {

    public static class sex {
        public static final int MAN = 1;
        public static final int WOMAN = 0;
    }

    public static class online {
        public static final int OFFLINE = 0;
        public static final int ONLINE_STAR = 1;
        public static final int ONLINE = 2;
    }

    private long id;
    private String nick;
    private int level, sex, online;

    public void setId(long id) {
        this.id = id;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public long getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public int getLevel() {
        return level;
    }

    public int getSex() {
        return sex;
    }

    public int getOnline() {
        return online;
    }
}
