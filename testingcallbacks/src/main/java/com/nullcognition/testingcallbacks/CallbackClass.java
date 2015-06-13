package com.nullcognition.testingcallbacks;// Created by ersin on 12/06/15

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import de.greenrobot.event.EventBus;

public class CallbackClass implements
                           GoogleApiClient.ConnectionCallbacks,
                           GoogleApiClient.OnConnectionFailedListener,
                           LocationListener{

	private GoogleApiClient googleApiClient;
	private Context context;
	private LocationRequest locationRequest;
	CallbackClass(Context context){
		this.context = context;
		synchronized(this){
			googleApiClient = new GoogleApiClient.Builder(context)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}

	public GoogleApiClient getGoogleApiClient(){ return googleApiClient;}


	@Override
	public void onConnected(final Bundle bundle){
		Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(10); // Update location every second
		// location accuracy from really fine, to 100m to 10 km
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this); // still crashing
	}
	@Override
	public void onConnectionSuspended(final int i){

	}
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult){

	}
	@Override
	public void onLocationChanged(final Location location){
		EventBus.getDefault().post(new LocationChanged(location));
		// System.gc(); // playing with the memory monitor
	}



}

