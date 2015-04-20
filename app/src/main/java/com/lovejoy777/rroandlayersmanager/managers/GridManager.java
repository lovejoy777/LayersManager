package com.lovejoy777.rroandlayersmanager.managers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import com.lovejoy777.rroandlayersmanager.About;
import com.lovejoy777.rroandlayersmanager.BackUpRestore;
import com.lovejoy777.rroandlayersmanager.ChangeLog;
import com.lovejoy777.rroandlayersmanager.CustomGridAdapter;
import com.lovejoy777.rroandlayersmanager.DeleteLayers;
import com.lovejoy777.rroandlayersmanager.GridViewActivity;
import com.lovejoy777.rroandlayersmanager.Instructions;
import com.lovejoy777.rroandlayersmanager.R;
import com.lovejoy777.rroandlayersmanager.Settings;
import com.lovejoy777.rroandlayersmanager.commands.RootCommands;
import com.lovejoy777.rroandlayersmanager.filepicker.FilePickerActivity;
import com.lovejoy777.rroandlayersmanager.menu;

/**
 * Created by lovejoy on 03/04/15.
 */
public class GridManager extends ActionBarActivity {
    static final String TAG = "CardManager";
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;

    final String startDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Overlays";
    final String startDirInstalled = "/vendor/overlay";
    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;



    GridView grid;

    String[] title = {
            "File Chooser",
            "Install",
            "Installed",
            "Reboot",
    } ;

    String[] subTitle = {
            "Pick a Layer to Install",
            "Your Chosen Layer",
            "Already Installed Layers",
            "For Changes to Take Effect",
    } ;

