package com.bignerdranch.android.osm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.osm.database.NoteBaseHelper;
import com.bignerdranch.android.osm.database.NoteCursorWrapper;
import com.bignerdranch.android.osm.database.NoteDbSchema.NoteTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.osm.database.NoteDbSchema.NoteTable.Cols;

/**
 * Created by Севастьян on 02.03.2017.
 */

public class NoteLab {
    private static final String TAG = "Lab";
    private static NoteLab sNoteLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private String str = null;
    private String TagRadio = null;
    private String[] Radio = null;
    private String Sort = null;
    private String What = null;
    private String Then = null;

    private NoteLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();


    }

    public static NoteLab get(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    private static ContentValues getContentValues(Note note) {

        ContentValues values = new ContentValues();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String s = format1.format(note.getDate());
        values.put(Cols.UUID, note.getId().toString());
        values.put(Cols.DATE, s);
        values.put(Cols.RADIO, note.isRad() ? 1 : 0);
        values.put(Cols.UP, note.getPstand());
        values.put(Cols.DOWN, note.getPsit());
        values.put(Cols.POINTS, note.getPoint());
        values.put(Cols.ZONE, note.getZone());
        values.put(Cols.REC, note.getRec());
        return values;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public void setTagRadio(String tagRadio) {
        TagRadio = tagRadio;
    }

    public void setRadio(String[] radio) {
        Radio = radio;
    }

    public void setThen(String then) {
        Then = then;
    }

    public void setWhat(String what) {
        What = what;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(TagRadio, Radio);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;
    }

    public Note getNote(UUID id) {

        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                What, // groupBy
                Then, // having
                Sort // orderBy
        );

        return new NoteCursorWrapper(cursor);
    }

    //Cursor cursor = mDatabase.rawQuery("select * from todo where DATE = ?", new String[] {  });
    public void addNote(Note c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void delNote(Note c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(NoteTable.NAME, NoteTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public void delAllNote() {
        mDatabase.delete(NoteTable.NAME, null, null);
    }

    public void updateNote(Note note) {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME, values,
                NoteTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }
}
