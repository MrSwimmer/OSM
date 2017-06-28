package com.bignerdranch.android.osm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Севастьян on 02.03.2017.
 */

public class NoteListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String TAG = "MyActivity";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    public String sort = null;
    public String[] rad = null;
    public String tagrad = null;
    public String what = null;
    public String than = null;
    int z1 = 0, z2 = 0, z3 = 0, z4 = 0;
    float midball = 0;
    private LinearLayout LayoutZone;
    private NoteAdapter mAdapter;
    private RecyclerView mNoteRecyclerView;
    private boolean mSubtitleVisible;
    private Date dat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mNoteRecyclerView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean
                    (SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }
    @Override
    public void onResume() {

        super.onResume();
        updateUI();

    }

    public void update() {
        Intent intent = new Intent(getActivity(), NoteListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    public void updateUI() {
        NoteLab noteLab = NoteLab.get(getActivity());
        List<Note> crimes = noteLab.getNotes();
        noteLab.setSort(sort);
        noteLab.setTagRadio(tagrad);
        noteLab.setRadio(rad);
        noteLab.setWhat(what);
        noteLab.setThen(than);
        if (mAdapter == null) {
            mAdapter = new NoteAdapter(crimes);
            mNoteRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            dat = date;
        }
        /*tagrad="DATE = ?";
        long l=dat.getTime();
        rad=new String[]{Long.toString(l)};*/
        tagrad = "DATE = ?";
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String s = format1.format(dat);
        rad = new String[]{s};
        updateUI();
        update();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.menu_item_new_note:
                Note note = new Note();
                NoteLab.get(getActivity()).addNote(note);
                Intent intent = NotePagerActivity.newIntent(getActivity(), note.getId());
                startActivity(intent);
                return true;*/
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.sortDateU:
                sort = "DATE DESC";
                updateUI();
                update();
                return true;
            case R.id.sortDateV:
                sort = "DATE ASC";
                updateUI();
                update();
                return true;
            case R.id.SearchByDate:
                Date date = new Date();
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(NoteListFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                //updateUI();
                return true;
            /*case R.id.showMonth:
                Calendar cal = Calendar.getInstance();

                int m=cal.get(Calendar.MONTH);
                int y=cal.get(Calendar.YEAR);
//                SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
//                String s=format1.format(cal);
                String s="01.02.2017";
                tagrad="DATE > ?";
                rad=new String[]{s};
                updateUI();
                update();
                return true;*/
//            case R.id.OnlyAfterSleep:
//                tagrad="RADIO = ?";
//                rad=new String[]{};
//                updateUI();
//                update();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        NoteLab noteLab = NoteLab.get(getActivity());
        int noteCount = noteLab.getNotes().size();
        @SuppressLint("StringFormatMatches") String subtitle = getString(R.string.subtitle_format, noteCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDateTextView;
        private TextView mRadio;
        private TextView mPoints;
        private TextView mZone;
        private Note mNote;

        public NoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            LayoutZone = (LinearLayout) itemView.findViewById(R.id.layout_zone);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_z_date_text_view);
            mRadio = (TextView) itemView.findViewById(R.id.list_item_z_radio_text_view);
            mPoints = (TextView) itemView.findViewById(R.id.list_item_z_point);
            mZone = (TextView) itemView.findViewById(R.id.list_item_z_zone);
        }

        public void bindNote(Note note) {
            mNote = note;
            mDateTextView.setText(mNote.getDateForm());
            if (mNote.isRad()) {
                mRadio.setText("После сна");
            } else {
                mRadio.setText("После тренировки");
            }
            //mPoints.setText( mNote.getPoint());
            if (mNote.getPsit() != null && mNote.getPstand() != null) {
                mPoints.setText(mNote.getPoint());
                if (midball == 0)
                    midball = mNote.getPointFloat();
                else
                    midball = (midball + mNote.getPointFloat()) / 2;
                mZone.setText(Integer.toString(mNote.getZone()));
                switch (mNote.getZone()) {
                    case 1:
                        LayoutZone.setBackgroundResource(R.drawable.oval_green);
                        z1++;
                        break;
                    case 2:
                        LayoutZone.setBackgroundResource(R.drawable.oval_blue);
                        z2++;
                        break;
                    case 3:
                        LayoutZone.setBackgroundResource(R.drawable.oval_yellow);
                        z3++;
                        break;
                    case 4:
                        LayoutZone.setBackgroundResource(R.drawable.oval_red);
                        z4++;
                        break;
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
