package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.lovejoy777.rroandlayersmanager.commands.RootCommands;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import util.Basename;

/**
 * Created by lovejoy on 05/02/15.
 */
public class WaitLayers extends Activity {

    private Handler mHandler = new Handler();
    static final String TAG = "layers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        //makeVOfolder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitlayers);

       // makeVOfolder();

        Intent extras = getIntent();

        if (extras != null) {
            String SZN = extras.getStringExtra("key3");
            String iszip = ".zip";

            if (SZN.endsWith(iszip)) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        initcommand1();
                    }
                }, 2000);

            } else {
                Toast.makeText(WaitLayers.this, "Invalid File", Toast.LENGTH_LONG).show();

            }
        }
    }

    // COMMAND 1 make temp dirs copy & move & unzip & mount rw
    public void initcommand1() {



        Intent extras = getIntent();

        if (extras != null) {

            String SZN = extras.getStringExtra("key3");
            String SZP = extras.getStringExtra("key1");
            Basename newbasename = new Basename(SZN, '/', '.');
            String newname = "" + newbasename.basename();
            String siondata = getApplicationInfo().dataDir + "/overlay";
            String mkdirperm = "mkdir -p /vendor/overlay";
            String overlaypath = "/vendor";
            String overlay = "overlay";


            //IF SOURCE ZIP NAME LENGTH IS LESS THAN 1 CHAR DO THIS.
            if (SZN.length() <= 1) {
                Toast.makeText(WaitLayers.this, "Invalid file", Toast.LENGTH_LONG).show();

                //IF SOURCE ZIP NAME LENGTH IS MORE THAN 1 CHAR DO THIS.
            } else {

                //RootCommands.DeleteFileRoot(getApplicationInfo().dataDir + "/overlays");

                File dir = new File(getApplicationInfo().dataDir + "/overlay/");

                // DELETES PREVIOUS TEMP LAYERS
                if (dir.exists() && dir.isDirectory()) {
                    RootCommands.DeleteFileRoot(getApplicationInfo().dataDir + "/overlay");
                }

                try {
                    // UNZIP & MOVE TO SION DATA OVERLAY FOLDER
                    unzip(SZP, siondata);

                    // CHANGE PERMISSIONS OF UNZIPPED FOLDER & FILES
                    CommandCapture command = new CommandCapture(0, "chmod -R 666 " + siondata + "/");
                    RootTools.getShell(true).add(command);
                    while (!command.isFinished()) {
                        Thread.sleep(1);
                    }

                    RootTools.remount("/system", "RW");

                    // CHANGE PERMISSIONS OF UNZIPPED FOLDER & FILES
                    CommandCapture command1 = new CommandCapture(0, "mkdir /vendor/overlay");
                    RootTools.getShell(true).add(command1);
                    while (!command1.isFinished()) {
                        Thread.sleep(1);
                    }

                    // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
                    CommandCapture command2 = new CommandCapture(0, "chmod -R 777 /vendor/overlay");
                    RootTools.getShell(true).add(command2);
                    while (!command2.isFinished()) {
                        Thread.sleep(1);
                    }

                    // COPY NEW FILES TO /VENDOR/OVERLAY FOLDER
                    RootCommands.moveCopyRoot(siondata + "/", overlaypath);

                    // DELETE SIONS COPY OF LAST INSTALLED LAYERS>APK'S
                    //SimpleUtils.deleteTarget(siondata);

                    // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
                    CommandCapture command5 = new CommandCapture(0, "chmod -R 666 /vendor/overlay");
                    RootTools.getShell(true).add(command5);
                    while (!command5.isFinished()) {
                        Thread.sleep(1);
                    }

                    // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER BACK TO 777
                    CommandCapture command6 = new CommandCapture(0, "chmod 777 /vendor/overlay");
                    RootTools.getShell(true).add(command6);
                    while (!command6.isFinished()) {
                        Thread.sleep(1);
                        RootTools.remount("/system", "RO");
                    }


                    // CLOSE ALL SHELLS
                    RootTools.closeAllShells();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RootDeniedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }

        }
    }

    /*************************************************************************************************************
     *                                      UNZIP UTIL
     *                                     ************
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws java.io.IOException
     ***************************************************************************************************************/
    public void unzip(String zipFile, String location) throws IOException {

        int size;
        byte[] buffer = new byte[1024];

        try {

            if ( !location.endsWith("/") ) {
                location += "/";
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {

                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }
                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                        try {
                            while ( (size = zin.read(buffer, 0, 1024)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                            out.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void LoadPrefs() {
        //cb = (CheckBox) findViewById(R.id.checkBoxDark);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);
        }
    }


}
