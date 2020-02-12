package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pavel_nikiforov.android.vacancieschecker.database.DBReader;
import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import java.util.List;

public class RecentVacanciesListFragment extends Fragment {
    private static final String TAG = "EmployersListFrg";
    private List<Vacancy> mVacanciesList;

    private RecyclerView mRecentVacsReciclerView;
    private RecentVacanciesListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(true);

        new FetchRecentVacancies(DBReader.ORDER_BY_DATE).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_vacancies, container, false);

        mRecentVacsReciclerView = (RecyclerView) view.findViewById(R.id.recent_vacs_recyclerview);
        mRecentVacsReciclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mAdapter != null) mRecentVacsReciclerView.setAdapter(mAdapter);

        return view;
    }


    private class RecentVacanciesListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mEmployerNameTextView;
        private TextView mVacancyNameTextView;
        private TextView mLastUpdatedNameTextView;
        private TextView mLastUpdatedTextView;
        private TextView mUpdateCountNameTextView;
        private TextView mUpdateCountTextView;
        private Vacancy mVacancy;

        public RecentVacanciesListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_vacancy, parent, false));

            mEmployerNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_secondary);
            mVacancyNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_primary);

            mLastUpdatedNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updated_date_name_name);
            mLastUpdatedTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updated_date);
            mUpdateCountNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updates_count_name);
            mUpdateCountTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updates_count);

            itemView.setOnClickListener(this);
        }

        public void bind(Vacancy vacancy) {
            mVacancy = vacancy;

            mEmployerNameTextView.setText(mVacancy.getEmployerName());
            mVacancyNameTextView.setText(mVacancy.getName());
            mLastUpdatedTextView.setText(mVacancy.getVacancyLastUpdated());
            mUpdateCountTextView.setText(String.valueOf(mVacancy.getVacancyUpdatesCount()));
        }

        @Override
        public void onClick(View view) {

            Bundle args = new Bundle();
            args.putSerializable("vacancy", mVacancy);

            VacancyDetailsFragment nextFrag = new VacancyDetailsFragment();
            nextFrag.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();

        }
    }


    private class RecentVacanciesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new RecentVacanciesListFragment.RecentVacanciesListHolder(inflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((RecentVacanciesListFragment.RecentVacanciesListHolder) holder).bind(mVacanciesList.get(position));
        }

        @Override
        public int getItemCount() {
            return mVacanciesList.size();
        }
    }


    private void setAdapter() {
        if (mAdapter == null) mAdapter = new RecentVacanciesListAdapter();
        mRecentVacsReciclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private class FetchRecentVacancies extends AsyncTask<Void, Void, Void> {

        private String mSortOrder;

        public FetchRecentVacancies(String sortOrder) {
            mSortOrder = sortOrder;
        }

        @Override
        public Void doInBackground(Void... params) {

            DBReader dbReader = DBReader.get(getContext());
            mVacanciesList = dbReader.fetchRecentVacancies(mSortOrder);
            dbReader.closeDatabase();

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            setAdapter();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_recent_vacs_fragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sort_by_date:
                new FetchRecentVacancies(DBReader.ORDER_BY_DATE).execute();
                return true;
            case R.id.menu_item_sort_by_update_date:
                new FetchRecentVacancies(DBReader.ORDER_BY_UPDATE_DATE).execute();
                return true;
            case R.id.menu_item_sort_by_update_count:
                new FetchRecentVacancies(DBReader.ORDER_BY_UPDATES_COUNT).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
