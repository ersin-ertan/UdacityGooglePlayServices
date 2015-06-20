package com.nullcognition.maps;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public class StreetView extends Activity implements OnStreetViewPanoramaReadyCallback{

	StreetViewPanoramaFragment streetViewPanoramaFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_street_view);

		streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetViewPano);
		streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_street_view, menu);
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
	@Override
	public void onStreetViewPanoramaReady(final StreetViewPanorama streetViewPanorama){
		// can also get the streetviewpanorama to and get the latlong
		streetViewPanorama.setPosition(new LatLng(36.0579667, -112.1430996));
		StreetViewPanoramaCamera streetViewPanoramaCamera = new StreetViewPanoramaCamera.Builder().bearing(180).build();
		streetViewPanorama.animateTo(streetViewPanoramaCamera, 5000);
	}
	// default behaviours on the panorama which are toggleable - street names, zoom gestures, user navigation

	// user interaction with the street view - detect camera changes, detect user touches on panorama, detect changes to panorama
}
