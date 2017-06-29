package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Севастьян on 01.03.2017.
 */

public class NewNoteFragment extends Fragment {
    private static final String TAG = "QQQ";
    private static final String ARG_NOTE_ID = "note_id";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private Note mNote;
    private LinearLayout InfoZone;
    private LinearLayout Chss1, Chss2;
    private EditText mPsit;
    private Boolean sec = true;
    private EditText mPstand;
    private TextView mResView;
    private ScrollView mScrollView;
    private EditText mRec;
    private Button mButton;
    private TextView mSaveAll;
    private TextView mText16;
    private Button mButtonDate;
    private TextView Zone;
    private TextView AboutZone;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonMorning;
    private RadioButton mRadioButtonTrain;
    private Chronometer mChronometer;
    private ImageView mButtonStart;
    private ImageView mButtonReset;
    private ImageView mChrono;
    private ImageView mZoneBallView;
    private Boolean actVib = false;

    public static NewNoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);
        NewNoteFragment fragment = new NewNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void secondCircularImageBar(ImageView iv2, int z, String s) {

        Bitmap b = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#efefef"));
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(225, 225, 205, paint);
        canvas.drawCircle(225, 225, 175, paint);
        canvas.drawCircle(225, 225, 145, paint);
        canvas.drawCircle(225, 225, 115, paint);

        switch (z) {
            case 1:
                paint.setColor(Color.parseColor("#c0e637"));
                canvas.drawCircle(225, 225, 205, paint);
                break;
            case 2:
                paint.setColor(Color.parseColor("#55bbeb"));
                canvas.drawCircle(225, 225, 175, paint);
                break;
            case 3:
                paint.setColor(Color.parseColor("#f0d73c"));
                canvas.drawCircle(225, 225, 145, paint);
                break;
            case 4:
                paint.setColor(Color.parseColor("#e76660"));
                canvas.drawCircle(225, 225, 115, paint);
                break;
        }
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#FFC95644"));
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(s, 225, 225 + (paint.getTextSize() / 3), paint);
        iv2.setImageBitmap(b);
    }
    private void circularImageBar(ImageView iv2, int i) {

        Bitmap b = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#c4c4c4"));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150, 150, 140, paint);
        paint.setColor(Color.parseColor("#d74b4e"));
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        oval.set(10, 10, 290, 290);
        canvas.drawArc(oval, 270, ((i * 360) / 60), false, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#FFC95644"));
        paint.setTextSize(80);
        canvas.drawText("" + (i / 60) + ":" + i, 150, 150 + (paint.getTextSize() / 3), paint);
        iv2.setImageBitmap(b);
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
        inflater.inflate(R.menu.new_fragment_note, menu);
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
        View v = inflater.inflate(R.layout.new_fragment_note, container, false);
        mChrono = (ImageView) v.findViewById(R.id.chrono);
        Chss1 = (LinearLayout) v.findViewById(R.id.chss1);
        Chss1.setBackgroundResource(R.drawable.grey_shape);
        Chss2 = (LinearLayout) v.findViewById(R.id.chss2);
        Chss2.setBackgroundResource(R.drawable.grey_shape);
        circularImageBar(mChrono, 0);
        mPsit = (EditText) v.findViewById(R.id.p_sit);
        mPsit.setText(mNote.getPsit());
        mChronometer = (Chronometer) v.findViewById(R.id.chronometer);
        mChronometer.setVisibility(View.INVISIBLE);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();
                if (elapsedMillis >= 60000)
                    elapsedMillis = 0;
                circularImageBar(mChrono, (int) (elapsedMillis / 1000));
                if (elapsedMillis > 15000 && !actVib) {
                    actVib = true;
                    Intent intentVibrate = new Intent(getActivity(), VibrateService.class);
                    getActivity().startService(intentVibrate);
                }
            }
        });
        mButtonStart = (ImageView) v.findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sec) {
                    onStartClick();
                    mButtonStart.setImageResource(R.drawable.stop);
                    sec = !sec;
                } else {
                    onStopClick();
                    mButtonStart.setImageResource(R.drawable.start);
                    sec = !sec;
                }
            }
        });
