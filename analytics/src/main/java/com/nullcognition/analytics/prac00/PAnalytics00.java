package com.nullcognition.analytics.prac00;// Created by ersin on 24/06/15

import android.content.Context;
import android.content.Intent;

import com.nullcognition.analytics.MainActivity;

public class PAnalytics00{

	public static void start(Context context){
		Intent starter = new Intent(context, MainActivity.class);
		starter.putExtra("name", "value");
		context.startActivity(starter);
	}
/*
Product -.set name, price, variant, id, quantity, brand, category, coupon code, custom dimentions, position

ProductAction - Constructor:add, checkout, checkout options, click, purchase, detail, detail options, remove, refund
	.set transaction. tax, shipping, revenue, id, couponcode, affiliation, then checkout options, checkout step, product actionlist, productListSource

tracker.send( new HitBuilder. AppView, ScreenView, Exception, Event, Item, Social, Timing, Transaction
.setCategory, value, label, variable, newSession, customMetrics...

*/

}
