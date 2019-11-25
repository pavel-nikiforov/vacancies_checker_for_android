package com.pavel_nikiforov.android.vacancieschecker.logic;

import java.io.Serializable;

public class Vacancy implements Serializable {
    private String vacancyName;
    private long vacancyID;

    private String employerName;
    private long employerID;

    private String vacancyURL;
    private String vacancyDate;
    private String vacancyLastUpdated;
    private int vacancyUpdatesCount;

    public Vacancy(){
        this.employerID = -1;
        this.vacancyID = -1;
        this.vacancyDate = null;
        this.vacancyLastUpdated = null;
        this.vacancyUpdatesCount = -1;
    }


    public String getName() { return vacancyName; }

    public void setName(String vacancyName) { this.vacancyName = vacancyName; }

    public long getVacancyID() { return vacancyID; }

    public void setVacancyID(long vacancyID) { this.vacancyID = vacancyID; }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public long getEmployerID() { return employerID; }

    public void setEmployerID(long employerID) { this.employerID = employerID; }

    public void setURL(String vacancyURL) {
        this.vacancyURL = vacancyURL;
    }

    public String getURL() {
        return vacancyURL;
    }

    public void setVacancyDate(String vacancyDate) { this.vacancyDate = vacancyDate; }

    public String getVacancyDate() { return vacancyDate; }

    public void setVacancyLastUpdated(String vacancyLastUpdated) { this.vacancyLastUpdated = vacancyLastUpdated; }

    public String getVacancyLastUpdated() { return vacancyLastUpdated; }

    public void setVacancyUpdatesCount(int vacancyUpdatesCount) { this.vacancyUpdatesCount = vacancyUpdatesCount; }

    public int getVacancyUpdatesCount() { return vacancyUpdatesCount; }
}
