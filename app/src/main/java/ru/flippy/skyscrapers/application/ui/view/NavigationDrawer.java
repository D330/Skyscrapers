package ru.flippy.skyscrapers.application.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ru.flippy.skyscrapers.BuildConfig;
import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.api.model.TotalData;

public class NavigationDrawer {

    public static final int PROFILE_ITEM = 0;
    public static final int CITY_ITEM = 1;
    public static final int MESSAGES_ITEM = 2;
    public static final int FRIENDS_ITEM = 3;
    public static final int QUESTS_ITEM = 4;
    public static final int SHOP_ITEM = 5;
    public static final int RATING_ITEM = 6;
    public static final int FORUM_ITEM = 7;
    public static final int CHAT_ITEM = 8;
    public static final int SETTINGS_ITEM = 9;
    public static final int EXIT_ITEM = 10;

    private Drawer drawer;
    private Context context;

    private CircleUserView userIcon;
    private ProgressBar levelProgress;
    private TextView balanceCoins, balanceDollars, level, nick;
    private TextView onlineCount, time;

    private OnDrawerClickListener drawerListener;

    public NavigationDrawer(final Activity activity, Toolbar toolbar) {
        this.context = activity;
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundColor(Color.WHITE)
                .withCloseOnClick(true)
                .withScrollToTopAfterClick(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(PROFILE_ITEM)
                                .withName(R.string.drawer_item_profile)
                                .withIcon(R.drawable.ic_user_man_white)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(CITY_ITEM)
                                .withName(R.string.drawer_item_city)
                                .withIcon(R.drawable.ic_drawer_city)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(FRIENDS_ITEM)
                                .withName(R.string.drawer_item_friends)
                                .withIcon(R.drawable.ic_drawer_friends)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(MESSAGES_ITEM)
                                .withName(R.string.drawer_item_messages)
                                .withIcon(R.drawable.ic_drawer_messages)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(QUESTS_ITEM)
                                .withName(R.string.drawer_item_quests)
                                .withIcon(R.drawable.ic_drawer_quests)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(SHOP_ITEM)
                                .withName(R.string.drawer_item_shop)
                                .withIcon(R.drawable.ic_drawer_shop)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(RATING_ITEM)
                                .withName(R.string.drawer_item_rating)
                                .withIcon(R.drawable.ic_drawer_rating)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(FORUM_ITEM)
                                .withName(R.string.drawer_item_forum)
                                .withIcon(R.drawable.ic_drawer_forum)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(CHAT_ITEM)
                                .withName(R.string.drawer_item_chat)
                                .withIcon(R.drawable.ic_drawer_chat)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(SETTINGS_ITEM)
                                .withName(R.string.drawer_item_settings)
                                .withIcon(R.drawable.ic_drawer_settings)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected),
                        new PrimaryDrawerItem()
                                .withIdentifier(EXIT_ITEM)
                                .withName(R.string.drawer_item_exit)
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_drawer_exit)
                )
                .withFooter(R.layout.drawer_footer)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof PrimaryDrawerItem) {
                            close();
                            drawerListener.onNavigationItemClick(((Number) drawerItem.getIdentifier()).intValue(), ((PrimaryDrawerItem) drawerItem).getName().getText(activity));
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .build();
        View header = drawer.getHeader();
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                drawerListener.onHeaderClick();
            }
        });
        userIcon = (CircleUserView) header.findViewById(R.id.drawer_header_user_icon);
        levelProgress = (ProgressBar) header.findViewById(R.id.drawer_header_level_progress);
        balanceCoins = (TextView) header.findViewById(R.id.drawer_header_balance_coins);
        balanceDollars = (TextView) header.findViewById(R.id.drawer_header_balance_dollars);
        level = (TextView) header.findViewById(R.id.drawer_header_level);
        nick = (TextView) header.findViewById(R.id.drawer_header_nick);

        View footer = drawer.getFooter();
        onlineCount = (TextView) footer.findViewById(R.id.drawer_footer_online);
        time = (TextView) footer.findViewById(R.id.drawer_footer_time);
        TextView app_version = (TextView) footer.findViewById(R.id.drawer_footer_app_version);
        app_version.setText(String.format(context.getString(R.string.drawer_footer_app_version), BuildConfig.VERSION_NAME));
        onlineCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                drawerListener.onFooterOnlineClick();
            }
        });
        footer.findViewById(R.id.drawer_footer_more_games).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                drawerListener.onFooterMoreGamesClick();
            }
        });
        footer.findViewById(R.id.drawer_footer_overmobile_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                drawerListener.onFooterOverMobileClick();
            }
        });
    }

    public void setTotalData(TotalData totalData) {
        userIcon.setUserData(totalData.getAuthUserData());
        levelProgress.setProgress(totalData.getLevelPercent());
        balanceCoins.setText(String.valueOf(totalData.getBalanceCoins()));
        balanceDollars.setText(String.valueOf(totalData.getBalanceDollars()));
        level.setText(String.format(context.getString(R.string.drawer_header_level), totalData.getAuthUserData().getLevel(), totalData.getLevelPercent()));
        nick.setText(totalData.getAuthUserData().getNick());
        onlineCount.setText(String.format(context.getString(R.string.drawer_footer_online), totalData.getOnlineCount()));
        time.setText(String.format(context.getString(R.string.drawer_footer_time), totalData.getTime().getHour(), totalData.getTime().getMinute()));
    }

    public void setOnDrawerListener(OnDrawerClickListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    public boolean isOpened() {
        return drawer.isDrawerOpen();
    }

    public void close() {
        drawer.closeDrawer();
    }

    public interface OnDrawerClickListener {
        void onHeaderClick();

        void onNavigationItemClick(int id, String title);

        void onFooterOnlineClick();

        void onFooterMoreGamesClick();

        void onFooterOverMobileClick();
    }
}