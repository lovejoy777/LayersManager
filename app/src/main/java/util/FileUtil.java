package util;


import com.lovejoy777.rroandlayersmanager.Settings;
import com.lovejoy777.rroandlayersmanager.commands.RootCommands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by lovejoy on 13/02/15.
 */
public class FileUtil {


    private static final int BUFFER = 4096;
    private static final long ONE_KB = 1024;
    private static final BigInteger KB_BI = BigInteger.valueOf(ONE_KB);
    private static final BigInteger MB_BI = KB_BI.multiply(KB_BI);
    private static final BigInteger GB_BI = KB_BI.multiply(MB_BI);
    private static final BigInteger TB_BI = KB_BI.multiply(GB_BI);



    public static void copyToDirectory(String old, String newDir) {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read;

        if (old_file.canWrite() && temp_dir.isDirectory()
                && temp_dir.canWrite()) {
            if (old_file.isFile()) {
                String file_name = old.substring(old.lastIndexOf("/"),
                        old.length());
                File cp_file = new File(newDir + file_name);

                try {
                    BufferedOutputStream o_stream = new BufferedOutputStream(
                            new FileOutputStream(cp_file));
                    BufferedInputStream i_stream = new BufferedInputStream(
                            new FileInputStream(old_file));

                    while ((read = i_stream.read(data, 0, BUFFER)) != -1)
                        o_stream.write(data, 0, read);

                    o_stream.flush();
                    i_stream.close();
                    o_stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (old_file.isDirectory()) {
                String files[] = old_file.list();
                String dir = newDir
                        + old.substring(old.lastIndexOf("/"), old.length());

                if (!new File(dir).mkdir())
                    return;

                for (String file : files) copyToDirectory(old + "/" + file, dir);
            }

        }
    }

    public static void moveToDirectory(String old, String newDir) {
        String file_name = old.substring(old.lastIndexOf("/"), old.length());
        File old_file = new File(old);
        File cp_file = new File(newDir + file_name);

        if (!old_file.renameTo(cp_file)) {
            copyToDirectory(old, newDir);
            deleteTarget(old);
        }
    }

    public static void deleteTarget(String path) {
        File target = new File(path);

        if (target.isFile() && target.canWrite()) {
            target.delete();
        } else {
            if (target.isDirectory() && target.canRead()) {
                String[] file_list = target.list();

                if (file_list != null && file_list.length == 0) {
                    target.delete();
                    return;
                } else if (file_list != null && file_list.length > 0) {

                    for (String aFile_list : file_list) {
                        File temp_f = new File(target.getAbsolutePath() + "/"
                                + aFile_list);

                        if (temp_f.isDirectory())
                            deleteTarget(temp_f.getAbsolutePath());
                        else if (temp_f.isFile()) {
                            temp_f.delete();
                        }
                    }
                }

                if (target.exists())
                    target.delete();
            } else if (target.exists() && !target.delete()) {
                //if (Settings.rootAccess())
                 //   RootCommands.DeleteFileRoot(path);
            }
        }
    }
}
