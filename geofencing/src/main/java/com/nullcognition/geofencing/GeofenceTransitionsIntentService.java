package com.nullcognition.geofencing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService{
	private static final String ACTION_ADD_GEOFENCE = "com.nullcognition.geofencing.action.ACTION_ADD_GEOFENCE";
	private static final String GEOFENCING_EVENT = "com.nullcognition.geofencing.extra.GEOFENCING_EVENT ";

	public static void startActionFoo(Context context, GeofencingEvent geofencingEvent){
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
	}

	@Override
	protected void onHandleIntent(Intent intent){
		if(intent != null){
			final String action = intent.getAction();
//			if(ACTION_ADD_GEOFENCE.equals(action)){
				final GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
				if(geofencingEvent.hasError()){
					Log.e("logErr", "error geofencing event:" + geofencingEvent.getErrorCode());
				}
				handleActionFoo(geofencingEvent);
//			}
		}
	}

	private void handleActionFoo(GeofencingEvent geofencingEvent){
		int geofenceTransition = geofencingEvent.getGeofenceTransition();

		switch(geofenceTransition){
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
				String geofenceTransitionDetails = null;
				for(Geofence g : geofences){ geofenceTransitionDetails += g.getRequestId() + "\n"; }
				// TextUtils.join("delimiter", geofences);
				sendNotification(geofenceTransitionDetails);
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

		Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
		android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.common_full_open_on_phone))
						.setSmallIcon(R.drawable.ic_plusone_medium_off_client)
						.setColor(Color.BLUE)
						.setContentTitle("My notification")
						.setContentText(geofenceTransitionDetails)
						.setContentIntent(pendingIntent);
		notificationBuilder.setAutoCancel(true);

		int mNotificationId = 001;
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, notificationBuilder.build());
	}
}
