package com.nullcognition.geofencing;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener,
                                                      ResultCallback<Status>,
                                                      LocationListener{

	protected GoogleApiClient googleApiClient;


	@Override
	public void onLocationChanged(final Location location){

	}
	@Override
	public void onResult(final Status status){

	}
	@Override
	public void onConnected(final Bundle bundle){

	}
	@Override
	public void onConnectionSuspended(final int i){

	}
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult){

	}
	@Override
	protected void onStart(){
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	protected void onStop(){
		if(googleApiClient.isConnected()){googleApiClient.disconnect();}
		super.onStop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		synchronized(this){
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}

	public void addGeofence(final View view){

		Toast.makeText(MainActivity.this, String.valueOf(googleApiClient.isConnected()), Toast.LENGTH_SHORT).show();
		GeofenceTransitionsIntentService.startActionFoo(this);
		// do button logic here
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == R.id.action_settings){
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
