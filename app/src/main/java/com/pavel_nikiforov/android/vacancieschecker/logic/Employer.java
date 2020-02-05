package com.pavel_nikiforov.android.vacancieschecker.logic;

import java.io.Serializable;

public class Employer implements Serializable {
    private long employerID;
    private String employerName;
    private long vacanciesCount;
    private String vacanciesLastUpdated;
    private long activityRank;

    public Employer(){
        this.employerID = -1;
        this.vacanciesCount = -1;
        this.employerName = null;
        this.vacanciesLastUpdated = null;
        this.activityRank = -1;
    }

    public long getID() { return employerID; }
    public String getName() { return employerName; }
    public long getCount() { return vacanciesCount; }
    public String getVacanciesLastUpdated() { return vacanciesLastUpdated; }
    public long getRank() { return activityRank; }

    public void setID(long employerID) { this.employerID = employerID; }
    public void setName(String employerName) { this.employerName = employerName; }
    public void setVacanciesCount(long vacanciesCount) { this.vacanciesCount = vacanciesCount; }
    public void setVacanciesLastUpdated(String vacanciesLastUpdated) { this.vacanciesLastUpdated = vacanciesLastUpdated; }
    public void setActivityRank(long activityRank) { this.activityRank = activityRank; }
}
