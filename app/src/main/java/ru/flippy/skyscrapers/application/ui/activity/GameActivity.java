package ru.flippy.skyscrapers.application.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.application.ui.view.NavigationDrawer;

public class GameActivity extends AppCompatActivity {

    private NavigationDrawer drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewCompat.setElevation(toolbar, 10f);
        setSupportActionBar(toolbar);
        drawer = new NavigationDrawer(this, toolbar);
        drawer.setOnDrawerListener(new NavigationDrawer.OnDrawerClickListener() {
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
            public void onFooterMoreGamesClick() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://igrotop.mobi/"));
                startActivity(i);
            }

            @Override
            public void onFooterOverMobileClick() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://overmobile.ru/"));
                startActivity(i);
            }
        });
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