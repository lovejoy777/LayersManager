package com.lovejoy777.rroandlayersmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lovejoy777.rroandlayersmanager.commands.RootCommands;

/**
 * Created by lovejoy777 on 27/02/15.
 */
public class InstallAll extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String layersdatapreviewzip = getApplicationInfo().dataDir + "/overlay/previewimages.zip";
        String layersdata = getApplicationInfo().dataDir + "/overlay";
        String vendover = "/vendor/";

        try {

            RootTools.remount("/system", "RW");

            // DELETES PREVIOUS /VENDOR/OVERLAY/ FOLDER
            File dir = new File("/vendor/overlay");
            if (dir.exists() && dir.isDirectory()) {

                RootCommands.DeleteFileRoot("/vendor/overlay");
            }

            // DELETES /DATA/DATA/OVERLAY/PREVIEWIMAGES.ZIP
            File dir1 = new File(layersdatapreviewzip);
            if (dir1.exists()) {

                RootCommands.DeleteFileRoot(layersdatapreviewzip);
            }

            // MK DIR /VENDOR/OVERLAY
            CommandCapture command1 = new CommandCapture(0, "mkdir /vendor/overlay");

            RootTools.getShell(true).add(command1);
            while (!command1.isFinished()) {
                Thread.sleep(1);
            }

            // COPY NEW FILES TO /VENDOR/OVERLAY FOLDER
            RootCommands.moveCopyRoot(layersdata, vendover);

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
            CommandCapture command2 = new CommandCapture(0, "chmod -R 777 /vendor/overlay");
            RootTools.getShell(true).add(command2);
            while (!command2.isFinished()) {
                Thread.sleep(1);
            }

            // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666
            CommandCapture command3 = new CommandCapture(0, "chmod -R 666 /vendor/overlay", "chmod 777 /vendor/overlay");
            RootTools.getShell(true).add(command3);
            while (!command3.isFinished()) {
                Thread.sleep(1);
            }

            // DELETE /VENDOR/OVERLAY/PREVIEWIMAGES.ZIP
            File dir2 = new File("/vendor/overlay/previewimages.zip");
            if (dir2.exists()) {
                RootCommands.DeleteFileRoot("/vendor/overlay/previewimages.zip");
            }

            //RootTools.remount("/system", "RO");

            // CLOSE ALL SHELLS
            RootTools.closeAllShells();

            Toast.makeText(InstallAll.this, "INSTALL FINISHED", Toast.LENGTH_LONG).show();

            // LAUNCH LAYERS.CLASS
            overridePendingTransition(R.anim.back2, R.anim.back1);
            Intent iIntent = new Intent(this, menu.class);
            iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(iIntent);

            finish();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
