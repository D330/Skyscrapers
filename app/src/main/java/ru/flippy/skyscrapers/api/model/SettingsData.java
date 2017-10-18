package ru.flippy.skyscrapers.api.model;

public class SettingsData {

    private String nick, about;
    private Date birthday;
    private int sex;
    private boolean guildInvite;

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setGuildInvite(boolean guildInvite) {
        this.guildInvite = guildInvite;
    }

    public String getNick() {
        return nick;
    }

    public String getAbout() {
        return about;
    }

    public boolean birthdayIsSpecified() {
        return birthday != null;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getSex() {
        return sex;
    }

    public boolean isGuildInvite() {
        return guildInvite;
    }
}
