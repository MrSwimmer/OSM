package com.bignerdranch.android.osm.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.osm.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.bignerdranch.android.osm.database.NoteDbSchema.NoteTable.Cols;

/**
 * Created by Севастьян on 05.03.2017.
 */

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String date = getString(getColumnIndex(Cols.DATE));
        int radio = getInt(getColumnIndex(Cols.RADIO));
        String up = getString(getColumnIndex(Cols.UP));
        String down = getString(getColumnIndex(Cols.DOWN));
        int points = getInt(getColumnIndex(Cols.POINTS));
        String rec = getString(getColumnIndex(Cols.REC));
        int zone = getInt(getColumnIndex(Cols.ZONE));
        Note note = new Note(UUID.fromString(uuidString));
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date docDate = null;
        try {
            docDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        note.setDate(docDate);
        note.setRad(radio != 0);
        note.setPsit(down);
        note.setPstand(up);
        note.setPoint(points);
        note.setRec(rec);
        note.setZone(zone);
        return note;
    }
}
