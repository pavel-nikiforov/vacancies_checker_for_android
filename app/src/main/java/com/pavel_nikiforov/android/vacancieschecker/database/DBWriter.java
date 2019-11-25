package com.pavel_nikiforov.android.vacancieschecker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;

import com.pavel_nikiforov.android.vacancieschecker.database.DBSchema.EmployersTable;
import com.pavel_nikiforov.android.vacancieschecker.database.DBSchema.VacanciesTable;

public class DBWriter {
    public static final long EMPLOYER_NOT_FOUND = -1;
    public static final long VACANCY_NOT_FOUND = -1;

    private static DBWriter sDBWriter;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DBWriter get(Context context) {
        if (sDBWriter == null) {
            sDBWriter = new DBWriter(context);
        }
        if (sDBWriter.mDatabase == null || !sDBWriter.mDatabase.isOpen()){
            sDBWriter.mDatabase = new DBHelper(context).getWritableDatabase();
        }
        return sDBWriter;
    }

    private DBWriter(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(mContext).getWritableDatabase();
    }

    public void closeDatabase(){
        mDatabase.close();
    }

    public long addEmployer(Vacancy vacancy) {
        ContentValues values = new ContentValues();

        values.put(EmployersTable.Cols.EMPLOYER_NAME, vacancy.getEmployerName());

        return mDatabase.insert(EmployersTable.NAME, null, values);

    }

    public long queryEmployerID(Vacancy vacancy) {
        long id;
        String[] projection = {EmployersTable.Cols.EMPLOYER_ID};
        String selection = EmployersTable.Cols.EMPLOYER_NAME + " = ?";
        String[] selectionArgs = {vacancy.getEmployerName()};

        Cursor cursor = mDatabase.query(
                EmployersTable.NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.getCount() == 0) {
            id = EMPLOYER_NOT_FOUND;
        } else {
            cursor.moveToFirst();
            id = cursor.getLong(0);
        }

        cursor.close();
        return id;
    }


    public long queryVacancyID(Vacancy vacancy){
        long id;
        String[] projection = {VacanciesTable.Cols.VACANCY_ID};
        String selection = VacanciesTable.Cols.EMPLOYER_ID + " = ? AND "
                         + VacanciesTable.Cols.VACANCY_NAME + " = ?";
        String[] selectionArgs = {String.valueOf(vacancy.getEmployerID()), vacancy.getName()};

        Cursor cursor = mDatabase.query(
                VacanciesTable.NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.getCount() == 0) {
            id = VACANCY_NOT_FOUND;
        } else {
            cursor.moveToFirst();
            id = cursor.getLong(0);
        }

        cursor.close();
        return id;

    }


    public long addVacancy(Vacancy vacancy, String today_date) {
        ContentValues values = new ContentValues();

        values.put(VacanciesTable.Cols.EMPLOYER_ID, vacancy.getEmployerID());
        values.put(VacanciesTable.Cols.VACANCY_NAME, vacancy.getName());
        values.put(VacanciesTable.Cols.VACANCY_URL, vacancy.getURL());
        values.put(VacanciesTable.Cols.VACANCY_DATE, today_date);
        values.put(VacanciesTable.Cols.VACANCY_LAST_UPDATED, today_date);
        values.put(VacanciesTable.Cols.VACANCY_UPDATES_COUNT, 0);

        vacancy.setVacancyDate(today_date);

        return mDatabase.insert(VacanciesTable.NAME, null, values);
    }

    public boolean updateVacancy(Vacancy vacancy, String today_date, String yesterday_date){
        boolean wasUpdated = false;
        int updatesCount;
        String updateDate;
        String addedDate;
        String[] projection = {VacanciesTable.Cols.VACANCY_LAST_UPDATED, VacanciesTable.Cols.VACANCY_UPDATES_COUNT, VacanciesTable.Cols.VACANCY_DATE};
        String selection = VacanciesTable.Cols.VACANCY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(vacancy.getVacancyID())};


        Cursor cursor = mDatabase.query(
                VacanciesTable.NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.getCount() == 0) {
            // some error handling should be here
        } else {
            cursor.moveToFirst();
            updateDate = cursor.getString(0);
            updatesCount = cursor.getInt(1);
            addedDate = cursor.getString(2);

            Log.d("DBWriter", "Vacancy " + vacancy.getVacancyID() + " update date: " + updateDate + " updates count: " + updatesCount);

            if(!updateDate.contentEquals(today_date) && !updateDate.contentEquals(yesterday_date)){
                ContentValues values = new ContentValues();
                values.put(VacanciesTable.Cols.VACANCY_LAST_UPDATED, today_date);
                values.put(VacanciesTable.Cols.VACANCY_UPDATES_COUNT, updatesCount + 1);

                int count = mDatabase.update(
                        VacanciesTable.NAME,
                        values,
                        selection,
                        selectionArgs);

                Log.d("DBWriter", "Vacancy " + vacancy.getVacancyID() + " update result: " + count);

                wasUpdated = true;
                vacancy.setVacancyDate(addedDate);
                vacancy.setVacancyLastUpdated(updateDate);
                vacancy.setVacancyUpdatesCount(updatesCount + 1);
            } else {
                wasUpdated = false;
                vacancy.setVacancyDate(addedDate);
                vacancy.setVacancyLastUpdated(updateDate);
                vacancy.setVacancyUpdatesCount(updatesCount);
            }
        }

        cursor.close();
        return wasUpdated;
    }

}
