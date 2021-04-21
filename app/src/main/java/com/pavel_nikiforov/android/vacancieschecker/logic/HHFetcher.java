package com.pavel_nikiforov.android.vacancieschecker.logic;

import android.content.Context;
import android.util.Log;

import com.pavel_nikiforov.android.vacancieschecker.database.DBWriter;
import com.pavel_nikiforov.android.vacancieschecker.logic.Vacancy;
import com.pavel_nikiforov.android.vacancieschecker.logic.LoggedAsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HHFetcher{
    private LoggedAsyncTask caller;
    private static final String tag = "Fetcher";
    private static String user_agent = "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:69.0) Gecko/20100101 Firefox/71.0";
    private static String vacancy_list_url = "https://spb.hh.ru/search/vacancy?text=&specialization=1.117&area=2&salary=&currency_code=RUR&experience=doesNotMatter&order_by=publication_time&search_period=1&items_on_page=50&no_magic=true";

    private static String block_selector = "div.vacancy-serp-item[data-qa^='vacancy-serp__vacancy']";

    private static String name_selector = "a[data-qa='vacancy-serp__vacancy-title']";
    private static String employer_selector = "a[data-qa='vacancy-serp__vacancy-employer']";

    private static String pager_selector = "a[data-qa='pager-next']";

    private static String accepted_keywords = "QA,test,Test,quality,Quality,тест,Тест,качеств";


    private static void log(LoggedAsyncTask caller, String message){
        caller.postLog(message);
    }

    public static void getVacancies(List<Vacancy> vacancies, LoggedAsyncTask caller) {
        String startURL = VacancyChecker.getInstance().getStartURL();
        String nextPage = null;

        nextPage = getPage(startURL, vacancies, caller);

        while (nextPage != null) {
            nextPage = getPage(nextPage, vacancies, caller);
        }
    }


    private static String getPage(String url, List<Vacancy> vacancies, LoggedAsyncTask caller) {

        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent(user_agent)
                    .get();
            Log.d(tag, "--- got title: " + doc.title());
            log(caller, "--- got title: " + doc.title());
//            Elements vacBlocks = doc.select("div[data-qa^='vacancy-serp__vacancy vacancy-serp__vacancy_']");
            Elements vacBlocks = doc.select(block_selector);
            Log.d(tag, "--- found blocks: " + vacBlocks.size());
            log(caller, "--- found blocks: " + vacBlocks.size());

            for (Element block : vacBlocks) {

                String name;
                name = block.select(name_selector).get(0).text();

                String link;
                link = block.select(name_selector).get(0).attr("href");

                String employer;
                if(block.select(employer_selector).size() != 0) {
                    employer = block.select(employer_selector).get(0).text();
                } else {
                    employer = "Unknown Employer";
                }

                Vacancy vacancy = new Vacancy();
                vacancy.setName(name);
                vacancy.setEmployerName(employer);
                vacancy.setURL(link);

                vacancies.add(vacancy);
                Log.d(tag, "--- name: " + name);
                Log.d(tag, "--- link: " + link);
                Log.d(tag, "--- employer: " + employer);

                log(caller, "--- name: " + name);
                log(caller, "--- employer: " + employer);
                log(caller, "--- link: " + link);

            }

            Elements pager = doc.select(pager_selector);
            if (pager.size() > 0) {
                String linkToNextPage = url.substring(0, url.indexOf("/", 8));
                linkToNextPage = linkToNextPage + pager.get(0).attr("href");
                Log.d(tag, "--- next page link: " + linkToNextPage);
                return linkToNextPage;
            } else {
                Log.d(tag, "--- no next page link");
                return null;
            }


        } catch (IOException ioe) {
            Log.d(tag, "--- IOException occured: ");
            Log.d(tag, ioe.getMessage());
        }

        return null;
    }


    public static void filterVacancies(List<Vacancy> unfiltered_list, List<Vacancy> accepted_list,  List<Vacancy> rejected_list,LoggedAsyncTask caller) {

        List<String> keywords = VacancyChecker.getInstance().getKeywordsList();
        Log.d(tag, "--- got Keywords list: " + keywords);

        for (Vacancy vacancy : unfiltered_list) {
            boolean isAccepted = false;
            for (String keyword : keywords) {
                if (vacancy.getName().contains(keyword)) {
                    accepted_list.add(vacancy);
                    isAccepted = true;
                    Log.d(tag, "--- accepted: " + vacancy.getName());
                    log(caller, "--- accepted: " + vacancy.getName());
                    break;
                }
            }
            if (!isAccepted) {
                rejected_list.add(vacancy);
                Log.d(tag, "--- rejected: " + vacancy.getName());
                log(caller, "--- rejected: " + vacancy.getName());
            }

        }
    }


    private static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }


    private static String getYesterdayDate(){
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    public static void saveVacancies(Context context, List<Vacancy> filtered_list, List<Vacancy> new_vacancies_list, List<Vacancy> new_emploers_list, List<Vacancy> updated_vacancies_list, LoggedAsyncTask caller){
        DBWriter db = DBWriter.get(context);

        String today_date = getTodayDate();
        String yesterday_date = getYesterdayDate();

        for (Vacancy vacancy : filtered_list){
            long employer_id = db.queryEmployerID(vacancy);
            if(employer_id == db.EMPLOYER_NOT_FOUND){
                employer_id = db.addEmployer(vacancy);
                log(caller, "+++ new employer: " + vacancy.getEmployerName());
                vacancy.setEmployerID(employer_id);
                db.addVacancy(vacancy, today_date);
                log(caller, "+++ new employer vacancy: " + vacancy.getName());
                new_emploers_list.add(vacancy);
            } else {
                vacancy.setEmployerID(employer_id);
                long vacancy_id = db.queryVacancyID(vacancy);
                if(vacancy_id == db.VACANCY_NOT_FOUND){
                    db.addVacancy(vacancy, today_date);
                    log(caller, "+++ new vacancy: " + vacancy.getName());
                    new_vacancies_list.add(vacancy);
                } else {
                    vacancy.setVacancyID(vacancy_id);
                    if (db.updateVacancy(vacancy, today_date, yesterday_date)) {
                        updated_vacancies_list.add(vacancy);
                        log(caller, "*** vacancy updated: " + vacancy.getName());
                    }
                }
            }

        }

        db.closeDatabase();
    }
}
