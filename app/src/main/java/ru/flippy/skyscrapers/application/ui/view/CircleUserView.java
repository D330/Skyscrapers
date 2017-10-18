package ru.flippy.skyscrapers.application.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.api.model.User;
import ru.flippy.skyscrapers.application.utils.ColorUtils;

public class CircleUserView extends FrameLayout {

    private ImageView icon;
    private View offline_mask;

    public CircleUserView(@NonNull Context context) {
        this(context, null);
    }

    public CircleUserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleUserView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.circle_user_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        icon = (ImageView) findViewById(R.id.circle_user_icon);
        offline_mask = findViewById(R.id.circle_user_offline_mask);
    }

    public void setUserData(User user) {
        GradientDrawable iconDrawable = new GradientDrawable();
        iconDrawable.setShape(GradientDrawable.OVAL);
        if (user.getLevel() / 10 == 5) {
            iconDrawable.setColor(Color.WHITE);
            iconDrawable.setStroke(2, Color.BLACK);
            icon.setImageResource(user.getSex() == User.sex.MAN ? R.drawable.ic_user_man_black : R.drawable.ic_user_man_black);
        } else {
            iconDrawable.setColor(ColorUtils.getLevelColor(getContext(), user.getLevel(), user.getSex()));
            iconDrawable.setStroke(0, Color.TRANSPARENT);
            icon.setImageResource(user.getSex() == User.sex.MAN ? R.drawable.ic_user_man_white : R.drawable.ic_user_man_white);
        }
        setBackground(iconDrawable);
        offline_mask.setVisibility(user.getOnline() == User.online.ONLINE ? View.GONE : View.VISIBLE);
    }
}
