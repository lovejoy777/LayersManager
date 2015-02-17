package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

/**
 * Created by lovejoy on 21/10/14.
 */
public class Settings extends Activity implements View.OnClickListener {

    CheckBox cb;
    ImageButton b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        cb = (CheckBox) findViewById(R.id.checkBoxDark);
        b = (ImageButton) findViewById(R.id.saveButton);
        b.setOnClickListener(this);

        ImageButton xdaButton = (ImageButton) findViewById(R.id.xda);

        xdaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/android/apps-games/sion-flashable-zips-init-d-installer-t2992612/post57934168#post57934168")));

            }
        });
    }

    private void savePrefs(String key, boolean value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }



    @Override
    public void onClick(View view) {

        savePrefs("CHECKBOX", cb.isChecked());
        Toast.makeText(Settings.this, "Theme choice saved\nPlease restart Layers Manager", Toast.LENGTH_LONG).show();

    }

    private static boolean mShowHiddenFiles;
    private static boolean mRootAccess;
    public static String defaultdir;

    public static void updatePreferences(Context context) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);

        mShowHiddenFiles = p.getBoolean("displayhiddenfiles", true);
        mRootAccess = p.getBoolean("enablerootaccess", true);
        defaultdir = p.getString("defaultdir", Environment
                .getExternalStorageDirectory().getPath());

        rootAccess();
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

    public static boolean showHiddenFiles() {
        return mShowHiddenFiles;
    }

    public static boolean rootAccess() {
        return mRootAccess && RootTools.isAccessGiven();
    }


}
