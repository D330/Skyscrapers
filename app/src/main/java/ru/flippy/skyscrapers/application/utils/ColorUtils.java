package ru.flippy.skyscrapers.application.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.api.model.User;

public class ColorUtils {

    @ColorInt
    public static int getLevelColor(Context context, int level, int sex) {
        int colorId = 0;
        switch (Math.max(1, level / 10)) {
            case 1:
            case 2:
                if (sex == User.sex.MAN) {
                    colorId = R.color.level_1_man;
                } else if (sex == User.sex.WOMAN) {
                    colorId = R.color.level_1_woman;
                }
            case 3:
                colorId = R.color.level_30;
                break;
            case 4:
                colorId = R.color.level_40;
                break;
            case 5:
                colorId = R.color.level_50;
                break;
            case 6:
                colorId = R.color.level_60;
                break;
            default:
                colorId = R.color.level_70;
                break;
        }
        return ContextCompat.getColor(context, colorId);
    }
}
