/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.nullcognition.analytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity{

	TagManager tagManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		((MyApp) getApplication()).startTracking();
		loadGTMContainer();
	}
	private void loadGTMContainer(){
		tagManager = ((MyApp) getApplication()).getTagManager();
		tagManager.setVerboseLoggingEnabled(true);
		PendingResult pendingResult = tagManager.loadContainerPreferFresh(MyApp.s + "-PC4NQH",
				R.raw.gtm_pc4nqh_v1);

		pendingResult.setResultCallback(new ResultCallback<ContainerHolder>(){
			@Override
			public void onResult(final ContainerHolder containerHolder){
				if(!containerHolder.getStatus().isSuccess()){
					return;
				}
				containerHolder.refresh();

				((MyApp) getApplication()).setContainerHolder(containerHolder);
			}
		}, 2, TimeUnit.SECONDS);
	}

	public void showFoodPrefsMenu(View view){
		android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.food_prefs_menu, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){

				getDinnerSuggestion(item.getItemId());
				return true;
			}
		});
		popup.show();
	}

	public void showDinners(final View view){
		startActivity(new Intent(this, AllDinners.class));
	}

	public void showDaily(final View view){

		android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.food_prefs_menu, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){

				putFoodPrefInDataLayer(item);
				startDailySpecialActivity();
				return true;
			}
		});
		popup.show();

	}

	public String getDinnerSuggestion(int item){

		String dinnerChoice = new Dinner(this, item).getDinnerTonight();
		Intent dinnerIntent = new Intent(this, ShowDinnerActivity.class);

		dinnerIntent.putExtra(String.valueOf(R.string.selected_dinner), dinnerChoice);
		startActivity(dinnerIntent);

		return dinnerChoice;
	}

	public void putFoodPrefInDataLayer(MenuItem item){

		TagManager tm = ((MyApp) getApplication()).getTagManager();
		DataLayer dl = tm.getDataLayer();
		String foodPref;

		switch(item.getItemId()){
			case R.id.vegan_pref:
				foodPref = "vegan";
				break;
			case R.id.vegetarian_pref:
				foodPref = "vegetarian";
				break;
			case R.id.fish_pref:
				foodPref = "fish";
				break;
			case R.id.meat_pref:
				foodPref = "meat";
				break;
			default:
				foodPref = "unrestricted";
		}
		dl.push("food_pref", foodPref);


	}

	public void startDailySpecialActivity(){

		// old way, without data layer
//		String dinnerChoice = new Dinner(this, 0).getDinnerTonight(); // 0 will default to unrestricted
//		Intent dinnerIntent = new Intent(this, ShowDaily.class);
//		dinnerIntent.putExtra(String.valueOf(R.string.selected_dinner), dinnerChoice);
//		startActivity(dinnerIntent);

		// new way will poll data layer for type
		startActivity(new Intent(this, ShowDaily.class));

		// push code into data layer, then tag manager will push the changes into analytics
		DataLayer dl =  tagManager.getDataLayer();
		dl.pushEvent("openScreen", DataLayer.mapOf("screen-name", "Show Daily Special"));
		// value collection variable - trigger fires which value to use
		// Tag - trigger fires data forwarding(according to the rules set up in the tag)




	}

}

