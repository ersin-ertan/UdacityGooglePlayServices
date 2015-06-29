package com.nullcognition.analytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tagmanager.ContainerHolder;

public class ShowDaily extends Activity{ // did not work because ShowDaily extended AppCompatActivity, which was created by the Android Studio creation template

	String selectedDinnerExtrasKey = String.valueOf(R.string.selected_dinner);
	String thisDinner;
	String thisDinnerId;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_daily);
	}

	protected void onStart(){
		super.onStart();

		TextView heading_tv = (TextView) findViewById(R.id.textView_info_heading);
		heading_tv.setText(getResources().getText(R.string.todays_special));

//		String dinner = getIntent().getStringExtra(selectedDinnerExtrasKey);
		String dinner = getDailySpecial();
		TextView tv = (TextView) findViewById(R.id.textView_info);
		tv.setText("Today's Special is: \n\n" + dinner);

		thisDinnerId = Utility.getDinnerId(dinner);
		thisDinner = dinner;
	}

	public void orderOnline(View view){
		Intent intent = new Intent(this, OrderDinnerActivity.class);
		intent.putExtra(selectedDinnerExtrasKey, thisDinner);
		startActivity(intent);
	}

	public String getDailySpecial(){

		// some new daily special was overriding the second attempt to view the daily special
		// with the value of 'some new daily special version 3' because the trigger is set to always
		// which seems to override the one shot like nature of predefined daily specials
		// thus I deleted the unrestricted variable and let the en language be the default, general but incorrect
		// solution, if a default variable is needed then make one, a language wide variable should not be place
		// holder for a specialized data field like daily_special

		ContainerHolder containerHolder = ((MyApp) getApplication()).getContainerHolder();
		String value = containerHolder.getContainer().getString("daily_special");
//		tagmanager.google.com
		return value;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_show_daily, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if(id == R.id.action_settings){
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
//
