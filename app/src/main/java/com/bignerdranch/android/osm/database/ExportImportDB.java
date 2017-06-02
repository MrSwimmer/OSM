package com.bignerdranch.android.osm.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.bignerdranch.android.osm.LogInActivity;
import com.bignerdranch.android.osm.SucEnter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ExportImportDB extends Activity {
    String operation = null;
    File mFile = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        operation = intent.getStringExtra("op");

//        Intent intent = getIntent();
//        operation = intent.getStringExtra("operation");
//creating a new folder for the database to be backuped to
        File direct = new File(Environment.getExternalStorageDirectory() + "/OSM");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                direct.mkdir();
                //directory is created;
            }

        }
        if (operation == null) {
            exportDB();
            Intent i = new Intent(ExportImportDB.this, LogInActivity.class);
            startActivity(i);
        }
        if (operation == "imp") {
            importDB();
            Intent i = new Intent(ExportImportDB.this, SucEnter.class);
            startActivity(i);
        }
        importDB();

    }

    //importing database
    private void importDB() {
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

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
//                Toast.makeText(getBaseContext(), backupDB.toString(),
//                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

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
//                Toast.makeText(getBaseContext(), "Бэкап данных выполнен!",
//                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

}