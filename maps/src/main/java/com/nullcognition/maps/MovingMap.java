package com.nullcognition.maps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MovingMap extends Activity implements OnMapReadyCallback{

	// uses the same layout as the first map but changes only the title, thus the title of the button
	// and the layout mapping will not be the same

	GoogleMap m_map;
	boolean mapReady = false;

	MarkerOptions one;
	MarkerOptions two;
	MarkerOptions three;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moving_map);
		Button btnMap = (Button) findViewById(R.id.btnMap);
		btnMap.setText("Seattle");
		btnMap.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mapMoveTo(new LatLng(47.6204, -122.3491));
			}
		});

		Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
		btnSatellite.setText("Tokyo");
		btnSatellite.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mapMoveTo(new LatLng(35.6895, 139.6917));

			}
		});

		Button btnHybrid = (Button) findViewById(R.id.btnHybrid);
		btnHybrid.setText("Dublin");
		btnHybrid.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mapMoveTo(new LatLng(53.3478, 6.2597));
			}
		});

		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	public void mapMoveTo(LatLng latLng){
		if(mapReady){
			CameraPosition target = CameraPosition.builder().target(latLng).zoom(50).bearing(15).tilt(45).build();
			m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);
		}
	}

	@Override
	public void onMapReady(GoogleMap map){
		mapReady = true;
		m_map = map;
		LatLng newYork = new LatLng(40.7484, -73.9857);
		CameraPosition target = CameraPosition.builder().target(newYork).zoom(20).build();
		m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

		initMarkerOptions();
	}
	private void initMarkerOptions(){
		one = new MarkerOptions().position(new LatLng(40.74842, -73.9859)).title("one").draggable(true).alpha(80);
		two = new MarkerOptions().position(new LatLng(40.74845, -73.9855)).title("two").flat(true).rotation(25).snippet("snippet");
		three = new MarkerOptions().position(new LatLng(40.74848, -73.9850)).title("three")
		                           .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.btn_star_big_on));

		PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).add(new LatLng(40.74842, -73.9859),
				new LatLng(40.74845, -73.9855),
				new LatLng(40.74848, -73.9850),
				new LatLng(40.74858, -73.9860),
				new LatLng(40.74842, -73.9859)); // or chain the adds
		polylineOptions.color(Color.CYAN);

		m_map.addCircle(new CircleOptions().center(new LatLng(40.74845, -73.9855)).fillColor(Color.GREEN).radius(15).strokeWidth(10));
		m_map.addCircle(new CircleOptions().center(new LatLng(40.74858, -73.9860)).fillColor(Color.argb(20, 0, 0, 255)).radius(15).strokeWidth(10));
																	// lower alpha = more opaque = less visible
		m_map.addPolyline(polylineOptions);

		m_map.addMarker(one);
		m_map.addMarker(two);
		m_map.addMarker(three);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_moving_map, menu);
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
