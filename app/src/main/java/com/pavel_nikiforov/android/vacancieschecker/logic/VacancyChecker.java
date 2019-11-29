package com.pavel_nikiforov.android.vacancieschecker.logic;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class VacancyChecker extends Application {
    private static VacancyChecker sInstance;

    private List<Vacancy> mRawVacanciesList;
    private List<Vacancy> mRejectedVacanciesList;
    private List<Vacancy> mAcceptedVacanciesList;
    private List<Vacancy> mUpdatedVacanciesList;
    private List<Vacancy> mNewVacanciesList;
    private List<Vacancy> mNewEmployersList;

    public static VacancyChecker getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }


    public void setRawVacanciesList(List<Vacancy> rawVacanciesList) { mRawVacanciesList = rawVacanciesList; }
    public void setRejectedVacanciesList(List<Vacancy> rejectedVacanciesList) { mRejectedVacanciesList = rejectedVacanciesList; }
    public void setAcceptedVacanciesList(List<Vacancy> acceptedVacanciesList) { mAcceptedVacanciesList = acceptedVacanciesList; }
    public void setUpdatedVacanciesList(List<Vacancy> updatedVacanciesList) { mUpdatedVacanciesList = updatedVacanciesList; }
    public void setNewVacanciesList(List<Vacancy> newVacanciesList) { mNewVacanciesList = newVacanciesList; }
    public void setNewEmployersList(List<Vacancy> newEmployersList) { mNewEmployersList = newEmployersList; }

    public List<Vacancy> getRawVacanciesList() {
        if (mRawVacanciesList == null) return new ArrayList<Vacancy>();
        return mRawVacanciesList;
    }

    public List<Vacancy> getRejectedVacanciesList() {
        if (mRejectedVacanciesList == null) return new ArrayList<Vacancy>();
        return mRejectedVacanciesList;
    }

    public List<Vacancy> getAcceptedVacanciesList() {
        if (mAcceptedVacanciesList == null) return new ArrayList<Vacancy>();
        return mAcceptedVacanciesList;
    }

    public List<Vacancy> getUpdatedVacanciesList() {
        if (mUpdatedVacanciesList == null) return new ArrayList<Vacancy>();
        return mUpdatedVacanciesList;
    }

    public List<Vacancy> getNewVacanciesList() {
        if (mNewVacanciesList == null) return new ArrayList<Vacancy>();
        return mNewVacanciesList;
    }

    public List<Vacancy> getNewEmployersList() {
        if (mNewEmployersList == null) return new ArrayList<Vacancy>();
        return mNewEmployersList;
    }

}
