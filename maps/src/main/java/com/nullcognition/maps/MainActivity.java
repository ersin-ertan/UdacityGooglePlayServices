package com.nullcognition.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity implements OnMapReadyCallback{

	GoogleMap m_map;
	boolean mapReady = false;

	// easy debugging fast switch
	private void switchActivity(){
		startActivity(new Intent(this, StreetView.class));
//		startActivity(new Intent(this, MovingMap.class));
//		startActivity(new Intent(this, MapXml.class));

	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		switchActivity();

		setContentView(R.layout.activity_main);

		Button btnMap = (Button) findViewById(R.id.btnMap);
		btnMap.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(mapReady){ m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL); }
			}
		});

		Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
		btnSatellite.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(mapReady){ m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE); }
			}
		});

		Button btnHybrid = (Button) findViewById(R.id.btnHybrid);
		btnHybrid.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(mapReady){ m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID); }
			}
		});

		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map){
		mapReady = true;
		m_map = map;
		LatLng newYork = new LatLng(40.7484, -73.9857);
		CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
		m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == R.id.action_moving_map){
			startActivity(new Intent(this, MovingMap.class));
			return true;
		}
		else if(id == R.id.action_xml_map){
			startActivity(new Intent(this, MapXml.class));
			return true;
		}
		else if(id == R.id.action_pano){
			startActivity(new Intent(this, StreetView.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


}
