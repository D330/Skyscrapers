package ru.flippy.skyscrapers.sdk.util;

import java.util.Arrays;

public class Utils {

    public static Long getValueAfterLastSlash(String url) {
        String cutCookie = url.split(";")[0];
        String cutSlash = cutCookie.substring(0, cutCookie.length() - (cutCookie.endsWith("/") ? 1 : 0));
        return Long.parseLong(cutSlash.substring(cutSlash.lastIndexOf("/") + 1));
    }

    public static int getMonthIndex(String month) {
        String[] months = {"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
        return Arrays.asList(months).indexOf(month);
    }
}
