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

import java.util.List;

public class NewEmployersFragment extends Fragment {
    private RecyclerView mEmployersReciclerView;
    //    private UpdateFragment.LogAdapter mLogListAdapter;
    private List<Vacancy> mEmployersList;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        VacancyChecker app = VacancyChecker.getInstance();
        mEmployersList = app.getNewEmployersList();

        View view = inflater.inflate(R.layout.fragment_new_empl, container, false);
        mEmployersReciclerView = (RecyclerView) view.findViewById(R.id.empl_recyclerview);
        mEmployersReciclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmployersReciclerView.setAdapter(new EmplListAdapter());

        return view;
    }





    private class EmplListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mEmployerNameTextView;
        private TextView mVacancyNameTextView;
        private Vacancy mVacancy;

        public EmplListHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_employer, parent, false));

            mEmployerNameTextView = (TextView) itemView.findViewById(R.id.empl_cardview_empl_name);
            mVacancyNameTextView = (TextView) itemView.findViewById(R.id.empl_cardview_vac_name);

            itemView.setOnClickListener(this);
        }

        public void bind(Vacancy vacancy){
            mVacancy = vacancy;

            mEmployerNameTextView.setText(mVacancy.getEmployerName());
            mVacancyNameTextView.setText(mVacancy.getName());
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), mVacancy.getName() + " clicked", Toast.LENGTH_SHORT).show();

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




    private class EmplListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new EmplListHolder(inflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

            ((EmplListHolder) holder).bind(mEmployersList.get(position));
        }

        @Override
        public int getItemCount(){
            return mEmployersList.size();
        }
    }
}
