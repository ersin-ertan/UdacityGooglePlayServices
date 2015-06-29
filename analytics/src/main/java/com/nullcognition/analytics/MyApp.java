package com.nullcognition.analytics;// Created by ersin on 22/06/15

import android.app.Application;

import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

public class MyApp extends Application{

	private TagManager tagManager;
	private ContainerHolder containerHolder;

	public ContainerHolder getContainerHolder(){
		return containerHolder;
	}
	public void setContainerHolder(final ContainerHolder containerHolder){
		this.containerHolder = containerHolder;
	}

	@Override
	public void onCreate(){
		super.onCreate();

		AnalyticCalls.init(this);

	}
//	public void startTracking(){
//		// if low memory killer was to clean resources of the app, this would spawn a new one
//		// also lazy instantiation, set on first need
//		if(tracker == null){
//			GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
//			tracker = ga.newTracker(R.xml.analytics_id);
//			ga.enableAutoActivityReports(this);
//			ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
//		}
//	}
//
//	public Tracker getTracker(){
//		startTracking();
//		return tracker;
//	}

	public TagManager getTagManager(){
		if(tagManager == null){ tagManager = TagManager.getInstance(this);}
		tagManager.setVerboseLoggingEnabled(true);
		return tagManager;
	}

	static final String s = "GTM";
}
