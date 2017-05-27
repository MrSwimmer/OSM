package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Севастьян on 01.03.2017.
 */

public class NoteFragment extends Fragment {
    private static final String TAG = "QQQ";
    private static final String ARG_NOTE_ID = "note_id";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private Note mNote;
    private Boolean mSecVis = false;
    private EditText mPsit;
    private EditText mPstand;
    private EditText mRec;
    private Button mButton;
    private TextView mViewDate;
    private Button mButtonDate;
    private TextView mZoneView;
    private TextView mPointsView;
    private RadioButton mRadioButtonMorning;
    private RadioButton mRadioButtonTrain;
    private Chronometer mChronometer;
    private ImageView mButtonStart;
    private Button mButtonStop;
    private ImageView mButtonReset;
    private Boolean actVib = false;
    private AdView mAdView;
    private LinearLayout mLinearSec;

    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteLab.get(getActivity()).getNote(noteId);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                /*FragmentManager manager = getFragmentManager();
                NoteDeletePicker dialog = new NoteDeletePicker();
                dialog.show(manager, DIALOG_DATE);*/
                NoteLab.get(getActivity()).delNote(mNote);
                getActivity().finish();
                return true;
            //case R.id.menu_item_sec:
            case R.id.menu_item_send_note:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getNoteReport());
                i.putExtra(Intent.EXTRA_SUBJECT, "Отправка результатов");
                i = Intent.createChooser(i, "Отправить с помощью");
                startActivity(i);
//            case R.id.menu_item_sec:
//                mSecVis=!mSecVis;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStartClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void onStopClick() {
        mChronometer.stop();
    }

    public void onResetClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        mPsit = (EditText) v.findViewById(R.id.p_sit);
        mPsit.setText(mNote.getPsit());
        mChronometer = (Chronometer) v.findViewById(R.id.chronometer);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();
                if (elapsedMillis > 15000 && !actVib) {
                    actVib = true;
                    Intent intentVibrate = new Intent(getActivity(), VibrateService.class);
                    getActivity().startService(intentVibrate);
                }
            }
        });
        mButtonStart = (ImageView) v.findViewById(R.id.buttonStart);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClick();
            }
        });
//        mButtonStop = (Button) findViewById(R.id.buttonStop);
//        mButtonStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStopClick();
//            }
//        });
        mButtonReset = (ImageView) v.findViewById(R.id.buttonStop);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopClick();
            }
        });

        mPsit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setPsit(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPstand = (EditText) v.findViewById(R.id.p_stand);
        mPstand.setText(mNote.getPstand());
        mPstand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mNote.setPstand(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRec = (EditText) v.findViewById(R.id.rec);
        mRec.setText(mNote.getRec());
        mRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setRec(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mButtonDate = (Button) v.findViewById(R.id.note_date);
        //mButtonDate.setText(mNote.getDate().toString());
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mNote.getDate());
                dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        mViewDate = (TextView) v.findViewById(R.id.dateView);
        mViewDate.setText(mNote.getDateForm());

        mRadioButtonMorning = (RadioButton) v.findViewById(R.id.radio_after_sleep);
        mRadioButtonTrain = (RadioButton) v.findViewById(R.id.radio_after_train);
        mRadioButtonMorning.setChecked(mNote.isRad());
        mRadioButtonTrain.setChecked(!mNote.isRad());
        mRadioButtonMorning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNote.setRad(!mNote.isRad());
                Log.i(TAG, String.valueOf(mNote.isRad()));
            }
        });
        mButton = (Button) v.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNote.getPsit() != null && mNote.getPstand() != null) {
                    mPointsView.setText(mNote.getPoint());
                    mZoneView.setText(Integer.toString(mNote.getZone()));
                }
            }
        });
        mZoneView = (TextView) v.findViewById(R.id.ZoneView);
        mPointsView = (TextView) v.findViewById(R.id.PointsView);
        if (mNote.getPsit() != null && mNote.getPstand() != null) {
            mPointsView.setText(mNote.getPoint());
            mZoneView.setText(Integer.toString(mNote.getZone()));
        }

        //mRadioButtonTrain = (RadioButton) v.findViewById(R.id.radio_after_train);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mNote.setDate(date);
            //mButtonDate.setText(mNote.getDate().toString());
            mViewDate.setText(mNote.getDateForm());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        NoteLab.get(getActivity())
                .updateNote(mNote);
    }

    private String getNoteReport() {
        String radioString = null;
        if (mNote.isRad()) {
            radioString = "После сна";
        } else {
            radioString = "После тренировки";
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mNote.getDate()).toString();
        String recString = mNote.getRec();
        String sitString = mNote.getPsit();
        String standString = mNote.getPstand();
        String zoneString = Integer.toString(mNote.getZone());
        String pointsString = mNote.getPoint();
        String report = getString(R.string.note_report, dateString, radioString, sitString, standString, pointsString, zoneString, recString);
        return report;
    }
}

