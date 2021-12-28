package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pavel_nikiforov.android.vacancieschecker.database.DBReader;
import com.pavel_nikiforov.android.vacancieschecker.logic.HHFetcher;
import com.pavel_nikiforov.android.vacancieschecker.logic.LoggedAsyncTask;
import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateFragment extends Fragment {
    private static final String TAG = "UpdateFragment";
    private List<Vacancy> mRawVacancies = new ArrayList<Vacancy>();
    private List<Vacancy> mRejectedVacancies = new ArrayList<Vacancy>();
    private List<Vacancy> mAcceptedVacancies = new ArrayList<Vacancy>();
    private List<Vacancy> mUpdatedVacancies = new ArrayList<Vacancy>();
    private List<Vacancy> mNewVacancies = new ArrayList<Vacancy>();
    private List<Vacancy> mNewEmployers = new ArrayList<Vacancy>();
    private List<String> mLogList = new ArrayList<>();

    private ViewGroup mViewGroup;
    private RecyclerView mLogReciclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LogAdapter mLogListAdapter;

    private Button mStartButton;
    private boolean mUpdateComplete;

    private CardView mStatusCardView;
    private CardView mWelcomeCardView;

    private long mEmployersTotal = 0;
    private long mVacanciesTotal = 0;
    private String mLastUpdateDate = "";
    TextView mEmpoyersTotalView;
    TextView mVacanciesTotalView;
    TextView mLastUpdateDateView;

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewGroup = container;
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        mLogReciclerView = (RecyclerView) view.findViewById(R.id.log_view);
        mLogListAdapter = new LogAdapter();
        mLogReciclerView.setAdapter(mLogListAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLogReciclerView.setLayoutManager(mLayoutManager);

        mStatusCardView = (CardView) view.findViewById(R.id.stat_view);
        mEmpoyersTotalView = (TextView) view.findViewById(R.id.status_empl_count);
        mEmpoyersTotalView.setText(String.valueOf(mEmployersTotal));
        mVacanciesTotalView = (TextView) view.findViewById(R.id.status_vac_count);
        mVacanciesTotalView.setText(String.valueOf(mVacanciesTotal));
        mLastUpdateDateView = (TextView) view.findViewById(R.id.status_date);
        mLastUpdateDateView.setText(mLastUpdateDate);

        mWelcomeCardView = (CardView) view.findViewById(R.id.welcome_view);

        mStartButton = (Button) view.findViewById(R.id.update_button);
        if(mUpdateComplete){
            mStartButton.setText(R.string.frag_update_view_results_button_name);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatsFragment nextFrag= new StatsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            mStartButton.setText(R.string.frag_update_update_button_name);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStartButton.setEnabled(false);
                    mWelcomeCardView.setVisibility(View.GONE);
                    mStatusCardView.setVisibility(View.GONE);
                    mLogReciclerView.setVisibility(View.VISIBLE);

                    new PagesGrabber().execute();

                }
            });

            new StatusGetter().execute();
        }



        return view;
    }

    private class PagesGrabber extends LoggedAsyncTask {

        @Override
        public Void doInBackground(Void... params) {

            HHFetcher.getVacancies(mRawVacancies, this);
            HHFetcher.filterVacancies(mRawVacancies, mAcceptedVacancies, mRejectedVacancies, this);
            HHFetcher.saveVacancies(getContext(), mAcceptedVacancies, mNewVacancies, mNewEmployers, mUpdatedVacancies, this);
            return null;
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //Log.d(TAG, "+++ onProgressUpdate: " + text[0]);
            mLogList.add(text[0]);
            mLogListAdapter.notifyDataSetChanged();
            mLayoutManager.scrollToPosition(mLogList.size() - 1);
        }


        @Override
        protected void onPostExecute(Void result) {

            VacancyChecker app = VacancyChecker.getInstance();

            app.setRawVacanciesList(mRawVacancies);
            app.setRejectedVacanciesList(mRejectedVacancies);
            app.setAcceptedVacanciesList(mAcceptedVacancies);
            app.setUpdatedVacanciesList(mUpdatedVacancies);
            app.setNewVacanciesList(mNewVacancies);
            app.setNewEmployersList(mNewEmployers);


            mUpdateComplete = true;
            mStartButton.setText(R.string.frag_update_view_results_button_name);
            mStartButton.setEnabled(true);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatsFragment nextFrag= new StatsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }


    }


    private class LogViewHolder extends RecyclerView.ViewHolder {
        private TextView mLogTextView;

        LogViewHolder(Context context, View itemView) {
            super(itemView);

            mLogTextView = (TextView) itemView.findViewById(R.id.log_item_textv);
        }

        public TextView getView() {
            return mLogTextView;
        }
    }


    private class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        @Override
        public int getItemCount() {
            return mLogList.size();
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final Context context = getContext();
            return new LogViewHolder(context, LayoutInflater.from(context).inflate(R.layout.list_item_log, viewGroup, false));

        }

        @Override
        public void onBindViewHolder(LogViewHolder viewHolder, int position) {
            TextView textView = viewHolder.getView();
            textView.setText(mLogList.get(position));
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_update_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_update_settings:
                SettingsFragment nextFrag= new SettingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private class StatusGetter extends AsyncTask<Void, Void, Void>{

        @Override
        public Void doInBackground(Void... params) {

            DBReader reader = DBReader.get(getContext());
            mEmployersTotal = reader.fetchEmployersTotalCount();
            if(mEmployersTotal > 0){
                mVacanciesTotal = reader.fetchVacanciesTotalCount();
                mLastUpdateDate = reader.fetchLastUpdateDate();
            }
            return null;
    }

        @Override
        protected void onPostExecute(Void result) {
            if(mEmployersTotal > 0){
                mWelcomeCardView.setVisibility(View.GONE);

                mStatusCardView.setVisibility(View.VISIBLE);
                mEmpoyersTotalView.setText(String.valueOf(mEmployersTotal));
                mVacanciesTotalView.setText(String.valueOf(mVacanciesTotal));
                mLastUpdateDateView.setText(mLastUpdateDate);
            }
            else {
                mWelcomeCardView.setVisibility(View.VISIBLE);
            }
        }

    }

}


