package com.nullcognition.udacitygoogleplayservices;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class ActivityMain extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener,
                                                      com.google.android.gms.location.LocationListener{


	private final String LOG_TAG = "LaurenceTestApp";
	private TextView txtLat;
	private TextView txtLon;
	private TextView txtLastKnown;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private Location lastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buildGoogleApiClient();

		txtLat = (TextView) findViewById(R.id.txtLat);
		txtLon = (TextView) findViewById(R.id.txtLon);
		txtLastKnown = (TextView) findViewById(R.id.txtLastKnown);
	}
	protected synchronized void buildGoogleApiClient(){
		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}


	@Override
	protected void onStart(){
		super.onStart();
		// Connect the client.
		googleApiClient.connect();
	}

	@Override
	protected void onStop(){
		// Disconnecting the client invalidates it.
		super.onStop();
		if(googleApiClient.isConnected()){googleApiClient.disconnect();}
	}

	@Override
	public void onConnected(Bundle bundle){

		continuousLocationUpdates();
		lastKnownUpdate();
	}
	private void continuousLocationUpdates(){
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(10); // Update location every second
		// location accuracy from really fine, to 100m to 10 km
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this); // still crashing
	}

	private void lastKnownUpdate(){
		lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		if(lastLocation != null){
			txtLastKnown.setText(String.valueOf(lastLocation.getLatitude() + " " + lastLocation.getLongitude()));
		}
	}

	@Override
	public void onConnectionSuspended(int i){Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult){ Log.i(LOG_TAG, "GoogleApiClient connection has failed" + connectionResult.getErrorCode());}

	@Override
	public void onLocationChanged(Location location){
		Log.i(LOG_TAG, location.toString());

		txtLat.setText(Double.toString(location.getLatitude()));
		txtLon.setText(Double.toString(location.getLongitude()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == R.id.action_settings){return true;}
		return super.onOptionsItemSelected(item);
	}
}