    int[] imageId = {
            R.drawable.books,
            R.drawable.add,
            R.drawable.chart,
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
                .withSliderBackgroundColor(bgcolor)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowToolbar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        // new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(100).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_manager).withIcon(R.drawable.ic_bitsyko_layers).withIdentifier(1).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_backups).withIcon(R.drawable.ic_backup).withIdentifier(2).withCheckable(false),
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
                            if (drawerItem.getIdentifier() == 1) {
                                Intent intent = new Intent(GridManager.this, menu.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(GridManager.this, BackUpRestore.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(GridManager.this, DeleteLayers.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(GridManager.this, About.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Intent intent = new Intent(GridManager.this, ChangeLog.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(GridManager.this, Instructions.class);
                                GridManager.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 7) {
                                Intent intent = new Intent(GridManager.this, Settings.class);
                                GridManager.this.startActivity(intent);
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
                    .withSliderBackgroundColor(bgcolor)
                    .withActionBarDrawerToggleAnimated(true)
                    .withDisplayBelowToolbar(true)
                    .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                    .addDrawerItems(
                            // new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(100).withCheckable(false),
                            new PrimaryDrawerItem().withName(R.string.drawer_item_manager).withIcon(R.drawable.ic_bitsyko_layers_dark).withIdentifier(1).withCheckable(false),
                            new PrimaryDrawerItem().withName(R.string.drawer_item_backups).withIcon(R.drawable.ic_backup_dark).withIdentifier(2).withCheckable(false),
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
                                if (drawerItem.getIdentifier() == 1) {
                                    Intent intent = new Intent(GridManager.this, menu.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 2) {
                                    Intent intent = new Intent(GridManager.this, BackUpRestore.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 3) {
                                    Intent intent = new Intent(GridManager.this, DeleteLayers.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 4) {
                                    Intent intent = new Intent(GridManager.this, About.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 5) {
                                    Intent intent = new Intent(GridManager.this, ChangeLog.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 6) {
                                    Intent intent = new Intent(GridManager.this, Instructions.class);
                                    GridManager.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 7) {
                                    Intent intent = new Intent(GridManager.this, Settings.class);
                                    GridManager.this.startActivity(intent);
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


        CustomGridAdapter adapter = new CustomGridAdapter(GridManager.this, title, subTitle, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setVerticalSpacing(16);
        grid.setHorizontalSpacing(16);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {

                    //INSTALL CHOOSER
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    // Set these depending on your use case. These are the defaults.
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false); // true = 1 | false = 2
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                    i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                    i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDir);

                    startActivityForResult(i, CODE_SD);
                }

                if (position == 1) {

                    Intent extras = getIntent();
                  //  if (extras != null) {
                        String SZP = extras.getStringExtra("key1");
                        if (SZP != null && !SZP.isEmpty()) {

                            // LAUNCH INSTALLCOMMAND
                            installcommand();
                        }
                   // }
                }

                if (position == 2) {

                    //INSTALLED CHOOSER
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    // Set these depending on your use case. These are the defaults.
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false); // false =
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                    i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                    i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDirInstalled);

                    startActivityForResult(i, CODE_SD);
                }

                if (position == 3) {

                    rebootcommand(); // REBOOT DEVICE
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
                // 1
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
                    Intent iIntent = new Intent(this, GridManager.class);
                    iIntent.putExtra("key1", SZP);
                    startActivity(iIntent);

                }


            } else {
                // 2
                    // Get the File path from the Uri
                    String SZP = (data.getData().toString());
                    if (SZP.startsWith("file://")) {
                        SZP = SZP.substring(7);
                        Intent iIntent = new Intent(this, GridManager.class);
                        iIntent.putExtra("key1", SZP);
                        startActivity(iIntent);
                    }

            }
        }
    }

    public void installcommand() {


        Intent extras = getIntent();

        if (extras != null) {


            String SZP = extras.getStringExtra("key1");
            String layersdata = getApplicationInfo().dataDir + "/overlay";
            String previewimageszip = getApplicationInfo().dataDir + "/overlay/previewimages.zip";

            String iszip = ".zip";
            String isapk = ".apk";

            //IF SZP IS LESS THAN 1 CHAR DO THIS.
            if (SZP.length() <= 1) {

                Toast.makeText(GridManager.this, "CHOOSE A FILE", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(GridManager.this, "installing " + SZP, Toast.LENGTH_LONG).show();

                // DELETES /DATA/DATA/OVERLAY
                File dir = new File(getApplicationInfo().dataDir + "/overlay/");
                if (dir.exists() && dir.isDirectory()) {

                    RootCommands.DeleteFileRoot(getApplicationInfo().dataDir + "/overlay");
                }

                // IF SZP ENDS WITH .ZIP
                if (SZP.endsWith(iszip)) {

                    try {
                        // UNZIP & MOVE TO LAYERS DATA OVERLAY FOLDER
                        unzip(SZP, layersdata);

                        // CHANGE PERMISSIONS 755
                        CommandCapture command = new CommandCapture(0, "chmod -R 755 " + layersdata);
                        RootTools.getShell(true).add(command);
                        while (!command.isFinished()) {
                            Thread.sleep(1);
                        }

                        // IF PREVIEWIMAGES.ZIP EXISTS
                        File dir1 = new File(previewimageszip);
                        if (dir1.exists()) {

                            // INSTALL WITH PREVIEWS
                            installzipwithpreviews();

                            finish();
                        }

                        // IF PREVIEWIMAGES.ZIP DOESNT EXISTS DO THIS
                        File dir2 = new File(previewimageszip);
                        if (!dir2.exists()) {

                            // INSTALL NO PREVIEWS
                            installzipnopreviews();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (RootDeniedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // IF SZP ENDS WITH APK INSTALL APK
                if (SZP.endsWith(isapk)) {

                    // INSTALL APK'S
                    installapk();
                }
            }
        } else {
            Toast.makeText(GridManager.this, "pick a Layer", Toast.LENGTH_LONG).show();

        }
    } // ENDS COMMAND 1


    // COMMAND 1, REBOOT DEVICE
    public void rebootcommand() {

        try {

            Toast.makeText(GridManager.this, "Rebooting", Toast.LENGTH_LONG).show();
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

    public void installzipwithpreviews() {

        Intent extras = getIntent();

        String SZP = extras.getStringExtra("key1");
        String siondataprev = getApplicationInfo().dataDir + "/overlay/previewimages.zip";
        String layerspreviewssd = Environment.getExternalStorageDirectory().getAbsolutePath() + "/layerspreviews";

        try {


            // CHANGE PERMISSIONS OF FINAL /SDCARD/LAYERSPREVIEWS FOLDER & FILES TO 777
            CommandCapture command22 = new CommandCapture(0, "chmod -R 777 " + Environment.getExternalStorageDirectory() + "/layerspreviews");
            RootTools.getShell(true).add(command22);
            while (!command22.isFinished()) {
                Thread.sleep(1);
            }

            // DELETES /SDCARD/LAYERSPREVIEWS
            File dir = new File(Environment.getExternalStorageDirectory() + "/layerspreviews/previewimages/");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }

            // UNZIP PREVIEWS TO SDCARD
            unzip(siondataprev, layerspreviewssd);

            // CLOSE ALL SHELLS
            RootTools.closeAllShells();

            finish();

            // LAUNCH GRIDVIEW
            overridePendingTransition(R.anim.back2, R.anim.back1);
            Intent iIntent = new Intent(this, GridViewActivity.class);
            iIntent.putExtra("key1", SZP);
            iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(iIntent);

            // finish();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void installzipnopreviews() {

        String layersdata = getApplicationInfo().dataDir + "/overlay";
        String overlaypath = "/vendor";

        try {

            // CHANGE PERMISSIONS OF UNZIPPED FOLDER & FILES
            CommandCapture command = new CommandCapture(0, "chmod -R 666 " + layersdata + "/");
            RootTools.getShell(true).add(command);
            while (!command.isFinished()) {
                Thread.sleep(1);
            }

            // MOUNT /SYSTEM RW
            RootTools.remount("/system", "RW");

            // MK DIR /VENDOR/OVERLAY
            CommandCapture command1 = new CommandCapture(0, "mkdir /vendor/overlay");
            RootTools.getShell(true).add(command1);
            while (!command1.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 777
            CommandCapture command2 = new CommandCapture(0, "chmod -R 777 /vendor/overlay");
            RootTools.getShell(true).add(command2);
            while (!command2.isFinished()) {
                Thread.sleep(1);
            }

            // COPY NEW FILES TO /VENDOR/OVERLAY FOLDER
            RootCommands.moveCopyRoot(layersdata + "/", overlaypath);

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
            CommandCapture command3 = new CommandCapture(0, "chmod -R 666 /vendor/overlay");
            RootTools.getShell(true).add(command3);
            while (!command3.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER BACK TO 777
            CommandCapture command4 = new CommandCapture(0, "chmod 777 /vendor/overlay");
            RootTools.getShell(true).add(command4);
            while (!command4.isFinished()) {
                Thread.sleep(1);
                // RootTools.remount("/system", "RO");
            }

            // CLOSE ALL SHELLS
            RootTools.closeAllShells();
            //  Toast.makeText(WaitLayers.this, "INSTALL FINISHED", Toast.LENGTH_LONG).show();

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

    public void installapk() {

        Intent extras = getIntent();
        String SZP = extras.getStringExtra("key1");
        String layersdata = getApplicationInfo().dataDir + "/overlay";
        String overlaypath = "/vendor";

        try {

            // MK DIR SIONDATA
            CommandCapture command1 = new CommandCapture(0, "mkdir " + layersdata);
            RootTools.getShell(true).add(command1);
            while (!command1.isFinished()) {
                Thread.sleep(1);
            }

            // ELSE NOT A .ZIP THEN COPY FILE TO SIONDATA
            CommandCapture command5 = new CommandCapture(0, "cp -f " + SZP + " " + layersdata + "/");
            RootTools.getShell(true).add(command5);
            while (!command5.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF UNZIPPED FOLDER & FILES
            CommandCapture command6 = new CommandCapture(0, "chmod -R 666 " + layersdata + "/");
            RootTools.getShell(true).add(command6);
            while (!command6.isFinished()) {
                Thread.sleep(1);
            }
            // MOUNT /SYSTEM RW
            RootTools.remount("/system", "RW");

            // CHANGE PERMISSIONS OF UNZIPPED FOLDER & FILES
            CommandCapture command7 = new CommandCapture(0, "mkdir /vendor/overlay");
            RootTools.getShell(true).add(command7);
            while (!command7.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
            CommandCapture command8 = new CommandCapture(0, "chmod -R 777 /vendor/overlay");
            RootTools.getShell(true).add(command8);
            while (!command8.isFinished()) {
                Thread.sleep(1);
            }

            // COPY NEW FILES TO /VENDOR/OVERLAY FOLDER
            RootCommands.moveCopyRoot(layersdata + "/", overlaypath);

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
            CommandCapture command9 = new CommandCapture(0, "chmod -R 666 /vendor/overlay");
            RootTools.getShell(true).add(command9);
            while (!command9.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER BACK TO 777
            CommandCapture command10 = new CommandCapture(0, "chmod 777 /vendor/overlay");
            RootTools.getShell(true).add(command10);
            while (!command10.isFinished()) {
                Thread.sleep(1);
                RootTools.remount("/system", "RO");
            }

            // CLOSE ALL SHELLS
            RootTools.closeAllShells();
            //  Toast.makeText(WaitLayers.this, "INSTALL SUCCESSFUL", Toast.LENGTH_LONG).show();

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

    /**
     * **********************************************************************************************************
     * UNZIP UTIL
     * ************
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile  Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws java.io.IOException *************************************************************************************************************
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
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.back2, R.anim.back1);
        }
    }
} // ENDS GRIDMANAGER
