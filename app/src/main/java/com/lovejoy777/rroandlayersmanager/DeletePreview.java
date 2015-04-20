package com.lovejoy777.rroandlayersmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import java.io.IOException;

/**
 * Created by lovejoy777 on 03/03/15.
 */
public class DeletePreview extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent extras = getIntent();
        String SZP = extras.getStringExtra("position");

        RootTools.remount("/system", "RW");
        RootTools.deleteFileOrDirectory(SZP, true);
        Toast.makeText(DeletePreview.this, "DELETED" + SZP, Toast.LENGTH_LONG).show();
        RootTools.remount("/system", "RO");

        // CLOSE ALL SHELLS
        try {
            RootTools.closeAllShells();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent iIntent = new Intent(this, GridViewActivity.class);
        startActivity(iIntent);

        finish();
    }
}