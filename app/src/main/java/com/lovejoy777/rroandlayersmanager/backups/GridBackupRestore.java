package com.lovejoy777.rroandlayersmanager.backups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.lovejoy777.rroandlayersmanager.About;
import com.lovejoy777.rroandlayersmanager.BackUpRestore;
import com.lovejoy777.rroandlayersmanager.ChangeLog;
import com.lovejoy777.rroandlayersmanager.CustomGridAdapter;
import com.lovejoy777.rroandlayersmanager.DeleteLayers;
import com.lovejoy777.rroandlayersmanager.Instructions;
import com.lovejoy777.rroandlayersmanager.R;
import com.lovejoy777.rroandlayersmanager.Settings;
import com.lovejoy777.rroandlayersmanager.commands.RootCommands;
import com.lovejoy777.rroandlayersmanager.filepicker.FilePickerActivity;
import com.lovejoy777.rroandlayersmanager.menu;

/**
 * Created by lovejoy on 04/04/15.
 */
public class GridBackupRestore extends ActionBarActivity {
    static final String TAG = "CardManager";
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;

    final String startDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Overlays/Backup";
    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;

    GridView grid;

    String[] title = {
            "Backup",
            "File Chooser",
            "Restore",
            "Reboot",
    } ;

    String[] subTitle = {
            "All Installed Layers",
            "Select a Backup to Restore",
            "Selected Backup",
            "For Changes to Take Effect",
    } ;

