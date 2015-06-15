package com.nullcognition.admob;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BannerActivity extends Activity{
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner);

		adView = (AdView) findViewById(R.id.adView);
		adView.setAdListener(new ToastAdListener(this));
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

}
