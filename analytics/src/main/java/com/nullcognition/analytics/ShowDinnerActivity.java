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
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

public class ShowDinnerActivity extends Activity{

	TextView tv;
	String mDinner;
	String selectedDinnerExtrasKey = String.valueOf(R.string.selected_dinner);

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_dinner_suggestion);
	}

	protected void onStart(){
		super.onStart();

		TextView heading_tv = (TextView) findViewById(R.id.textView_info_heading);
		heading_tv.setText(getResources().getText(R.string.dinner_heading));

		tv = (TextView) findViewById(R.id.textView_info);

		Intent myIntent = getIntent();
		mDinner = myIntent.getStringExtra(selectedDinnerExtrasKey);
		tv.setText(mDinner);
	}

	public void orderOnline(View view){
		Intent intent = new Intent(this, OrderDinnerActivity.class);
		intent.putExtra(selectedDinnerExtrasKey, mDinner);
		startActivity(intent);
	}

	public void removeMeal(View view){
		Intent intent = new Intent(this, RemoveMealActivity.class);
		intent.putExtra(selectedDinnerExtrasKey, mDinner);
		startActivity(intent);

		TagManager tm = ((MyApp)getApplication()).getTagManager();
		DataLayer dl = tm.getDataLayer();

		dl.pushEvent("openScreen", DataLayer.mapOf("screen-name", "Dislike dinner", "selected-dinner", mDinner));

		// dont want two hits
		// AnalyticCalls.sendDislikeEvent(mDinner);
	}

	public void showRecipe(View view){
		Intent intent = new Intent(this, ShowRecipeActivity.class);
		intent.putExtra(selectedDinnerExtrasKey, mDinner);
		startActivity(intent);
	}

	public void chooseAgain(View view){
		android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.food_prefs_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){

				setDinnerSuggestion(item.getItemId());
				return true;
			}
		});
		popup.show();
	}

	public void setDinnerSuggestion(int item){

		Dinner dinner = new Dinner(this, item);
		mDinner = dinner.getDinnerTonight();
		tv.setText(mDinner);
	}

}
