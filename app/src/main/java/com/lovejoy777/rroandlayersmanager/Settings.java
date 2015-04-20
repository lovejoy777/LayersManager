package com.lovejoy777.rroandlayersmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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

/**
 * Created by lovejoy on 21/10/14.
 */
public class Settings extends ActionBarActivity {

    private static final int PROFILE_SETTING = 0;

    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;

    public static String defaultdir;
    private static boolean mShowHiddenFiles;
    private static boolean mRootAccess;
    private Switch mySwitch1;
    private Switch mySwitch2;


    public static void updatePreferences(Context context) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);

        mShowHiddenFiles = p.getBoolean("displayhiddenfiles", true);
        mRootAccess = p.getBoolean("enablerootaccess", true);
        defaultdir = p.getString("defaultdir", Environment
                .getExternalStorageDirectory().getPath());

        rootAccess();
    }

    public static boolean showHiddenFiles() {
        return mShowHiddenFiles;
    }

    public static boolean rootAccess() {
        return mRootAccess && RootTools.isAccessGiven();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // GET SHARED PREFERENCES FOR SWITCH1 IN SETTINGS FOR WHITE OR BLACK TEXT
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sw2value = sp.getBoolean("switch2", true);

        if (sw2value) {

            setTheme(R.style.white_text);

        } else {

            setTheme(R.style.black_text);

        } // ENDS SWVALUE ELSE

        setContentView(R.layout.settings);


        Intent extras = getIntent();
        String hexcol = null;
        if (extras != null) {
            hexcol = extras.getStringExtra("hexcol");

        }

        EditText tv = (EditText) findViewById(R.id.textView8);
        tv.setText(hexcol);

        SharedPreferences prefs = getSharedPreferences("BackgroundColor",
                MODE_PRIVATE);
        int bgcolor = prefs.getInt("bgcolor", 1728053248);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.layout1);
        l1.setBackgroundColor(bgcolor);

        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.toolbar_container);
        l2.setBackgroundColor(bgcolor);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("BitSyko").withEmail("www.bitsyko.com").withIcon(getResources().getDrawable(R.drawable.ic_launcher));

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
                                Intent intent = new Intent(Settings.this, menu.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(Settings.this, BackUpRestore.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(Settings.this, DeleteLayers.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(Settings.this, About.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Intent intent = new Intent(Settings.this, ChangeLog.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(Settings.this, Instructions.class);
                                Settings.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 7) {
                                Intent intent = new Intent(Settings.this, Settings.class);
                                Settings.this.startActivity(intent);
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


        mySwitch1 = (Switch) findViewById(R.id.switch1);
        //set the switch to ON
        mySwitch1.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    savePrefs("switch1", true);
                    Toast.makeText(Settings.this, "CardView", Toast.LENGTH_SHORT).show();
                }else{

                    savePrefs("switch1", false);
                    Toast.makeText(Settings.this, "GridView", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mySwitch2 = (Switch) findViewById(R.id.switch2);
        //set the switch to ON
        mySwitch2.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    savePrefs("switch2", true);
                    Toast.makeText(Settings.this, "Light Theme", Toast.LENGTH_SHORT).show();
                }else{

                    savePrefs("switch2", false);
                    Toast.makeText(Settings.this, "Dark Theme", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // BUTTON 1 open colorpicker
        Button button1 = (Button) findViewById(R.id.button1);

        // PICK a COLOR BUTTON
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("bs.layersmanager.COLORPICKER"));

            }
        });

        // BUTTON 2 SET COLOR
        Button button2 = (Button) findViewById(R.id.button2);

        // SET BUTTON
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText tv = (EditText) findViewById(R.id.textView8);

                String hexbgcolor = tv.getText().toString();

                int bgcolor = Color.parseColor(hexbgcolor);

                backgroundColor(bgcolor);

                Toast.makeText(Settings.this, "Color Set", Toast.LENGTH_LONG).show();

                // finish();


            }
        });

        // BUTTON 3 open menu
        Button button3 = (Button) findViewById(R.id.button3);

        // RESTART APP BUTTON
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                Toast.makeText(Settings.this, "Set", Toast.LENGTH_LONG).show();

                // finish();


            }
        });

        // loading grid or card prefs
        loadPrefs();

    }

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

    private void loadPrefs() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean swvalue = sp.getBoolean("switch1", false);
        if (swvalue){

            mySwitch1.setChecked(true);
        }else{

            mySwitch1.setChecked(false);
        }

        boolean sw2value = sp.getBoolean("switch2", true);
        if (sw2value){

            mySwitch2.setChecked(true);
        }else{

            mySwitch2.setChecked(false);
        }

    }

    private void backgroundColor(Integer bgcolor) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = getSharedPreferences("BackgroundColor", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putInt("bgcolor", bgcolor);
        editor.apply();
    }



    public void savePrefs(String key, boolean value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

}
