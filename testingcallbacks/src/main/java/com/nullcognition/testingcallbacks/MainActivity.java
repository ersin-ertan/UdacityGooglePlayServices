package com.nullcognition.testingcallbacks;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity{
	protected GoogleApiClient googleApiClient;
	protected CallbackClass callbackClass;
	@Override
	protected void onStart(){
		super.onStart();
		googleApiClient.connect();
		EventBus.getDefault().register(this);

	}
	@Override
	protected void onStop(){
		if(googleApiClient.isConnected()){googleApiClient.disconnect();}
		Toast.makeText(this, "DisConnected from mainactivity", Toast.LENGTH_SHORT).show();
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		callbackClass = new CallbackClass(this);
		googleApiClient = callbackClass.getGoogleApiClient();
		textView = (TextView) findViewById(R.id.textView);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if(id == R.id.action_settings){
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onEventMainThread(LocationChanged my){
		textView.setText(String.valueOf(my.location.getLatitude()));
	}
}
