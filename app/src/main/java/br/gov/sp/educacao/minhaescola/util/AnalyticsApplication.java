package br.gov.sp.educacao.minhaescola.util;

import android.app.Activity;
import android.app.Application;

public class AnalyticsApplication
        extends Application {

}

/*private static final String PROPERTY_ID = "UA-109306828-1";

    public enum TrackerName
    {
        APP_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public AnalyticsApplication()
    {
        super();
    }
    public synchronized  Tracker getTracker(TrackerName trackerId)
    {
        if(!mTrackers.containsKey(trackerId))
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if(trackerId == TrackerName.APP_TRACKER)
            {
                Tracker t = analytics.newTracker(PROPERTY_ID);
                mTrackers.put(trackerId, t);
            }
        }
        return mTrackers.get(trackerId);
    }*/
