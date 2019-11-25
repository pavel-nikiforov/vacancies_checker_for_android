package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

public class StatsFragment extends Fragment {
    private TextView mNewEmpText;
    private TextView mNewVacText;
    private TextView mUpdatedVacText;
    private TextView mAcceptedVacText;
    private TextView mRejectedVacText;
    private TextView mTotalVacText;

    private Button mNewEmpButton;
    private Button mNewVacButton;
    private Button mUpdatedVacButton;
    private Button mAcceptedVacButton;
    private Button mRejectedVacButton;
    private Button mTotalVacButton;

    private TextView mJunkStatText;
    private TextView mNewStatText;


    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mNewEmpText = (TextView) view.findViewById(R.id.stats_new_emp_number);
        mNewVacText = (TextView) view.findViewById(R.id.stats_new_vac_number);
        mUpdatedVacText = (TextView) view.findViewById(R.id.stats_updated_vac_number);
        mAcceptedVacText = (TextView) view.findViewById(R.id.stats_accepted_vac_number);
        mRejectedVacText = (TextView) view.findViewById(R.id.stats_rejected_vac_number);
        mTotalVacText = (TextView) view.findViewById(R.id.stats_total_vac_number);

        mJunkStatText = (TextView) view.findViewById(R.id.stats_junk_stat);
        mNewStatText = (TextView) view.findViewById(R.id.stats_new_stat);

        populateStats();

        mNewEmpButton = (Button) view.findViewById(R.id.stats_new_emp_button);
        mNewEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEmployersFragment nextFrag = new NewEmployersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


        mNewVacButton = (Button) view.findViewById(R.id.stats_new_vac_button);
        mNewVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTo("new");
            }
        });


        mUpdatedVacButton = (Button) view.findViewById(R.id.stats_updated_vac_button);
        mUpdatedVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTo("updated");
            }
        });


        mAcceptedVacButton = (Button) view.findViewById(R.id.stats_accepted_vac_button);
        mAcceptedVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTo("accepted");
            }
        });

        mRejectedVacButton = (Button) view.findViewById(R.id.stats_rejected_vac_button);
        mRejectedVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTo("rejected");
            }
        });


        mTotalVacButton = (Button) view.findViewById(R.id.stats_total_vac_button);
        mTotalVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTo("total");
            }
        });



        return view;
    }


    private void proceedTo(String destination){
        Bundle args = new Bundle();
        args.putString("type", destination);

        VacanciesListFragment nextFrag = new VacanciesListFragment();
        nextFrag.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }


    private void populateStats(){
        VacancyChecker app = VacancyChecker.getInstance();

        int newEmployers = app.getNewEmployersList().size();
        int newVacancies = app.getNewVacanciesList().size();
        int updVacancies = app.getUpdatedVacanciesList().size();
        int acceptedVacancies = app.getAcceptedVacanciesList().size();
        int rejectedVacancies = app.getRejectedVacanciesList().size();
        int totalVacancies = app.getRawVacanciesList().size();

        mNewEmpText.setText(String.valueOf(newEmployers));
        mNewVacText.setText(String.valueOf(newVacancies));
        mUpdatedVacText.setText(String.valueOf(updVacancies));
        mAcceptedVacText.setText(String.valueOf(acceptedVacancies));
        mRejectedVacText.setText(String.valueOf(rejectedVacancies));
        mTotalVacText.setText(String.valueOf(totalVacancies));

        int junkPercent = (int) (((float) rejectedVacancies / (float) totalVacancies) * 100);
        int newPercent = (int) (((float) (newEmployers + newVacancies) / (float) acceptedVacancies) * 100);


        mJunkStatText.setText(String.format(getString(R.string.stats_junk_stat_text),
                junkPercent,
                rejectedVacancies,
                totalVacancies));

        mNewStatText.setText(String.format(getString(R.string.stats_new_stat_text),
                newPercent,
                newEmployers + newVacancies,
                acceptedVacancies));



    }
}
