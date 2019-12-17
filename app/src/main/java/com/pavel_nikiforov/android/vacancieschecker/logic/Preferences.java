package com.pavel_nikiforov.android.vacancieschecker.logic;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preferences {
    private static final String PREF_START_URL = "startUrl";
    private static final String PREF_KEYWORDS = "keywords";

    private static final String DEFAULT_START_URL = "https://spb.hh.ru/search/vacancy?text=&specialization=1.117&area=2&salary=&currency_code=RUR&experience=doesNotMatter&order_by=publication_time&search_period=1&items_on_page=50&no_magic=true";
    private static final String DEFAULT_KEYWORDS = "QA,test,Test,quality,Quality,тест,Тест,качеств";

    public static String getStartURL(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_START_URL, DEFAULT_START_URL);
    }

    public static String getKeywords(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_KEYWORDS, DEFAULT_KEYWORDS);
    }

    public static List<String> getKeywordsList(Context context) {
        String keywords = getKeywords(context);
        List<String> keywordsList;
        if(keywords.length() > 0) {
            keywordsList = new ArrayList<String>(Arrays.asList(keywords.split(",")));
        } else {
            keywordsList = new ArrayList<String>();
        }
        return keywordsList;
    }


    public static void setStartURL(Context context, String url) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_START_URL, url)
                .apply();
    }

    public static void setKeywords(Context context, String keywords) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_KEYWORDS, keywords)
                .apply();
    }

    public static void setKeywordsList(Context context, List<String> keywordsList) {
        String keywordsString = "";
        int listSize = keywordsList.size();
        for(int i = 0; i < listSize; i++){
            keywordsString += keywordsList.get(i);
            if(i < listSize - 1){
                keywordsString += ",";
            }
        }

        Preferences.setKeywords(context, keywordsString);
    }

    public static void reset(Context context){
        setStartURL(context, DEFAULT_START_URL);
        setKeywords(context, DEFAULT_KEYWORDS);
    }

}
