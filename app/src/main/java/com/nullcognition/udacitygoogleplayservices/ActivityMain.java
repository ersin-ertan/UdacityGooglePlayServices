package com.nullcognition.udacitygoogleplayservices;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// asynchronous programming model
/*
onCcreate - googleApiClient
onStart - googleApiClient

onconnectionfailed, suspended, connected - location request, locationchanged - location update

location service
*/


public class ActivityMain extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == R.id.action_settings){ return true;}
		return super.onOptionsItemSelected(item);
	}
}
