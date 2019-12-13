package br.gov.sp.educacao.minhaescola.services;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.AsyncTask;

import android.util.Log;

import org.jsoup.Jsoup;

import br.gov.sp.educacao.minhaescola.interfaces.WSCallerVersionListener;

public class GooglePlayStoreAppVersion
        extends AsyncTask<String, Void, String> {

    WSCallerVersionListener mWsCallerVersionListener;

    private String newVersion = "";
    private String currentVersion = "";
    private boolean isVersionAvailabel;
    private boolean isAvailableInPlayStore;
    private Context mContext;
    private String mStringCheckUpdate = "";

    public GooglePlayStoreAppVersion(Context mContext, WSCallerVersionListener callback) {

        mWsCallerVersionListener = callback;

        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {

        try {

            isAvailableInPlayStore = true;

            mStringCheckUpdate = Jsoup.connect(
                    "https://play.google.com/store/apps/details?id=" + mContext.getPackageName())
                    .timeout(30000)
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();

            return mStringCheckUpdate;
        }
        catch (Throwable e) {

            isAvailableInPlayStore = false;

            return mStringCheckUpdate;
        }
    }

    @Override
    protected void onPostExecute(String string) {

        if (isAvailableInPlayStore) {

            newVersion = string;

            checkApplicationCurrentVersion();

            if (currentVersion.equalsIgnoreCase(newVersion)) {

                isVersionAvailabel = false;
            }
            else {

                isVersionAvailabel = true;
            }
            mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
        }
    }

    private void checkApplicationCurrentVersion() {

        PackageManager packageManager = mContext.getPackageManager();

        PackageInfo packageInfo = null;

        try {

            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);

        }
        catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        if (packageInfo != null) {

            currentVersion = packageInfo.versionName;
        }
    }
}