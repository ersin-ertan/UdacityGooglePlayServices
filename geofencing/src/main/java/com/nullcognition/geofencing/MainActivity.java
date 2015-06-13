package com.nullcognition.geofencing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener,
                                                      ResultCallback<Status>,
                                                      LocationListener{

	protected GoogleApiClient googleApiClient;
	protected ArrayList<Geofence> geofenceArrayList;

	@Override
	public void onLocationChanged(final Location location){

	}

	@Override
	public void onResult(final Status status){
		if(status.isSuccess()){Toast.makeText(this, "Geofences Added", Toast.LENGTH_SHORT).show();}
		else{Log.e("logErr", status.getStatusMessage()); }
	}

	@Override
	public void onConnected(final Bundle bundle){

	}
	@Override
	public void onConnectionSuspended(final int i){
		googleApiClient.connect();

	}
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult){
		Log.e("logErr", "onConnection failed:" + connectionResult.getErrorCode());
	}

	@Override
	protected void onStart(){
		super.onStart();
		if(!googleApiClient.isConnecting() || !googleApiClient.isConnected()){
			googleApiClient.connect();
		}
	}

	@Override
	protected void onStop(){
		if(googleApiClient.isConnecting() || googleApiClient.isConnected()){
			googleApiClient.disconnect();
		}
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
		geofenceArrayList = new ArrayList<Geofence>();
		populateGeofenceList();
	}
	private void populateGeofenceList(){
		for(Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()){

			geofenceArrayList.add(new Geofence.Builder()
					.setRequestId(entry.getKey())
					.setCircularRegion(entry.getValue().latitude, entry.getValue().longitude, Constants.GEOFENCE_RADIUS_IN_METERS)
					.setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
					.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
					.build());
		}
	}

	public void addGeofence(final View view){
		if(!googleApiClient.isConnected()){
			Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
			return;
		}

		// A pending intent that that is reused when calling removeGeofences(). This
		// pending intent is used to generate an intent when a matched geofence
		// transition is observed.
		try{
			LocationServices.GeofencingApi.addGeofences(
					googleApiClient, getGeofencingRequest(), getGeofencePendingIntent()
			).setResultCallback(this); // Result processed in onResult().
		}
		catch(SecurityException securityException){
			// Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
		}
	}

	private PendingIntent getGeofencePendingIntent(){
		Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}


	private GeofencingRequest getGeofencingRequest(){
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		builder.addGeofences(geofenceArrayList);
		return builder.build();
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
