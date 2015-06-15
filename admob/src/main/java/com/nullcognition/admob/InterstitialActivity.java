package com.nullcognition.admob;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialActivity extends Activity {

	private Button mShowButton;
	private InterstitialAd interstitialAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interstitial);

		mShowButton = (Button) findViewById(R.id.btn_show_interstitial);
		mShowButton.setEnabled(false);
	}

	public void loadInterstitial(View unusedView) {
		mShowButton.setEnabled(false);
		mShowButton.setText(getResources().getString(R.string.interstitial_loading));

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
		interstitialAd.setAdListener(new ToastAdListener(this){
			@Override
			public void onAdLoaded(){
				super.onAdLoaded();
				mShowButton.setText(getResources().getString(R.string.interstitial_show));
				mShowButton.setEnabled(true);
			}

			@Override
			public void onAdFailedToLoad(int errorCode){
				super.onAdFailedToLoad(errorCode);
				mShowButton.setText(getErrorReason());
			}
		});

		AdRequest ar = new AdRequest.Builder().build();
		interstitialAd.loadAd(ar);
	}

	public void showInterstitial(View unusedView) {
		if (interstitialAd.isLoaded()) {
			interstitialAd.show();
		}

		mShowButton.setText(getResources().getString(R.string.interstitial_not_ready));
		mShowButton.setEnabled(false);
	}
}

