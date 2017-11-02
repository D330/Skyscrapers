package ru.flippy.skyscrapers.application.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.model.User;

public class CircleUserView extends FrameLayout {

    @BindView(R.id.circle_user_icon)
    ImageView icon;
    private View star;
    @BindView(R.id.circle_user_offline_mask)
    View offlineMask;

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
        ButterKnife.bind(this);
    }

    public void setUserData(User user) {

        star.setVisibility(user.getOnline() == User.online.ONLINE_STAR ? View.VISIBLE : View.GONE);
        offlineMask.setVisibility(user.getOnline() == User.online.OFFLINE ? View.VISIBLE : View.GONE);
    }
}
