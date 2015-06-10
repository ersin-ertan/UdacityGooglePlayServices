package com.nullcognition.locationcontext;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class MainActivity extends Activity implements
                                           GoogleApiClient.ConnectionCallbacks,
                                           GoogleApiClient.OnConnectionFailedListener,
                                           ResultCallback<Status>{

	protected ActivityDetectionBroadcastReceiver activityDetectionBroadcastReceiver;
	protected GoogleApiClient googleApiClient;

	{
		synchronized(this){
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(ActivityRecognition.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}

	public void onResult(Status status){
		if(status.isSuccess()){Log.e("logErr", "Worked"); }
		else{
			Log.e("logErr", "Did not work" + status.getStatusMessage());
		}
	}

	private PendingIntent getActivityDetectionPendingIntent(){
		Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	TextView txtDetectedActivities;
	Button btnRequestActivityUpdates;
	Button btnRemoveActivityUpdates;

	@Override
	public void onConnected(final Bundle bundle){ }
	@Override
	public void onConnectionSuspended(final int i){googleApiClient.connect();}
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult){
		Log.i("logErr", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
	}

	@Override
	protected void onStart(){
		super.onStart();
		googleApiClient.connect();
	}
	@Override
	protected void onPause(){
		LocalBroadcastManager.getInstance(this).unregisterReceiver(activityDetectionBroadcastReceiver);
		super.onPause();
	}

	@Override
	protected void onResume(){
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(activityDetectionBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}
	@Override
	protected void onStop(){
		if(googleApiClient.isConnected()){ googleApiClient.disconnect(); }
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activityDetectionBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
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

	public void removeActivityUpdatesButtonHandler(final View view){
		ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(googleApiClient, getActivityDetectionPendingIntent());
	}

	public void requestActivityUpdatesButtonHandler(final View view){
		if(!googleApiClient.isConnected()){
			Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
			return;
		}
		ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getActivityDetectionPendingIntent())
		                                          .setResultCallback(this);
		btnRequestActivityUpdates.setEnabled(false);
		btnRemoveActivityUpdates.setEnabled(true);
	}

	class ActivityDetectionBroadcastReceiver extends BroadcastReceiver{
		ActivityDetectionBroadcastReceiver(){}
		@Override
		public void onReceive(final Context context, final Intent intent){
			ArrayList<DetectedActivity> detectedActivityArrayList =
					intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

			String status = null;
			for(DetectedActivity da : detectedActivityArrayList){
				status += getActivityString(da.getType()) + da.getConfidence() + "%\n";
			}
			txtDetectedActivities.setText(status);
		}
	}
	public String getActivityString(int detectedActivityType){
		Resources resources = this.getResources();
		switch(detectedActivityType){
			case DetectedActivity.IN_VEHICLE:
				return resources.getString(R.string.in_vehicle);
			case DetectedActivity.ON_BICYCLE:
				return resources.getString(R.string.on_bicycle);
			case DetectedActivity.ON_FOOT:
				return resources.getString(R.string.on_foot);
			case DetectedActivity.RUNNING:
				return resources.getString(R.string.running);
			case DetectedActivity.STILL:
				return resources.getString(R.string.still);
			case DetectedActivity.TILTING:
				return resources.getString(R.string.tilting);
			case DetectedActivity.UNKNOWN:
				return resources.getString(R.string.unknown);
			case DetectedActivity.WALKING:
				return resources.getString(R.string.walking);
			default:
				return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
		}
	}

}
