package com.pavel_nikiforov.android.vacancieschecker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pavel_nikiforov.android.vacancieschecker.database.DBSchema.EmployersTable;
import com.pavel_nikiforov.android.vacancieschecker.database.DBSchema.VacanciesTable;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "vacancies.database";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EmployersTable.NAME + " (" +
                EmployersTable.Cols.EMPLOYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EmployersTable.Cols.EMPLOYER_NAME + " CHAR(300)" +
                ")"
        );

        db.execSQL("CREATE TABLE " + VacanciesTable.NAME + " (" +
                VacanciesTable.Cols.VACANCY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VacanciesTable.Cols.EMPLOYER_ID + " INTEGER," +
                VacanciesTable.Cols.VACANCY_NAME + " CHAR(300)," +
                VacanciesTable.Cols.VACANCY_URL + " CHAR(300)," +
                VacanciesTable.Cols.VACANCY_DATE + " CHAR(10)," +
                VacanciesTable.Cols.VACANCY_LAST_UPDATED + " CHAR(10)," +
                VacanciesTable.Cols.VACANCY_UPDATES_COUNT + " INTEGER" +
                ")"
        );

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
