package com.lovejoy777.rroandlayersmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.lovejoy777.rroandlayersmanager.adapter.GridViewImageAdapter;
import com.lovejoy777.rroandlayersmanager.commands.RootCommands;
import com.lovejoy777.rroandlayersmanager.helper.AppConstant;
import com.lovejoy777.rroandlayersmanager.helper.Utils;
import util.Basename;

public class GridViewActivity extends ActionBarActivity {

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // GET PATH STRING
            String imageId = (String) parent.getItemAtPosition(position);
            Basename newbasename = new Basename(imageId, '/', '.');
            String newname = "" + newbasename.basename();
            String newnamezip = newname + ".apk";

            // SEND INTENT TO PreviewFullScreen Activity
            Intent iIntent = new Intent(getApplicationContext(), FullScreenViewActivity.class);
            //PASS THE IMAGE INDEX
            iIntent.putExtra("key4", position);
            // START THE INTENT FOR "PreviewFullScreen"
            startActivity(iIntent);

        }
    };  // END SHORT CLICK
    // START LONG CLICK
    AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            String imageId = (String) parent.getItemAtPosition(position);

            Basename newbasename = new Basename(imageId, '/', '.');
            String newname = "" + newbasename.basename();
            String newnameapk = newname + ".apk";

            String destfold = "/vendor/overlay/";
            String layerstempsd = getApplicationInfo().dataDir + "/overlay/" + newnameapk;

            Toast.makeText(getApplicationContext(), "Installing " + newnameapk, Toast.LENGTH_LONG).show();

            RootTools.remount("/system", "RW");

            try {

                RootCommands.moveCopyRoot(layerstempsd, destfold);

                CommandCapture command = new CommandCapture(0, "chmod -R 666 /vendor/overlay", "chmod 755 /vendor/overlay");
                RootTools.getShell(true).add(command);
                while (!command.isFinished()) {
                    Thread.sleep(1);
                }

                RootTools.closeAllShells();

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

            return true;
        }
    };  //END LONG CLICK
    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("BackgroundColor",
                MODE_PRIVATE);
        int bgcolor = prefs.getInt("bgcolor", 1728053248);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.layout1);
        l1.setBackgroundColor(bgcolor);

        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.toolbar_container);
        l2.setBackgroundColor(bgcolor);


        gridView = (GridView) findViewById(R.id.grid_view);

        utils = new Utils(this);

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();

        // Gridview adapter
        adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);

        // ON CLICK FULLSCREEN
        gridView.setOnItemClickListener(myOnItemClickListener);

        // ON LONG CLICK INSTALL
        gridView.setOnItemLongClickListener(myOnItemLongClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.previews_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.back2, R.anim.back1);
            this.startActivity(new Intent(GridViewActivity.this, menu.class));
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the menu/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case R.id.action_installall:
                Intent intent2 = new Intent();
                intent2.setClass(this, InstallAll.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }

        return super.

                onOptionsItemSelected(item);
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);

    }
}
