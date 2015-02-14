package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by lovejoy on 05/10/14.
 */
public class Instructions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
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
