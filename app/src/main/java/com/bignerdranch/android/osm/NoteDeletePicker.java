package com.bignerdranch.android.osm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Севастьян on 06.03.2017.
 */

public class NoteDeletePicker extends DialogFragment {
    NoteLab noteLab = NoteLab.get(getActivity());
    private List<Note> mNotes;
    private SQLiteDatabase mDatabase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Вы действительно хотите удалить все записи?")
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.get(getActivity()).delAllNote();
                                Toast.makeText(getActivity(),
                                        "Все записи удалены",
                                        Toast.LENGTH_SHORT).show();
                                //getActivity().finish();
                            }
                        }).setNegativeButton("Нет", null)
                .create();
    }


}
