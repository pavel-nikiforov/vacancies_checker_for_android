package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pavel_nikiforov.android.vacancieschecker.database.DBReader;
import com.pavel_nikiforov.android.vacancieschecker.logic.Employer;
import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import java.util.List;

public class EmployersListFragment extends Fragment {
    private static final String TAG = "EmployersListFrg";
    private List<Employer> mEmployersList;
    private List<Vacancy> mVacanciesList;

    private RecyclerView mEmpoyersReciclerView;
    private EmloyersListAdapter mAdapter;

    private boolean mIsDesc = false;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(true);

        new FetchEmployersFromDB().execute(DBReader.ORDER_BY_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employers_list, container, false);

        mEmpoyersReciclerView = (RecyclerView) view.findViewById(R.id.dbempl_recyclerview);
        mEmpoyersReciclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(mAdapter != null) mEmpoyersReciclerView.setAdapter(mAdapter);

        return view;
    }


    private void setAdapter(){
        if(mAdapter == null) mAdapter = new EmloyersListAdapter();
        mEmpoyersReciclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private class FetchEmployersFromDB extends AsyncTask<String, Void, Void> {

        @Override
        public Void doInBackground(String... params) {

            DBReader dbReader = DBReader.get(getContext());
            mEmployersList = dbReader.fetchEmployers(params[0]);
            dbReader.closeDatabase();

            return null;
        }

        @Override
        public void onPostExecute(Void result){
            setAdapter();
        }
    }


    private class EmployersListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mEmployerNameTextView;
        private TextView mVacanciesCountTextView;
        private TextView mUpdatedTextView;
        private TextView mActivityRankTextView;

        private Employer mEmployer;

        public EmployersListHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_db_employer, parent, false));

            mEmployerNameTextView = (TextView) itemView.findViewById(R.id.dbempl_cardview_empl_name);
            mVacanciesCountTextView = (TextView) itemView.findViewById(R.id.dbempl_cardview_vac_count);
            mUpdatedTextView = (TextView) itemView.findViewById(R.id.dbempl_cardview_updated);
            mActivityRankTextView = (TextView) itemView.findViewById(R.id.dbempl_cardview_rank);

            itemView.setOnClickListener(this);
        }

        public void bind(Employer employer){
            mEmployer = employer;

            Log.d(TAG, "Binding " + mEmployer.getName());

            mEmployerNameTextView.setText(mEmployer.getName());
            mVacanciesCountTextView.setText(String.valueOf(mEmployer.getCount()));
            mUpdatedTextView.setText(mEmployer.getVacanciesLastUpdated());
            mActivityRankTextView.setText(String.valueOf(mEmployer.getRank()));
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), mEmployer.getName() + " clicked", Toast.LENGTH_SHORT).show();

            new FetchVacanciesForEmployer().execute(mEmployer);

        }


    }

    private class FetchVacanciesForEmployer extends AsyncTask<Employer, Void, Void> {

        @Override
        public Void doInBackground(Employer... params) {

            DBReader dbReader = DBReader.get(getContext());
            mVacanciesList = dbReader.fetchVacanciesByEmployer(params[0]);
            dbReader.closeDatabase();

            return null;
        }

        @Override
        public void onPostExecute(Void result){
            VacancyChecker app = VacancyChecker.getInstance();
            app.setVacanciesByEmployerList(mVacanciesList);

            Bundle args = new Bundle();
            args.putString("type", "by employer");

            Fragment nextFrag = new VacanciesListFragment();
            nextFrag.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private class EmloyersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new EmployersListHolder(inflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

            ((EmployersListHolder) holder).bind(mEmployersList.get(position));
        }

        @Override
        public int getItemCount(){
            return mEmployersList.size();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_empl_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_sort_by_name:
                if(mIsDesc) {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_NAME);
                } else {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_NAME_DESC);
                }
                mIsDesc = !mIsDesc;
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_sort_by_count:
                if(mIsDesc) {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_COUNT);
                } else {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_COUNT_DESC);
                }
                mIsDesc = !mIsDesc;
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_sort_by_rank:
                if(mIsDesc) {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_RANK);
                } else {
                    new FetchEmployersFromDB().execute(DBReader.ORDER_BY_RANK_DESC);
                }
                mIsDesc = !mIsDesc;
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
