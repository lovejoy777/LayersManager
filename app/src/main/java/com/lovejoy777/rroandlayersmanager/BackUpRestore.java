package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.lovejoy777.rroandlayersmanager.backups.CardBackupRestore;
import com.lovejoy777.rroandlayersmanager.backups.GridBackupRestore;

/**
 * Created by lovejoy777 on 30/03/15.
 */
public class BackUpRestore extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


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
                Intent iIntent = new Intent(this, CardBackupRestore.class);
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
                Intent iIntent = new Intent(this, GridBackupRestore.class);
                iIntent.putExtra("key1", SZP);
                startActivity(iIntent);

            } // ENDS SWVALUE ELSE
    } // ENDS ON CREATE
} // ENDS BackUpRestore CLASS



