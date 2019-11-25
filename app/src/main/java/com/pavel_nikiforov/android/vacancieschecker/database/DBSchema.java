package com.pavel_nikiforov.android.vacancieschecker.database;

public class DBSchema {

    public static final class EmployersTable{
        public static final String NAME = "EMPLOYERS";

        public static final class Cols {
            public static final String EMPLOYER_ID = "EmployerID";
            public static final String EMPLOYER_NAME = "EmployerName";
        }
    }

    public static final class VacanciesTable{
        public static final String NAME = "VACANCIES";

        public static final class Cols {
            public static final String VACANCY_ID = "VacancyID";
            public static final String EMPLOYER_ID = "EmployerID";
            public static final String VACANCY_NAME = "VacancyName";
            public static final String VACANCY_URL = "VacancyURL";
            public static final String VACANCY_DATE = "VacancyDate";
            public static final String VACANCY_LAST_UPDATED = "VacancyLastUpdated";
            public static final String VACANCY_UPDATES_COUNT = "VacancyUpdatesCount";
        }
    }
}
