package com.nullcognition.analytics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllDinners extends ListActivity{

	String selectedDinnerExtrasKey = String.valueOf(R.string.selected_dinner);

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_dinners);

		Dinner dinner = new Dinner();
		String[] allDinners = dinner.getAllDinners(this);

		/**
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, allDinners);
		 **/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.show_dinner_in_row, R.id.textview_dinner_row, allDinners);

		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){

		super.onListItemClick(l, v, position, id);
		String value = (String) getListView().getItemAtPosition(position);

		Utility.showMyToast("selected dinner is " + value, this);
		Intent intent = new Intent(this, OrderDinnerActivity.class);
		intent.putExtra(selectedDinnerExtrasKey, value);

		startActivity(intent);
	}
}
