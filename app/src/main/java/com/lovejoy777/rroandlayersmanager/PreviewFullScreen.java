package com.lovejoy777.rroandlayersmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import util.Basename;

/**
 * Created by lovejoy777 on 21/02/15.
 */

public class PreviewFullScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewfullscreen);

        SharedPreferences prefs = getSharedPreferences("BackgroundColor",
                MODE_PRIVATE);
        int bgcolor = prefs.getInt("bgcolor", 1728053248);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.layout1);
        l1.setBackgroundColor(bgcolor);

        // GET IMAGE INDEX
        Intent extras = getIntent();
        String imageId = extras.getStringExtra("key4");
        Basename newbasename = new Basename(imageId, '/', '.');
        String imagepathtozip = newbasename + ".zip";

        // DISPLAY IMAGE
        File imgFile = new File(imageId);

        // IF IMAGE EXISTS
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            // SET IMAGE VIEW
            ImageView myImage = (ImageView) findViewById(R.id.previewlargeimage);

            // SET IMAGE
            myImage.setImageBitmap(myBitmap);

        }
    }
}
