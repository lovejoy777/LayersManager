package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import java.io.DataOutputStream;
import java.io.IOException;


public class Layers extends Activity {

    static final String TAG = "sion";
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layers);
        Button buttonlayerschooser = (Button) findViewById(R.id.buttonlayerschooser);
        final Button layersinstallbutton = (Button) findViewById(R.id.layersinstallbutton);
        layersinstallbutton.setTag(1);
        layersinstallbutton.setText("Install Chosen layer");
        Button deletebutton = (Button) findViewById(R.id.deletebutton);
        Button deleteallbutton = (Button) findViewById(R.id.deleteallbutton);
        Button rebootbutton = (Button) findViewById(R.id.rebootbutton);
        Button deletelayerschooserbutton = (Button) findViewById(R.id.deletelayerschooserbutton);




        Intent extras = getIntent();
        // gets the string of source path for text veiw
        String sourcezippath = null;
        if (extras != null) {
            sourcezippath = extras.getStringExtra("key1");
        }
        TextView tv = (TextView) findViewById(R.id.tvlayerspath);
        tv.setText(sourcezippath);

        // FILE CHOOSER BUTTON
        buttonlayerschooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.sion.lovejoy777sa.sion.LAYERSCHOOSER"));
            }
        });


        // INSTALL BUTTON
        if (sourcezippath != null)
            //layersinstallbutton.setVisibility(View.VISIBLE);
            layersinstallbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //command1();
                    Intent extras = getIntent();

                    if (extras != null) {
                        String sourcezipname = extras.getStringExtra("key3");
                        String iszip = ".zip";

                        if (sourcezipname != null && !sourcezipname.isEmpty()) {
                            if (sourcezipname.endsWith(iszip)) {

                                oninstallClick();

                            }else {
                                Toast.makeText(Layers.this, "Invalid File", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        }else {
                            Toast.makeText(Layers.this, "Please Choose a File", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        // REBOOT BUTTON
        rebootbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                command4(); // reboot device
            }
        });

        // DELETE FILE CHOOSER BUTTON
        deletelayerschooserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent("com.sion.lovejoy777sa.sion.DELETELAYERSCHOOSER"));
            }
        });

        // DELETE FILE CHOOSER BUTTON BUTTON

        if (sourcezippath != null) {

            // Delete Chosen Layer Button
            deletebutton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    command3(); // delete chosen file

                }

            });


        }
        // Delete All Layers Button
        deleteallbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                command5(); // delete chosen file

            }

        });
    }

    private void oninstallClick() {
        Intent extras = getIntent();
        String SZN = extras.getStringExtra("key3");
        String SZP = extras.getStringExtra("key1");
        //String iszip = ".zip";

        Intent iIntent;
        iIntent = new Intent(this, WaitLayers.class);
        iIntent.putExtra("key1", SZP);
        iIntent.putExtra("key3", SZN);
        startActivity(iIntent);

        finish();
    }

    // COMMAND 3 DELETE CHOSEN FILE
    public void command3() {
        CheckBox deletecb = (CheckBox) findViewById(R.id.deletecb);

        Intent extras = getIntent();
        String SZP = extras.getStringExtra("key1");

        if (SZP.length() >= 1)
            if (deletecb.isChecked()) {
                RootTools.remount("/system", "RW");
                RootTools.deleteFileOrDirectory(SZP, true);
                Toast.makeText(Layers.this, "Delete Successful", Toast.LENGTH_LONG).show();
                RootTools.remount("/system", "RO");

            } else {
                Toast.makeText(Layers.this, "Confirm with checkbox", Toast.LENGTH_LONG).show();
                //finish();
            }
        finish();
    }

    // COMMAND 5 DELETE CHOSEN FILE
    public void command5() {


        String delall = "/vendor/overlay/";
        CheckBox deletecb = (CheckBox) findViewById(R.id.deletecb);


        if (deletecb.isChecked()) {
            RootTools.remount("/system", "RW");
            RootTools.deleteFileOrDirectory(delall, true);
            Toast.makeText(Layers.this, "Delete Successful", Toast.LENGTH_LONG).show();

            RootTools.remount("/system", "RO");
        } else {
            Toast.makeText(Layers.this, "Confirm with checkbox", Toast.LENGTH_LONG).show();
            //finish();
        }
        finish();
    }

    // COMMAND 4, REBOOT DEVICE

    public void command4(){

        try {
            Toast.makeText(Layers.this, "Rebooting", Toast.LENGTH_LONG).show();
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream std2in = new DataOutputStream(proc.getOutputStream());
            //from here all commands are executed with su permissions
            std2in.writeBytes("-c\n");
            std2in.flush();
            std2in.writeBytes("reboot\n");
            std2in.flush();
            std2in.close();
            proc.waitFor();

            finish();
        } catch (InterruptedException e) {
            Log.e(TAG, "reboot waitfor", e);
        } catch (IOException e) {
            Log.e(TAG, "reboot runtime", e);
        }
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
