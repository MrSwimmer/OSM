package com.bignerdranch.android.osm;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

/**
 * Created by Севастьян on 03.05.2017.
 */

public class TheBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    static final String HIGH_SCORES_FILENAME = "notes";

    // A key to uniquely identify the set of backup data
    static final String FILES_BACKUP_KEY = "noteBase";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        FileBackupHelper helper = new FileBackupHelper(this, HIGH_SCORES_FILENAME);
        addHelper(FILES_BACKUP_KEY, helper);
    }

}
