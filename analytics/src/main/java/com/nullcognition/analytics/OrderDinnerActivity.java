/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.nullcognition.analytics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class OrderDinnerActivity extends Activity{
	String selectedDinnerExtrasKey = String.valueOf(R.string.selected_dinner);
	String thisDinner;
	String thisDinnerId;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_dinner); // was .show_info
	}

	protected void onStart(){
		super.onStart();

		TextView heading_tv = (TextView) findViewById(R.id.textView_info_heading);
		heading_tv.setText(getResources().getText(R.string.order_online_heading));

		TextView tv = (TextView) findViewById(R.id.textView_info);

		String dinner = getIntent().getStringExtra(selectedDinnerExtrasKey);
		tv.setText("This is where you will order the selected dinner: \n\n" + dinner);

		thisDinnerId = Utility.getDinnerId(dinner);
		thisDinner = dinner;

		sendViewProductHit(dinner, thisDinnerId);

		AnalyticCalls.sendProductView();
	}

 	public void startCheckoutProcess(final View view){
		AnalyticCalls.sendStartCheckoutProcessHit(thisDinner, thisDinnerId);
	}

	public void addDinnerToCart(View view){
		AnalyticCalls.sendAddToCartHit(thisDinner, thisDinnerId);
		Button button = (Button) findViewById(R.id.start_checkout_btn);
		button.setVisibility(View.VISIBLE);

		button = (Button) findViewById(R.id.add_to_cart_btn);
		button.setVisibility(View.INVISIBLE);
	}

	public void purchase(final View view){
		AnalyticCalls.purchase(thisDinnerId);

		Button button = (Button) findViewById(R.id.purchase);
		button.setVisibility(View.VISIBLE);

		button = (Button) findViewById(R.id.start_checkout_btn);
		button.setVisibility(View.INVISIBLE);
	}

	public void sendViewProductHit(String dinner, String dinnerId){
		AnalyticCalls.sendProductViewHit(dinner, dinnerId);
	}
}

