package com.nullcognition.analytics;// Created by ersin on 22/06/15

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

public class MyApp extends Application{

	Tracker tracker;

	public void startTracking(){
		if(tracker == null){
			GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
			tracker = ga.newTracker(R.xml.analytics_id);
			ga.enableAutoActivityReports(this);
			ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		}
	}

	public Tracker getTracker(){
		startTracking();
		return tracker;
	}

}
