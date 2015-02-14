package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by lovejoy777 on 15/12/13.
 */
public class ChangeLog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);

        ImageButton xdaButton = (ImageButton) findViewById(R.id.xdaButton1);
        xdaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/android/apps-games/official-layers-bitsyko-apps-rro-t3012172")));

            }
        });
    }

    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);
        }
    }
}
