package com.nullcognition.admob.pract00;// Created by ersin on 27/06/15

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Pract00{

	/*
		<string name = "banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string>
		<string name = "interstitial_ad_unit_id">ca-app-pub-3940256099942544/1033173712</string>
	*/
	// ads ids for development use, as using real ads is only for deployment as per terms of service
		Context context = null;
	{
		AdView adView = new AdView(context);
		adView.setAdListener(new AdListener(){ });
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	{
		InterstitialAd ia = new InterstitialAd(context);
		// can set the unit id in xml or here
		ia.setAdUnitId("333");
		ia.loadAd(new AdRequest.Builder().build());

	}
}
