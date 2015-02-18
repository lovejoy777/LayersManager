package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

/**
 * Created by lovejoy on 13/02/15.
 */
public class menu extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        RootTools.debugMode = true; //ON
        if (RootTools.isAccessGiven()) {
        // your app has been granted root access

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            Button buttonlayers = (Button) findViewById(R.id.buttonlayers);
            Button buttondllayers = (Button) findViewById(R.id.buttondllayers);

            buttonlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent("com.lovejoy777.rroandlayersmanager.LAYERS"));
                }
            });

            buttondllayers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/android/themes/0-themes-official-mega-rro-themes-t3011075")));


                }
            });


        }else {
            Toast.makeText(menu.this, "Your device doesn't seem to be rooted", Toast.LENGTH_LONG).show();
            Intent intent0 = new Intent();
            intent0.setClass(this, PlaystoreSuperUser.class);
            startActivity(intent0);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the menu/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent1 = new Intent();
                intent1.setClass(this, Settings.class);
                startActivity(intent1);
                break;

            case R.id.action_instructions:
                Intent intent2 = new Intent();
                intent2.setClass(this, Instructions.class);
                startActivity(intent2);
                break;

            case R.id.action_about:
                Intent intent3 = new Intent();
                intent3.setClass(this, About.class);
                startActivity(intent3);
                break;

            case R.id.action_changelog:
                Intent intent4 = new Intent();
                intent4.setClass(this, ChangeLog.class);
                startActivity(intent4);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
