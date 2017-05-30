package com.bignerdranch.android.osm;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Севастьян on 26.05.2017.
 */

public class expimp {
    public String operation = null;

    expimp() {
        File direct = new File(Environment.getExternalStorageDirectory() + "/OSM");
        if (!direct.exists()) {
            if (direct.mkdir()) {
                direct.mkdir();
                //directory is created;
            }

        }

    }

    public void importDB(File file) {

        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.bignerdranch.android.osm"
                        + "//databases//" + "noteBase.db";
                String backupDBPath = "/OSM/noteBase.db";
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);
                if (file != null)
                    backupDB = file;
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();


            }
        } catch (Exception e) {

        }
    }

    //exporting database
    public void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.bignerdranch.android.osm"
                        + "//databases//" + "noteBase.db";
                String backupDBPath = "/OSM/noteBase.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            }
        } catch (Exception e) {

        }
    }


}
