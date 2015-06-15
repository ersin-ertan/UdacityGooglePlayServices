package com.nullcognition.identity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

// Scopes Mini-Lesson
// Profile - id, display name, image url, profile url
// Plus.Login(Social) - Profile + Age Range, List of Circles, Read/Write activities
// Email - email address for their account
// Plus.Profiles.Emails.Read - email + other emails

// one authorization equals multi device authorization
// over the air downloads allows web site apps(with a certain rating) to allow free apps to be installed
// on the android device

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

	public static final int State_Signed_In = 0;
	public static final int State_Sign_In = 1;
	public static final int State_Progress = 2;
	private int signInProgress = 1;

	SignInButton signInGPlus;
	Button signIn;
	Button signOut;
	Button revoke;
	TextView textView;

	private PendingIntent signInIntent;
	private int signInError;
	private static final int RC_SIGN_IN = 0;

	protected GoogleApiClient googleApiClient;
	protected synchronized void createGoogleApiClient(){
		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(new Scope(Scopes.PROFILE))
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}

	protected synchronized void createGoogleApiClientWithLimitedScope(){
		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(new Scope("email"))
				.build();
		// access to account but not people api so the PeopleApi.getCurrentPerson will return null
		// you could use getAccountName(), clearDefaultAccount(), revokeAccessAndDisconnect
		// thus replace the PeopleApi calls with the email scope alternatives and getAccountName for the email
	}

	@Override
	public void onConnected(final Bundle bundle){
		signInGPlus.setEnabled(false);
		signIn.setEnabled(false);
		signOut.setEnabled(true);
		revoke.setEnabled(true);
		signInProgress = State_Signed_In;


		Person currentUser = Plus.PeopleApi.getCurrentPerson(googleApiClient);
		textView.setText("Signed In as:" + currentUser.getDisplayName());
	}
	@Override
	public void onConnectionSuspended(final int i){ }
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult){
		if(signInProgress != State_Progress){
			signInIntent = connectionResult.getResolution();
			signInError = connectionResult.getErrorCode();
			if(signInProgress == State_Sign_In){
				resolveSignInError();
			}
		}
		onSignedOut();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
			case RC_SIGN_IN:
				if(resultCode == RESULT_OK){
					signInProgress = State_Sign_In;
				}
				else{
					signInProgress = State_Signed_In;
				}

				if(!googleApiClient.isConnecting()){
					googleApiClient.connect();
				}
				break;
		}
	}

	private void onSignedOut(){
		// Update the UI to reflect that the user is signed out.
		signInGPlus.setEnabled(true);
		signIn.setEnabled(true);
		signOut.setEnabled(false);
		revoke.setEnabled(false);

		textView.setText("Signed out");

	}

	private void resolveSignInError(){
		if(signInIntent != null){
			try{
				signInProgress = State_Progress;
				startIntentSenderForResult(signInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			}
			catch(IntentSender.SendIntentException e){
				Log.e("logErr", "Sign in intent could not be sent" + e.getLocalizedMessage());
				signInProgress = State_Sign_In;
				googleApiClient.connect();
			}
		}
		else{
			showDialog(CONTEXT_RESTRICTED);
		}
	}

	@Override
	protected void onStart(){
		super.onStart();
		googleApiClient.connect();
	}
	@Override
	protected void onStop(){
		if(googleApiClient.isConnected()){googleApiClient.disconnect();}
		super.onStop();
	}

	public void signOut(final View view){
		Plus.AccountApi.clearDefaultAccount(googleApiClient);
		googleApiClient.disconnect();
		googleApiClient.connect();
	}

	public void revokeAccess(final View view){
		Plus.AccountApi.clearDefaultAccount(googleApiClient);
		Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
		createGoogleApiClient();
		googleApiClient.connect();

	}


	@Override
	public void onClick(View v){
		if(!googleApiClient.isConnecting()){
			// We only process button clicks when GoogleApiClient is not transitioning
			// between connected and not connected.
			switch(v.getId()){
				case R.id.btn_sign_in:
					textView.setText("Signing In");
					resolveSignInError();
					break;
				case R.id.btn_standard_signin:
					textView.setText("Signing In Standard"); // doe every thing the same but build the google api builder with limited scope

					break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createGoogleApiClient();

		signInGPlus = (SignInButton) findViewById(R.id.btn_sign_in);
		signIn = (Button) findViewById(R.id.btn_standard_signin);
		signOut = (Button) findViewById(R.id.btn_sign_out);
		revoke = (Button) findViewById(R.id.btn_revoke);
		textView = (TextView) findViewById(R.id.textView);

		signInGPlus.setOnClickListener(this);
		signIn.setOnClickListener(this);
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
