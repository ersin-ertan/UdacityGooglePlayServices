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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;


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

		// Set the heading
		TextView heading_tv = (TextView) findViewById(R.id.textView_info_heading);
		heading_tv.setText(getResources().getText(R.string.order_online_heading));

		// Set the text
		TextView tv = (TextView) findViewById(R.id.textView_info);

		String dinner = getIntent().getStringExtra(selectedDinnerExtrasKey);
		tv.setText("This is where you will order the selected dinner: \n\n" +
				dinner);

		thisDinnerId = Utility.getDinnerId(dinner);
		thisDinner = dinner;
//		Tracker tracker = ((MyApp) getApplication()).getTracker();
//
//		tracker.setScreenName("screen name: order dinner activity");
//		tracker.send(new HitBuilders.ScreenViewBuilder().build());


//		String dinnerId = Utility.getDinnerId(dinner);
//		Utility.showMyToast(dinnerId, this);
//		sendViewProductHit(dinner, dinnerId);
	}

	public void addDinnerToCart(View view){
		sendAddToCartHit();
	}
	private void sendAddToCartHit(){

		Product product = new Product()
				.setName("dinner")
				.setPrice(5)
				.setVariant(thisDinner)
				.setId(thisDinnerId)
				.setQuantity(1);

		ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);


		Tracker tracker = ((MyApp) getApplication()).getTracker();

		tracker.setScreenName("Add to Cart");
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("shopping steps")
				.setAction("Add dinner to cart")
				.setLabel(thisDinner)
				.addProduct(product)
				.setProductAction(productAction)
				.build());
	}

	public void sendViewProductHit(String dinner, String dinnerId){
		Product product = new Product()
				.setName("dinner").setPrice(6).setVariant(dinner).setId(dinnerId).setQuantity(1);

		ProductAction productAction = new ProductAction(ProductAction.ACTION_DETAIL);

		Tracker tracker = ((MyApp) getApplication()).getTracker();
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("shopping stepes")
				.setAction("view order dinner screen")
				.setLabel(dinner)
				.addProduct(product)
				.build());
	}


}

