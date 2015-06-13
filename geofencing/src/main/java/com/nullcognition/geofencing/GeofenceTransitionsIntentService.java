package com.nullcognition.geofencing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService{
	private static final String ACTION_ADD_GEOFENCE = "com.nullcognition.geofencing.action.ACTION_ADD_GEOFENCE";
	private static final String GEOFENCING_EVENT = "com.nullcognition.geofencing.extra.GEOFENCING_EVENT ";

	public static void startActionFoo(Context context ){ // , GeofencingEvent geofencingEvent
		Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
		intent.setAction(ACTION_ADD_GEOFENCE);
//		intent.putExtra(GEOFENCING_EVENT,geofencingEvent);
		context.startService(intent);
	}

	public GeofenceTransitionsIntentService(){
		super("GeofenceTransitionsIntentService");
	}

	@Override
	public void onCreate(){
		super.onCreate();
		sendNotification("notification sent");
	}
	@Override
	protected void onHandleIntent(Intent intent){
//		if(intent != null){
//			final String action = intent.getAction();
//			if(ACTION_ADD_GEOFENCE.equals(action)){
//				final GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
//				if(geofencingEvent.hasError()){
//					Log.e("logErr", "error geofencing event:" + geofencingEvent.getErrorCode());
//				}
//				handleActionFoo(geofencingEvent);
//			}
//		}
	}

	private void handleActionFoo(GeofencingEvent geofencingEvent){
		int geofenceTransition = geofencingEvent.getGeofenceTransition();

		List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
		String geofenceTransitionDetails = null;
		for(Geofence g : geofences){ geofenceTransitionDetails += g.getRequestId() + "\n"; }
		// TextUtils.join("delimiter", geofences);
		sendNotification(geofenceTransitionDetails);

		switch(geofenceTransition){
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				break;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				break;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				break;
			default:
				throw new RuntimeException("geofence transition input invalid:" + geofenceTransition);
		}


	}
	public void sendNotification(final String geofenceTransitionDetails){

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_plusone_medium_off_client)
						.setContentTitle("My notification")
						.setContentText(geofenceTransitionDetails);

		int mNotificationId = 001;
		NotificationManager mNotifyMgr =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}
}
