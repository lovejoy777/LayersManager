package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by lovejoy777 on 15/12/13.
 */
public class ChangeLog extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // GET SHARED PREFERENCES FOR SWITCH1 IN SETTINGS FOR WHITE OR BLACK TEXT
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sw2value = sp.getBoolean("switch2", true);

        if (sw2value) {

            setTheme(R.style.white_text);

        } else {

            setTheme(R.style.black_text);

        } // ENDS SWVALUE ELSE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("BackgroundColor",
                MODE_PRIVATE);
        int bgcolor = prefs.getInt("bgcolor", 1728053248);

        ScrollView l1 = (ScrollView) findViewById(R.id.layout1);
        l1.setBackgroundColor(bgcolor);

        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.toolbar_container);
        l2.setBackgroundColor(bgcolor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.back2, R.anim.back1);
            return true;
        }
        return false;
    }
}