    int[] imageId = {
            R.drawable.player,
            R.drawable.books,
            R.drawable.sync,
            R.drawable.power,
    };

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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_gridview_main);

        SharedPreferences prefs = getSharedPreferences("BackgroundColor",
                MODE_PRIVATE);
        int bgcolor = prefs.getInt("bgcolor", 1728053248);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.layout1);
        l1.setBackgroundColor(bgcolor);

        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.toolbar_container);
        l2.setBackgroundColor(bgcolor);

        // GET SZP STRING FOR TEXT VIEW
        Intent extras = getIntent();
        String SZP = null;
        if (extras != null) {
            SZP = extras.getStringExtra("key1");
        }
        TextView tv = (TextView) findViewById(R.id.tvlayerspath);
        tv.setText(SZP);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("BitSyko").withEmail("www.bitsyko.com").withIcon(getResources().getDrawable(R.drawable.ic_launcher));

         if (sw2value) { // IF SETTINGS SWITCH IS SET TO LIGHT

        // Create the AccountHeader
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(bgcolor)
                .withCompactStyle(true)
                .withSelectionListEnabledForSingleProfile(false)
                .withSelectionListEnabled(false)
                .withProfileImagesVisible(true)
                .addProfiles(profile)

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            //IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                    }
                })

                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer1
        result = new Drawer()

                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
               // .withDisplayBelowToolbar(true)
                .withSliderBackgroundColor(bgcolor)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        // new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(100).withCheckable(false),
                      //  new PrimaryDrawerItem().withName(R.string.drawer_item_manager).withIcon(R.drawable.ic_bitsyko_layers).withIdentifier(1).withCheckable(false),
                      //  new PrimaryDrawerItem().withName(R.string.drawer_item_backups).withIcon(R.drawable.ic_backup).withIdentifier(2).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_remove).withIcon(R.drawable.ic_delete).withIdentifier(3).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_about).withIdentifier(4).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_changelog).withIcon(R.drawable.ic_changelog).withIdentifier(5).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_instructions).withIcon(R.drawable.info).withIdentifier(6).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.settings).withIdentifier(7).withCheckable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_links_header),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_gplus).withIcon(R.drawable.bitsyko_g_plus).withIdentifier(8).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_xda).withIcon(R.drawable.xda).withIdentifier(9).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_forum).withIcon(R.drawable.ic_forums).withIdentifier(10).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_website).withIcon(R.drawable.ic_web).withIdentifier(11).withCheckable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 3) {
                           //     Intent intent = new Intent(GridBackupRestore.this, menu.class);
                           //     GridBackupRestore.this.startActivity(intent);
                          //  } else if (drawerItem.getIdentifier() == 2) {
                          //      Intent intent = new Intent(GridBackupRestore.this, BackUpRestore.class);
                          //      GridBackupRestore.this.startActivity(intent);
                          //  } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(GridBackupRestore.this, DeleteLayers.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(GridBackupRestore.this, About.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Intent intent = new Intent(GridBackupRestore.this, ChangeLog.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(GridBackupRestore.this, Instructions.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 7) {
                                Intent intent = new Intent(GridBackupRestore.this, Settings.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 8) {
                                launchgplus();
                            } else if (drawerItem.getIdentifier() == 9) {
                                launchxda();
                            } else if (drawerItem.getIdentifier() == 10) {
                                launchforum();
                            } else if (drawerItem.getIdentifier() == 11) {
                                launchwebsite();

                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // set the selection to the item with the identifier 0
        result.setSelectionByIdentifier(0, false);
        headerResult.setActiveProfile(profile);

         } else { // IF SETTINGS SWITCH IS SET TO DARK

        // Create the AccountHeader
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(bgcolor)
                .withCompactStyle(true)
                .withSelectionListEnabledForSingleProfile(false)
                .withSelectionListEnabled(false)
                .withProfileImagesVisible(true)
                .addProfiles(profile)

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            //IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                    }
                })

                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer1
        result = new Drawer()

                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowToolbar(true)
                .withSliderBackgroundColor(bgcolor)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        // new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(100).withCheckable(false),
                       // new PrimaryDrawerItem().withName(R.string.drawer_item_manager).withIcon(R.drawable.ic_bitsyko_layers_dark).withIdentifier(1).withCheckable(false),
                       // new PrimaryDrawerItem().withName(R.string.drawer_item_backups).withIcon(R.drawable.ic_backup_dark).withIdentifier(2).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_remove).withIcon(R.drawable.ic_delete_dark).withIdentifier(3).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_about_dark).withIdentifier(4).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_changelog).withIcon(R.drawable.ic_changelog_dark).withIdentifier(5).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_instructions).withIcon(R.drawable.info_dark).withIdentifier(6).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.settings_dark).withIdentifier(7).withCheckable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_links_header),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_gplus).withIcon(R.drawable.bitsyko_g_plus_dark).withIdentifier(8).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_xda).withIcon(R.drawable.xda_dark).withIdentifier(9).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_forum).withIcon(R.drawable.ic_forums_dark).withIdentifier(10).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_website).withIcon(R.drawable.ic_web_dark).withIdentifier(11).withCheckable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 3) {
                          //      Intent intent = new Intent(GridBackupRestore.this, menu.class);
                          //      GridBackupRestore.this.startActivity(intent);
                          //  } else if (drawerItem.getIdentifier() == 2) {
                         //       Intent intent = new Intent(GridBackupRestore.this, BackUpRestore.class);
                          //      GridBackupRestore.this.startActivity(intent);
                          //  } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(GridBackupRestore.this, DeleteLayers.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(GridBackupRestore.this, About.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Intent intent = new Intent(GridBackupRestore.this, ChangeLog.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(GridBackupRestore.this, Instructions.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 7) {
                                Intent intent = new Intent(GridBackupRestore.this, Settings.class);
                                GridBackupRestore.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 8) {
                                launchgplus();
                            } else if (drawerItem.getIdentifier() == 9) {
                                launchxda();
                            } else if (drawerItem.getIdentifier() == 10) {
                                launchforum();
                            } else if (drawerItem.getIdentifier() == 11) {
                                launchwebsite();

                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // set the selection to the item with the identifier 0
        result.setSelectionByIdentifier(0, false);
        headerResult.setActiveProfile(profile);

         } // ENDS SWVALUE ELSE

        CustomGridAdapter adapter = new CustomGridAdapter(GridBackupRestore.this, title, subTitle, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setVerticalSpacing(16);
        grid.setHorizontalSpacing(16);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(GridBackupRestore.this);
                    final EditText input = new EditText(GridBackupRestore.this);
                    alert.setIcon(R.drawable.ic_backup);
                    alert.setTitle("LAYERS BACKUP");
                    alert.setMessage("");
                    alert.setView(input);
                    input.setHint("enter backup name");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String backupname = input.getText().toString();

                            if (backupname.length() <= 1) {

                                Toast.makeText(GridBackupRestore.this, "INPUT A NAME", Toast.LENGTH_LONG).show();

                                finish();

                            } else {

                                File directory = new File("/vendor/overlay");
                                File[] contents = directory.listFiles();

                                // Folder is empty
                                if (contents.length == 0) {

                                    Toast.makeText(GridBackupRestore.this, "NOTHING TO BACKUP", Toast.LENGTH_LONG).show();

                                    finish();
                                }

                                // Folder contains files
                                else {

                                    try {
                                        // CREATES /SDCARD/OVERLAYS
                                        File dir = new File(Environment.getExternalStorageDirectory() + "/Overlays");
                                        if (!dir.exists() && !dir.isDirectory()) {
                                            dir.mkdir();
                                        }

                                        // CREATES /SDCARD/OVERLAYS/BACKUP
                                        File dir0 = new File(Environment.getExternalStorageDirectory() + "/Overlays/Backup");
                                        if (!dir0.exists() && !dir0.isDirectory()) {
                                            dir0.mkdir();
                                        }

                                        // CREATES /SDCARD/OVERLAYS/BACKUP/TEMP
                                        File dir1 = new File(Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp");
                                        if (!dir1.exists() && !dir1.isDirectory()) {
                                            dir1.mkdir();
                                        }


                                        // CREATES /SDCARD/OVERLAYS/BACKUP/BACKUPNAME
                                        File dir2 = new File(Environment.getExternalStorageDirectory() + "/Overlays/Backup/" + backupname);
                                        if (!dir2.exists() && !dir2.isDirectory()) {
                                            dir2.mkdir();
                                        }

                                        RootTools.remount("/system", "RW");

                                        // CHANGE PERMISSIONS OF /VENDOR/OVERLAY && /SDCARD/OVERLAYS/BACKUP
                                        CommandCapture command1 = new CommandCapture(0,
                                                "chmod -R 755 /vendor/overlay",
                                                "chmod -R 755 " + Environment.getExternalStorageDirectory() + "/Overlays/Backup/",
                                                "cp -fr /vendor/overlay " + Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp/");
                                        RootTools.getShell(true).add(command1);
                                        while (!command1.isFinished()) {
                                            Thread.sleep(1);
                                        }

                                        // ZIP OVERLAY FOLDER
                                        zipFolder(Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp/overlay", Environment.getExternalStorageDirectory() + "/Overlays/Backup/" + backupname + "/overlay.zip");

                                        // DELETE /SDCARD/OVERLAYS/BACKUP/TEMP FOLDER
                                         RootTools.deleteFileOrDirectory(Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp", true);

                                        // CHANGE PERMISSIONS OF /VENDOR/OVERLAY/ 666  && /VENDOR/OVERLAY 777 && /SDCARD/OVERLAYS/BACKUP/ 666
                                        CommandCapture command17 = new CommandCapture(0, "chmod -R 666 /vendor/overlay", "chmod 755 /vendor/overlay", "chmod -R 666" + Environment.getExternalStorageDirectory() + "/Overlays/Backup/");
                                        RootTools.getShell(true).add(command17);
                                        while (!command17.isFinished()) {
                                            Thread.sleep(1);
                                        }

                                        // CLOSE ALL SHELLS
                                        RootTools.closeAllShells();

                                        Toast.makeText(GridBackupRestore.this, "BACKUP COMPLETED", Toast.LENGTH_LONG).show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (RootDeniedException e) {
                                        e.printStackTrace();
                                    } catch (TimeoutException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    finish();
                                }
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.cancel();

                        }
                    });

                    alert.show();
                }

                if (position == 1) {

                    // LAUNCH RESTORE CHOOSER
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    // Set these depending on your use case. These are the defaults.
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false); // true = 1 | false = 2
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                    i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                    i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDir);

                    startActivityForResult(i, CODE_SD);
                }

                if (position == 2) {

                    restore();
                }

                if (position == 3) {

                    reboot(); // REBOOT DEVICE
                }
            }
        });
    } // ENDS ONCREATE


    public void launchforum() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forums.bitsyko.com/index.php?sid=8dabaae5fd44b00df498fa84e36f6924")));
    }

    public void launchwebsite() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bitsyko.com/")));
    }

    public void launchxda() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/android/apps-games/official-layers-bitsyko-apps-rro-t3012172")));
    }

    public void launchgplus() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/communities/102261717366580091389")));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if ((CODE_SD == requestCode || CODE_DB == requestCode) &&
                resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                    false)) {

                ArrayList<String> paths = data.getStringArrayListExtra(
                        FilePickerActivity.EXTRA_PATHS);
                StringBuilder sb = new StringBuilder();

                if (paths != null) {
                    for (String path : paths) {
                        if (path.startsWith("file://")) {
                            path = path.substring(7);
                            sb.append(path);
                            sb.append("\n");
                        }
                    }

                    String SZP = (sb.toString());
                    Intent iIntent = new Intent(this, GridBackupRestore.class);
                    iIntent.putExtra("key1", SZP);
                    startActivity(iIntent);

                }


            } else {

                    // Get the File path from the Uri
                    String SZP = (data.getData().toString());
                    if (SZP.startsWith("file://")) {
                        SZP = SZP.substring(7);
                        Intent iIntent = new Intent(this, GridBackupRestore.class);
                        iIntent.putExtra("key1", SZP);
                        startActivity(iIntent);
                    }

            }
        }
    }


    // COMMAND 3 REBOOT DEVICE

    public void restore() {

        Intent extras = getIntent();
        String SZP = extras.getStringExtra("key1");

        if (SZP != null) {

            try {

                RootTools.remount("/system/", "RW");

                // DELETE /VENDOR/OVERLAY
                RootTools.deleteFileOrDirectory("/vendor/overlay", true);

                // MK DIR /VENDOR/OVERLAY
                CommandCapture command3 = new CommandCapture(0, "mkdir /vendor/overlay");

                RootTools.getShell(true).add(command3);
                while (!command3.isFinished()) {
                    Thread.sleep(1);
                }

                // MK DIR /SDCARD/OVERLAYS/BACKUP/TEMP
                CommandCapture command4 = new CommandCapture(0, "mkdir" + Environment.getExternalStorageDirectory() + "/Overlays/Backup/Temp");

                RootTools.getShell(true).add(command4);
                while (!command4.isFinished()) {
                    Thread.sleep(1);
                }

                // MK DIR /SDCARD/OVERLAYS/BACKUP/TEMP/OVERLAY
                CommandCapture command5 = new CommandCapture(0, "mkdir" + Environment.getExternalStorageDirectory() + "/Overlays/Backup/Temp/overlay");

                RootTools.getShell(true).add(command5);
                while (!command5.isFinished()) {
                    Thread.sleep(1);
                }

                // CHANGE PERMISSIONS OF /VENDOR/OVERLAY && /SDCARD/OVERLAYS/BACKUP
                CommandCapture command6 = new CommandCapture(0, "chmod 755 /vendor/overlay", "chmod 755 " + Environment.getExternalStorageDirectory() + "/Overlays/Backup");
                RootTools.getShell(true).add(command6);
                while (!command6.isFinished()) {
                    Thread.sleep(1);
                }

                // UNZIP SZP TO /SDCARD/OVERLAYS/BACKUP/TEMP/OVERLAY FOLDER
                unzip(SZP, Environment.getExternalStorageDirectory() + "/Overlays/Backup/Temp/overlay");

                // MOVE /SDCARD/OVERLAYS/BACKUP/TEMP/OVERLAY TO /VENDOR/
                RootCommands.moveCopyRoot(Environment.getExternalStorageDirectory() + "/Overlays/Backup/Temp/overlay", "/vendor/");

                // DELETE /SDCARD/OVERLAYS/BACKUP/TEMP FOLDER
                RootTools.deleteFileOrDirectory(Environment.getExternalStorageDirectory() + "/Overlays/Backup/Temp", true);

                // CHANGE PERMISSIONS OF /VENDOR/OVERLAY/ 666  && /VENDOR/OVERLAY 777 && /SDCARD/OVERLAYS/BACKUP/ 666
                CommandCapture command7 = new CommandCapture(0, "chmod -R 666 /vendor/overlay", "chmod 755 /vendor/overlay", "chmod -R 666" + Environment.getExternalStorageDirectory() + "/Overlays/Backup");
                RootTools.getShell(true).add(command7);
                while (!command7.isFinished()) {
                    Thread.sleep(1);
                }

                // CLOSE ALL SHELLS
                RootTools.closeAllShells();

                Toast.makeText(GridBackupRestore.this, "RESTORE COMPLETED", Toast.LENGTH_LONG).show();

                finish();

                // LAUNCH LAYERS.CLASS
                overridePendingTransition(R.anim.back2, R.anim.back1);
                Intent iIntent = new Intent(this, menu.class);
                iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iIntent);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {

            Toast.makeText(GridBackupRestore.this, "SELECT A BACKUP TO RESTORE", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    public void reboot() {

        try {

            Toast.makeText(GridBackupRestore.this, "Rebooting", Toast.LENGTH_LONG).show();
            // MK DIR /SDCARD/OVERLAYS/BACKUP/TEMP
            CommandCapture command4 = new CommandCapture(0, "reboot");

            RootTools.getShell(true).add(command4);
            while (!command4.isFinished()) {
                Thread.sleep(1);

                finish();
            }

        } catch (RootDeniedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * **********************************************************************************************************
     * ZIP
     * ********
     * zip a zip file.
     *
     * @throws java.io.IOException *******************************************************************************
     */
    private static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    /**
     * **********************************************************************************************************
     * UNZIP UTIL
     * ************
     * Unzip a zip file.
     *
     * @throws java.io.IOException ******************************************************************************
     */
    public void unzip(String zipFile, String location) throws IOException {

        int size;
        byte[] buffer = new byte[1024];

        try {

            if (!location.endsWith("/")) {
                location += "/";
            }
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {

                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }
                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                        try {
                            while ((size = zin.read(buffer, 0, 1024)) != -1) {
                                fout.write(buffer, 0, size);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                            out.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
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
