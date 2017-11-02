package ru.flippy.skyscrapers.application.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import ru.flippy.skyscrapers.sdk.api.model.TotalData;

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
    private OnDrawerListener drawerListener;

    private CircleUserView userIcon;
    private TextView nick;
    private TextView level;
    private ProgressBar levelProgress;
    private TextView balanceCoins;
    private TextView balanceDollars;

    private TextView onlineCount;
    private TextView time;
    private TextView appVersion;

    private PrimaryDrawerItem makeItem(long identifier, int nameRes, int iconRes) {
        return new PrimaryDrawerItem()
                .withIdentifier(identifier)
                .withName(nameRes)
                .withIcon(iconRes)
                .withIconTintingEnabled(true)
                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                .withSelectedTextColorRes(R.color.drawer_navigation_item_selected)
                .withSelectedIconColorRes(R.color.drawer_navigation_item_selected);
    }

    public NavigationDrawer(@NonNull final Activity activity) {
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar((Toolbar) activity.findViewById(R.id.toolbar))
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.drawer_header)
                .withFooter(R.layout.drawer_footer)
                .addDrawerItems(
                        makeItem(PROFILE_ITEM, R.string.drawer_item_profile, R.drawable.ic_user_man_black),
                        makeItem(CITY_ITEM, R.string.drawer_item_city, R.drawable.ic_drawer_city),
                        makeItem(FRIENDS_ITEM, R.string.drawer_item_friends, R.drawable.ic_drawer_friends),
                        makeItem(MESSAGES_ITEM, R.string.drawer_item_messages, R.drawable.ic_drawer_messages),
                        makeItem(QUESTS_ITEM, R.string.drawer_item_quests, R.drawable.ic_drawer_quests),
                        makeItem(SHOP_ITEM, R.string.drawer_item_shop, R.drawable.ic_drawer_shop),
                        makeItem(RATING_ITEM, R.string.drawer_item_rating, R.drawable.ic_drawer_rating),
                        makeItem(FORUM_ITEM, R.string.drawer_item_forum, R.drawable.ic_drawer_forum),
                        makeItem(CHAT_ITEM, R.string.drawer_item_chat, R.drawable.ic_drawer_chat),
                        makeItem(SETTINGS_ITEM, R.string.drawer_item_settings, R.drawable.ic_drawer_settings),
                        new PrimaryDrawerItem()
                                .withIdentifier(EXIT_ITEM)
                                .withName(R.string.drawer_item_exit)
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_drawer_exit)
                                .withIconTintingEnabled(true)
                                .withDisabledTextColorRes(R.color.drawer_navigation_item_disabled)
                                .withDisabledIconColorRes(R.color.drawer_navigation_item_disabled)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerListener != null && drawerItem instanceof PrimaryDrawerItem) {
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
        userIcon = header.findViewById(R.id.drawer_header_user_icon);
        levelProgress = header.findViewById(R.id.drawer_header_level_progress);
        balanceCoins = header.findViewById(R.id.drawer_header_balance_coins);
        balanceDollars = header.findViewById(R.id.drawer_header_balance_dollars);
        level = header.findViewById(R.id.drawer_header_level);
        nick = header.findViewById(R.id.drawer_header_nick);

        View footer = drawer.getFooter();
        onlineCount = footer.findViewById(R.id.drawer_footer_online);
        time = footer.findViewById(R.id.drawer_footer_time);
        appVersion = footer.findViewById(R.id.drawer_footer_app_version);
        appVersion.setText(String.format(activity.getString(R.string.drawer_footer_app_version), BuildConfig.VERSION_NAME));
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
                openURL("http://overmobile.ru/");
            }
        });
        footer.findViewById(R.id.drawer_footer_overmobile_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                openURL("http://igrotop.mobi/");
            }
        });
    }

    private void openURL(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public void updateData(@NonNull TotalData totalData) {
        userIcon.setUserData(totalData.getAuthUserData());
        levelProgress.setProgress(totalData.getLevelPercent());
        balanceCoins.setText(String.valueOf(totalData.getBalanceCoins()));
        balanceDollars.setText(String.valueOf(totalData.getBalanceDollars()));
        level.setText(String.format(context.getString(R.string.drawer_header_level), totalData.getAuthUserData().getLevel(), totalData.getLevelPercent()));
        nick.setText(totalData.getAuthUserData().getNick());
        onlineCount.setText(String.format(context.getString(R.string.drawer_footer_online), totalData.getOnlineCount()));
        time.setText(String.format(context.getString(R.string.drawer_footer_time), totalData.getTime().getHour(), totalData.getTime().getMinute()));
    }

    public void setOnDrawerListener(@NonNull OnDrawerListener listener) {
        drawerListener = listener;
    }

    public boolean isOpened() {
        return drawer.isDrawerOpen();
    }

    public void close() {
        drawer.closeDrawer();
    }

    public interface OnDrawerListener {

        void onHeaderClick();

        void onNavigationItemClick(int id, String title);

        void onFooterOnlineClick();
    }
}