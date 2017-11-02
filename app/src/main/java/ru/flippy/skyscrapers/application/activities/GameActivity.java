package ru.flippy.skyscrapers.application.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.application.view.NavigationDrawer;

public class GameActivity extends AppCompatActivity implements NavigationDrawer.OnDrawerListener {

    private NavigationDrawer drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        ViewCompat.setElevation(toolbar, 10f);
        setSupportActionBar(toolbar);
        drawer = new NavigationDrawer(this);
        drawer.setOnDrawerListener(this);
    }

    @Override
    public void onHeaderClick() {

    }

    @Override
    public void onNavigationItemClick(int id, String title) {

    }

    @Override
    public void onFooterOnlineClick() {

    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpened()) {
            drawer.close();
        } else {
            super.onBackPressed();
        }
    }
}