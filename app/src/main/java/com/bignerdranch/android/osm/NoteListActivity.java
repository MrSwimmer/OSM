package com.bignerdranch.android.osm;

import android.support.v4.app.Fragment;

/**
 * Created by Севастьян on 02.03.2017.
 */

public class NoteListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