//        mButtonStop = (Button) findViewById(R.id.buttonStop);
//        mButtonStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStopClick();
//            }
//        });
//        mButtonReset = (ImageView) v.findViewById(R.id.buttonStop);
//        mButtonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStopClick();
//            }
//        });

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
                dialog.setTargetFragment(NewNoteFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
//        mViewDate = (TextView) v.findViewById(R.id.dateView);
//        mViewDate.setText(mNote.getDateForm());
        mButtonDate.setText(mNote.getDateForm());
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
        mZoneBallView = (ImageView) v.findViewById(R.id.resview);
        mButton = (Button) v.findViewById(R.id.button);
        mText16 = (TextView) v.findViewById(R.id.textView16);
        mResView = (TextView) v.findViewById(R.id.restext);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        InfoZone = (LinearLayout) v.findViewById(R.id.infozone);
        Zone = (TextView) v.findViewById(R.id.textZone);
        mScrollView = (ScrollView) v.findViewById(R.id.scroll);
        mSaveAll = (Button) v.findViewById(R.id.save);
        AboutZone = (TextView) v.findViewById(R.id.textLetZone);
        if (mNote.getPsit() != null && mNote.getPstand() != null) {
            mZoneBallView.setVisibility(View.VISIBLE);
            secondCircularImageBar(mZoneBallView, mNote.getZone(), mNote.getPoint());
            mButtonDate.setVisibility(View.VISIBLE);
            mText16.setVisibility(View.VISIBLE);
            mRadioGroup.setVisibility(View.VISIBLE);
            mButtonDate.setVisibility(View.VISIBLE);
            mRec.setVisibility(View.VISIBLE);
            InfoZone.setVisibility(View.VISIBLE);
            mResView.setVisibility(View.VISIBLE);
            mSaveAll.setVisibility(View.VISIBLE);
            mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mZoneBallView.getY() - 100);
            switch (mNote.getZone()) {
                case 1:
                    Zone.setText(R.string.zone_1t);
                    AboutZone.setText(R.string.zone_1);
                    break;
                case 2:
                    Zone.setText(R.string.zone_2t);
                    AboutZone.setText(R.string.zone_2);
                    break;
                case 3:
                    Zone.setText(R.string.zone_3t);
                    AboutZone.setText(R.string.zone_3);
                    break;
                case 4:
                    Zone.setText(R.string.zone_4t);
                    AboutZone.setText(R.string.zone_4);
                    break;
            }
            mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mZoneBallView.getY() - 100);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNote.getPsit() != null && mNote.getPstand() != null) {
//                    mPointsView.setText(mNote.getPoint());
//                    mZoneView.setText(Integer.toString(mNote.getZone()));
                    mZoneBallView.setVisibility(View.VISIBLE);
                    secondCircularImageBar(mZoneBallView, mNote.getZone(), mNote.getPoint());
                    mButtonDate.setVisibility(View.VISIBLE);
                    mText16.setVisibility(View.VISIBLE);
                    mRadioGroup.setVisibility(View.VISIBLE);
                    mButtonDate.setVisibility(View.VISIBLE);
                    mRec.setVisibility(View.VISIBLE);
                    InfoZone.setVisibility(View.VISIBLE);
                    mResView.setVisibility(View.VISIBLE);
                    mSaveAll.setVisibility(View.VISIBLE);
                    mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mZoneBallView.getY() - 100);
                    switch (mNote.getZone()) {
                        case 1:
                            Zone.setText(R.string.zone_1t);
                            AboutZone.setText(R.string.zone_1);
                            break;
                        case 2:
                            Zone.setText(R.string.zone_2t);
                            AboutZone.setText(R.string.zone_2);
                            break;
                        case 3:
                            Zone.setText(R.string.zone_3t);
                            AboutZone.setText(R.string.zone_3);
                            break;
                        case 4:
                            Zone.setText(R.string.zone_4t);
                            AboutZone.setText(R.string.zone_4);
                            break;
                    }
                    mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mZoneBallView.getY() - 100);
                }
            }
        });
        mSaveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        mZoneView = (TextView) v.findViewById(R.id.ZoneView);
//        mPointsView = (TextView) v.findViewById(R.id.PointsView);

        if (mNote.getPsit() != null && mNote.getPstand() != null) {
            mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mZoneBallView.getY());
            secondCircularImageBar(mZoneBallView, mNote.getZone(), mNote.getPoint());
//            mPointsView.setText(mNote.getPoint());
//            mZoneView.setText(Integer.toString(mNote.getZone()));

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
            //mViewDate.setText(mNote.getDateForm());
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

