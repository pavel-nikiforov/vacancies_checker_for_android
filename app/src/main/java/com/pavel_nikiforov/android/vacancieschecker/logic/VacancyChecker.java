package com.pavel_nikiforov.android.vacancieschecker.logic;

import android.app.Application;

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

    public List<Vacancy> getRawVacanciesList() { return mRawVacanciesList; }
    public List<Vacancy> getRejectedVacanciesList() { return mRejectedVacanciesList; }
    public List<Vacancy> getAcceptedVacanciesList() { return mAcceptedVacanciesList; }
    public List<Vacancy> getUpdatedVacanciesList() { return mUpdatedVacanciesList; }
    public List<Vacancy> getNewVacanciesList() { return mNewVacanciesList; }
    public List<Vacancy> getNewEmployersList() { return mNewEmployersList; }

}
