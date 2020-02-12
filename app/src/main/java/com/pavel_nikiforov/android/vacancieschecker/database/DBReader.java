package com.pavel_nikiforov.android.vacancieschecker.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pavel_nikiforov.android.vacancieschecker.logic.Employer;
import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class DBReader {
    public static final String ORDER_BY_NAME = "2";
    public static final String ORDER_BY_NAME_DESC = "2 desc";
    public static final String ORDER_BY_COUNT = "3";
    public static final String ORDER_BY_COUNT_DESC = "3 desc";
    public static final String ORDER_BY_RANK = "5";
    public static final String ORDER_BY_RANK_DESC = "5 desc";
    public static final String ORDER_BY_UPDATE_DATE = "\"VacancyLastUpdated\" desc, \"VacancyDate\" desc";
    public static final String ORDER_BY_DATE = "\"VacancyDate\" desc, \"VacancyLastUpdated\" desc";
    public static final String ORDER_BY_UPDATES_COUNT = "\"VacancyUpdatesCount\" desc, \"VacancyLastUpdated\" desc";


    private static DBReader sDBReader;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DBReader get(Context context) {
        if (sDBReader == null) {
            sDBReader = new DBReader(context);
        }
        if (sDBReader.mDatabase == null || !sDBReader.mDatabase.isOpen()){
            sDBReader.mDatabase = new DBHelper(context).getWritableDatabase();
        }
        return sDBReader;
    }

    private DBReader(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(mContext).getWritableDatabase();
    }

    public void closeDatabase(){
        mDatabase.close();
    }

    public List<Employer> fetchEmployers(String sortOrder){
        List<Employer> employers = new ArrayList<Employer>();

        String query = "select e.\"EmployerID\", e.\"EmployerName\", count(v.\"VacancyID\"), max(v.\"VacancyLastUpdated\"), count(v.\"VacancyID\") + sum(v.\"VacancyUpdatesCount\") " +
                "from EMPLOYERS as e inner join VACANCIES as v on e.\"EmployerID\" = v.\"EmployerID\" " +
                "group by e.\"EmployerID\" order by ";

        Cursor cursor = mDatabase.rawQuery(query + sortOrder, null);
        int count = cursor.getCount();
        if (count == 0) {
//            some error handling here
        } else {
            cursor.moveToFirst();
            for(int i=0; i < count; i++){
                Employer employer = new Employer();
                employer.setID(cursor.getLong(0));
                employer.setName(cursor.getString(1));
                employer.setVacanciesCount(cursor.getLong(2));
                employer.setVacanciesLastUpdated(cursor.getString(3));
                employer.setActivityRank(cursor.getLong(4));
                employers.add(employer);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return employers;
    }


    public List<Vacancy> fetchVacanciesByEmployer(Employer employer){
        List<Vacancy> vacancies = new ArrayList<Vacancy>();

        String query_head = "select * from VACANCIES where EmployerID = ";
        String query_tail = " order by 6 desc";
        String query = query_head + String.valueOf(employer.getID()) + query_tail;

        Cursor cursor = mDatabase.rawQuery(query, null);
        int count = cursor.getCount();
        if (count == 0) {
//            some error handling here
        } else {
            cursor.moveToFirst();
            for(int i=0; i < count; i++){
                Vacancy vacancy = new Vacancy();
                vacancy.setVacancyID(cursor.getLong(0));
                vacancy.setEmployerID(cursor.getLong(1));
                vacancy.setName(cursor.getString(2));
                vacancy.setURL(cursor.getString(3));
                vacancy.setVacancyDate(cursor.getString(4));
                vacancy.setVacancyLastUpdated(cursor.getString(5));
                vacancy.setVacancyUpdatesCount(cursor.getInt(6));
                vacancy.setEmployerName(employer.getName());

                vacancies.add(vacancy);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return vacancies;
    }


    public List<Employer> fetchSearchEmployerResults(String search_query){
        List<Employer> employers = new ArrayList<Employer>();

        String query_head = "select e.\"EmployerID\", e.\"EmployerName\", count(v.\"VacancyID\"), max(v.\"VacancyLastUpdated\"), count(v.\"VacancyID\") + sum(v.\"VacancyUpdatesCount\"), lower(e.\"EmployerName\") " +
                "from EMPLOYERS as e inner join VACANCIES as v on e.\"EmployerID\" = v.\"EmployerID\" " +
                "where lower(e.\"EmployerName\") like '%";

        String query_tail = "%' group by e.\"EmployerID\" order by 2 asc";

        Cursor cursor = mDatabase.rawQuery(query_head + search_query + query_tail, null);
        int count = cursor.getCount();
        if (count == 0) {
//            some error handling here
        } else {
            cursor.moveToFirst();
            for(int i=0; i < count; i++){
                Employer employer = new Employer();
                employer.setID(cursor.getLong(0));
                employer.setName(cursor.getString(1));
                employer.setVacanciesCount(cursor.getLong(2));
                employer.setVacanciesLastUpdated(cursor.getString(3));
                employer.setActivityRank(cursor.getLong(4));
                employers.add(employer);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return employers;
    }



    public List<Vacancy> fetchRecentVacancies(String sortOrder){
        List<Vacancy> vacancies = new ArrayList<Vacancy>();

        String query_head = "select * from VACANCIES as v join EMPLOYERS as e on v.\"EmployerID\" = e.\"EmployerID\" order by ";
        String query_tail = " limit 100";

        Cursor cursor = mDatabase.rawQuery(query_head + sortOrder + query_tail, null);
        int count = cursor.getCount();
        if (count == 0) {
//            some error handling here
        } else {
            cursor.moveToFirst();
            for(int i=0; i < count; i++){
                Vacancy vacancy = new Vacancy();
                vacancy.setVacancyID(cursor.getLong(0));
                vacancy.setEmployerID(cursor.getLong(1));
                vacancy.setName(cursor.getString(2));
                vacancy.setURL(cursor.getString(3));
                vacancy.setVacancyDate(cursor.getString(4));
                vacancy.setVacancyLastUpdated(cursor.getString(5));
                vacancy.setVacancyUpdatesCount(cursor.getInt(6));
                vacancy.setEmployerName(cursor.getString(8));

                vacancies.add(vacancy);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return vacancies;
    }



}
