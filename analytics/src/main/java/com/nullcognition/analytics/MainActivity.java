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


public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		((MyApp) getApplication()).startTracking();
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

	public String getDinnerSuggestion(int item){

		String dinnerChoice = new Dinner(this, item).getDinnerTonight();
		Intent dinnerIntent = new Intent(this, ShowDinnerActivity.class);

		dinnerIntent.putExtra(String.valueOf(R.string.selected_dinner), dinnerChoice);
		startActivity(dinnerIntent);

		return dinnerChoice;
	}
}

