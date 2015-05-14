package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.lovejoy777.rroandlayersmanager.managers.CardManager;
import com.lovejoy777.rroandlayersmanager.managers.GridManager;
import com.stericson.RootTools.RootTools;

/**
 * Created by lovejoy on 13/02/15.
 */
public class menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // IF ROOT ACCESS IS GIVEN / ELSE LAUNCH PLAYSTORE FOR SUPERUSER APP
        if (!RootTools.isAccessGiven()) {
            Toast.makeText(menu.this, "Your device doesn't seem to be rooted", Toast.LENGTH_LONG).show();
            Intent intent0 = new Intent();
            intent0.setClass(this, PlaystoreSuperUser.class);
            startActivity(intent0);
            finish();

        } else {

            // GET SHARED PREFERENCES FOR SWITCH1 IN SETTINGS FOR GRID OR CARD VIEW
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            boolean swvalue = sp.getBoolean("switch1", false);

            if (swvalue) {

                // GET SZP STRING FOR TEXT VIEW
                Intent extras = getIntent();
                String SZP = null;
                if (extras != null) {
                    SZP = extras.getStringExtra("key1");
                }

                // ADD SZP && LAUNCH CARD BUTTON VIEW
                Intent iIntent = new Intent(this, CardManager.class);
                iIntent.putExtra("key1", SZP);
                startActivity(iIntent);

            } else {

                // GET SZP STRING FOR TEXT VIEW
                Intent extras = getIntent();
                String SZP = null;
                if (extras != null) {
                    SZP = extras.getStringExtra("key1");
                }

                // ADD SZP && LAUNCH GRID BUTTON VIEW
                Intent iIntent = new Intent(this, GridManager.class);
                iIntent.putExtra("key1", SZP);
                startActivity(iIntent);

            } // ENDS SWVALUE ELSE
        } // ENDS SUPERUSER ELSE
    } // ENDS ON CREATE
} // ENDS menu CLASS

