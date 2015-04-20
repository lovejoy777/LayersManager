package com.lovejoy777.rroandlayersmanager.deleters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.lovejoy777.rroandlayersmanager.About;
import com.lovejoy777.rroandlayersmanager.BackUpRestore;
import com.lovejoy777.rroandlayersmanager.ChangeLog;
import com.lovejoy777.rroandlayersmanager.DeleteLayers;
import com.lovejoy777.rroandlayersmanager.Instructions;
import com.lovejoy777.rroandlayersmanager.R;
import com.lovejoy777.rroandlayersmanager.Settings;
import com.lovejoy777.rroandlayersmanager.filepicker.FilePickerActivity;
import com.lovejoy777.rroandlayersmanager.menu;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by lovejoy on 04/04/15.
 */
public class CardDeleteLayers extends ActionBarActivity {
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;

    final String startDir = "/vendor/overlay";
    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;

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
        setContentView(R.layout.activity_list);

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
                                Intent intent = new Intent(CardDeleteLayers.this, menu.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(CardDeleteLayers.this, BackUpRestore.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(CardDeleteLayers.this, DeleteLayers.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(CardDeleteLayers.this, About.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Intent intent = new Intent(CardDeleteLayers.this, ChangeLog.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(CardDeleteLayers.this, Instructions.class);
                                CardDeleteLayers.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 7) {
                                Intent intent = new Intent(CardDeleteLayers.this, Settings.class);
                                CardDeleteLayers.this.startActivity(intent);
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
                                    Intent intent = new Intent(CardDeleteLayers.this, menu.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 2) {
                                    Intent intent = new Intent(CardDeleteLayers.this, BackUpRestore.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 3) {
                                    Intent intent = new Intent(CardDeleteLayers.this, DeleteLayers.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 4) {
                                    Intent intent = new Intent(CardDeleteLayers.this, About.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 5) {
                                    Intent intent = new Intent(CardDeleteLayers.this, ChangeLog.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 6) {
                                    Intent intent = new Intent(CardDeleteLayers.this, Instructions.class);
                                    CardDeleteLayers.this.startActivity(intent);
                                } else if (drawerItem.getIdentifier() == 7) {
                                    Intent intent = new Intent(CardDeleteLayers.this, Settings.class);
                                    CardDeleteLayers.this.startActivity(intent);
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

        // GET STRING FOR TEXT VIEW
        final Intent extras = getIntent();
        String SZP1 = null;
        if (extras != null) {
            SZP1 = extras.getStringExtra("key1");
        }

        final String SZP = SZP1;
        TextView tv = (TextView) findViewById(R.id.tvlayerspath);
        tv.setText(SZP);

        int listImages[] = new int[]{R.drawable.books, R.drawable.delete,
                R.drawable.delete, R.drawable.power};


        final ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < 4; i++) {
            // Create a Card
            final Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);


            // get & set thumbnail
            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listImages[i]);
            card.addCardThumbnail(thumb);


            if (i == 0) {
                // Add Header to card
              //  header.setTitle("File Chooser");
                card.setTitle("FILE CHOOSER\n \nAll Installed Layers");
               // card.addCardHeader(header);
            }

            if (i == 1) {
                // Add Header to card
               // header.setTitle("Delete");
                card.setTitle("DELETE\n \nSelected Layers");
               // card.addCardHeader(header);

            }

            if (i == 2) {
                // Add Header to card
               // header.setTitle("Delete All");
                card.setTitle("DELETE ALL\n \nApplied Layers");
               // card.addCardHeader(header);

            }

            if (i == 3) {
                // Add Header to card
               // header.setTitle("Reboot");
                card.setTitle("REBOOT\n \nFor Changes to Take Effect");
               // card.addCardHeader(header);

            }

            final int finalI = i;

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //  Toast.makeText(CardsListActivity.this, "installing " + finalSZP, Toast.LENGTH_LONG).show();

                    if (finalI == 0) {

                        // LAUNCH DELETE CHOOSER
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        // Set these depending on your use case. These are the defaults.
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true); // true = 1 | false = 2
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                        i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDir);

                        startActivityForResult(i, CODE_SD);

                    }

                    if (finalI == 1) {

                        // DELETE SELECTED FILE BUTTON

                        if (SZP != null) {

                            deletemultiplecommand(); // DELETE SELECTED LAYERS
                        }
                    }

                    if (finalI == 2) {

                        deleteallcommand(); // DELETE ALL LAYERS
                    }

                    if (finalI == 3) {

                        rebootcommand(); // REBOOT DEVICE
                    }
                }
            });

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }


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
                    Intent iIntent = new Intent(this, CardDeleteLayers.class);
                    iIntent.putExtra("key1", SZP);
                    iIntent.putStringArrayListExtra("key2", paths);
                    startActivity(iIntent);

                }

            } else {

                // 2
                // Get the File path from the Uri
                String SZP = (data.getData().toString());
                if (SZP.startsWith("file://")) {
                    SZP = SZP.substring(7);
                    Intent iIntent = new Intent(this, CardDeleteLayers.class);
                    iIntent.putExtra("key1", SZP);
                    startActivity(iIntent);
                }
            }
        }
    }

    // COMMAND 1 DELETE SELECTED LAYERS
    public void deletemultiplecommand() {

        ArrayList<String> paths;
        paths = getIntent().getStringArrayListExtra("key2");

        if (paths != null) {

            for (String path : paths) {
                if (path.startsWith("file://")) {
                    path = path.substring(7);

                    RootTools.remount("/system", "RW");
                    RootTools.deleteFileOrDirectory(path, true);
                }
            }

            Toast.makeText(CardDeleteLayers.this, "DELETED SELECTED LAYERS", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(CardDeleteLayers.this, "NOTHING TO DELETE", Toast.LENGTH_LONG).show();
        }
    }

    // COMMAND 2 DELETE ALL
    public void deleteallcommand() {

        String delall = "/vendor/overlay/";

        try {

            RootTools.remount("/system", "RW");
            RootTools.deleteFileOrDirectory(delall, true);
            Toast.makeText(CardDeleteLayers.this, "DELETED ALL LAYERS", Toast.LENGTH_LONG).show();

            // CLOSE ALL SHELLS
            RootTools.closeAllShells();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // COMMAND 3 REBOOT DEVICE

    public void rebootcommand() {

        try {

            Toast.makeText(CardDeleteLayers.this, "Rebooting", Toast.LENGTH_LONG).show();
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
}
