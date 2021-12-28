package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import java.util.ArrayList;

public class VacancyDetailsFragment extends Fragment {
    private Vacancy mVacancy;

    private TextView mEmployerTextView;
    private TextView mVacacyNameTextView;
    private TextView mAddedDateTextView;
    private TextView mUpdatedDateTextView;
    private TextView mUpdatesCountTextView;
    private Button mOpenInViewButton;
    private Button mOpenInBrowserButton;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        Bundle args = getArguments();
        mVacancy = (Vacancy) args.getSerializable("vacancy");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancy_details, container, false);

        mEmployerTextView = (TextView) view.findViewById(R.id.vacdetails_employer);
        mEmployerTextView.setText(mVacancy.getEmployerName());
        mEmployerTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "emp selected " + mEmployerTextView.getText(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mVacacyNameTextView = (TextView) view.findViewById(R.id.vacdetails_vacancy);
        mVacacyNameTextView.setText(mVacancy.getName());

        mAddedDateTextView = (TextView) view.findViewById(R.id.vacdetails_addition_date);
        if(mVacancy.getVacancyDate() == null) {
            mAddedDateTextView.setText("---");
        } else {
            mAddedDateTextView.setText(mVacancy.getVacancyDate());
        }

        mUpdatedDateTextView = (TextView) view.findViewById(R.id.vacdetails_update_date);
        if(mVacancy.getVacancyLastUpdated() == null) {
            mUpdatedDateTextView.setText("---");
        } else {
            mUpdatedDateTextView.setText(mVacancy.getVacancyLastUpdated());
        }

        mUpdatesCountTextView = (TextView) view.findViewById(R.id.vacdetails_updates_count);
        if(mVacancy.getVacancyUpdatesCount() == -1) {
            mUpdatesCountTextView.setText("---");
        } else {
            mUpdatesCountTextView.setText(String.valueOf(mVacancy.getVacancyUpdatesCount()));
        }

        mOpenInViewButton = (Button) view.findViewById(R.id.vacdetails_view_view_btn);
        mOpenInViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("url", mVacancy.getURL());

                VacancyWebViewFragment nextFrag = new VacancyWebViewFragment();
                nextFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


        mOpenInBrowserButton = (Button) view.findViewById(R.id.vacdetails_view_browser_btn);
        mOpenInBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mVacancy.getURL()));
                startActivity(browserIntent);
            }
        });


        return view;
    }
}
