package com.pavel_nikiforov.android.vacancieschecker.logic;

import android.os.AsyncTask;

public abstract class LoggedAsyncTask extends AsyncTask<Void, String, Void> {

    public void postLog(String message){
        publishProgress(message);
    }

}
