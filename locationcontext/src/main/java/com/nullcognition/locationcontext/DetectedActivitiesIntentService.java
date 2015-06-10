package com.nullcognition.locationcontext;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 An {@link IntentService} subclass for handling asynchronous task requests in
 a service on a separate handler thread.
 */
public class DetectedActivitiesIntentService extends IntentService{
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_FOO = "com.nullcognition.locationcontext.action.FOO";
	private static final String EXTRA_PARAM1 = "com.nullcognition.locationcontext.extra.PARAM1";

	@Override
	public void onCreate(){
		super.onCreate();
	}
	/**
	 Starts this service to perform action Foo with the given parameters. If
	 the service is already performing a task this action will be queued.

	 @see IntentService
	 */
	public static void startActionFoo(Context context, String param1, String param2){
		Intent intent = new Intent(context, DetectedActivitiesIntentService.class);
		intent.setAction(ACTION_FOO);
		intent.putExtra(EXTRA_PARAM1, param1);
//		intent.putExtra(EXTRA_PARAM2, param2);
		context.startService(intent);
	}

	public DetectedActivitiesIntentService(){
		super("DetectedActivitiesIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent){
		if(intent != null){
			//final String action = intent.getAction();
			//if(ACTION_FOO.equals(action)){
			//	final String param1 = intent.getStringExtra(EXTRA_PARAM1);
			//	final String param2 = intent.getStringExtra(EXTRA_PARAM2);
			//  handleActionFoo(param1, param2, intent);
			handleActionFoo(intent);
			//}
		}
	}

	/**
	 Handle action Foo in the provided background thread with the provided
	 parameters.
	 */
//	private void handleActionFoo(String param1, String param2, Intent intent){
	private void handleActionFoo(Intent intent){

		ActivityRecognitionResult activityRecognitionResult =
				ActivityRecognitionResult.extractResult(intent);

		Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

		ArrayList<DetectedActivity> detectedActivitiesArrayList = (ArrayList<DetectedActivity>)
				activityRecognitionResult.getProbableActivities();

		localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivitiesArrayList);
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
	}
}
