package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import java.util.ArrayList;
import java.util.List;

public class VacanciesListFragment extends Fragment {
    private String mHeadlineString;
    private String mListType;
    private List<Vacancy> mVacanciesList;

    private TextView mHeadlineTextView;
    private RecyclerView mVacanciesReciclerView;

    public VacanciesListFragment(){


    }


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        Bundle args = getArguments();
        mListType = args.getString("type", "default");

        VacancyChecker app = VacancyChecker.getInstance();
        switch (mListType){
            case "new":
                mHeadlineString = getString(R.string.vacancies_headline_new);
                mVacanciesList = app.getNewVacanciesList();
                break;
            case "updated":
                mHeadlineString = getString(R.string.vacancies_headline_updated);
                mVacanciesList = app.getUpdatedVacanciesList();
                break;
            case "accepted":
                mHeadlineString = getString(R.string.vacancies_headline_accepted);
                mVacanciesList = app.getAcceptedVacanciesList();
                break;
            case "rejected":
                mHeadlineString = getString(R.string.vacancies_headline_rejected);
                mVacanciesList = app.getRejectedVacanciesList();
                break;
            case "total":
                mHeadlineString = getString(R.string.vacancies_headline_total);
                mVacanciesList = app.getRawVacanciesList();
                break;
            case "default":
                mHeadlineString = "Default headline:";
                mVacanciesList = new ArrayList<Vacancy>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancies_list, container, false);

        mHeadlineTextView = (TextView) view.findViewById(R.id.vaclist_headline);
        mHeadlineTextView.setText(mHeadlineString);

        mVacanciesReciclerView = (RecyclerView) view.findViewById(R.id.vaclist_recyclerview);
        mVacanciesReciclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVacanciesReciclerView.setAdapter(new VacanciesListAdapter());

        return view;
    }


    private class VacanciesListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mEmployerNameTextView;
        private TextView mVacancyNameTextView;
        private TextView mLastUpdatedNameTextView;
        private TextView mLastUpdatedTextView;
        private TextView mUpdateCountNameTextView;
        private TextView mUpdateCountTextView;
        private Vacancy mVacancy;

        public VacanciesListHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_vacancy, parent, false));

            mEmployerNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_secondary);
            mVacancyNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_primary);

            mLastUpdatedNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updated_date_name_name);
            mLastUpdatedTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updated_date);
            mUpdateCountNameTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updates_count_name);
            mUpdateCountTextView = (TextView) itemView.findViewById(R.id.vacancy_cardview_updates_count);

//            if(mListType.contentEquals("updated")){
//
//            } else {
//                mLastUpdatedTextView.setVisibility(View.GONE);
//                mUpdateCountTextView.setVisibility(View.GONE);
//            }

            itemView.setOnClickListener(this);
        }

        public void bind(Vacancy vacancy){
            mVacancy = vacancy;

            mEmployerNameTextView.setText(mVacancy.getEmployerName());
            mVacancyNameTextView.setText(mVacancy.getName());

            if(mListType.contentEquals("updated")){
                mLastUpdatedTextView.setText(mVacancy.getVacancyLastUpdated());
                mUpdateCountTextView.setText(String.valueOf(mVacancy.getVacancyUpdatesCount()));

            } else {
                mLastUpdatedNameTextView.setVisibility(View.GONE);
                mUpdateCountNameTextView.setVisibility(View.GONE);
                mLastUpdatedTextView.setVisibility(View.GONE);
                mUpdateCountTextView.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View view){
//            Toast.makeText(getActivity(), mVacancy.getName() + " clicked", Toast.LENGTH_SHORT).show();

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




    private class VacanciesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new VacanciesListHolder(inflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

            ((VacanciesListHolder) holder).bind(mVacanciesList.get(position));
        }

        @Override
        public int getItemCount(){
            return mVacanciesList.size();
        }
    }
}
