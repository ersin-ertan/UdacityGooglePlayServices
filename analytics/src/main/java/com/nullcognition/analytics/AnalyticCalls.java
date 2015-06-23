package com.nullcognition.analytics;// Created by ersin on 23/06/15

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

// would work well with a model and a hierarchy thus products would be passed in
// with the model type, and screen/labels, would be infered or polled from the
// model class

public enum AnalyticCalls{
	INSTANCE;

	private static Tracker tracker;

	public static void init(Application application){
		if(tracker == null){
			GoogleAnalytics ga = GoogleAnalytics.getInstance(application);
			tracker = ga.newTracker(R.xml.analytics_id);
			ga.enableAutoActivityReports(application);
			ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		}
	}

	public static void sendDislikeEvent(String dinner){
		tracker.send((new HitBuilders.EventBuilder()
				.setCategory("Dinner actions")
				.setAction("Dislike dinner choice")
				.setLabel(dinner)
				.build()));
	}

	public static void sendAddToCartHit(String variantAndLabel, String dinnerId){
		// variantAndLabel is the dinner
		Product product = newProduct(variantAndLabel, dinnerId);
		ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);

		tracker.setScreenName("Add to Cart");
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("Shopping steps")
				.setAction("Add dinner to cart")
				.setLabel(variantAndLabel)
				.addProduct(product)
				.setProductAction(productAction)
				.build());
	}

	public static void sendProductViewHit(String variantAndLabel, String dinnerId){
		// variantAndLabel is the dinner
		Product product = newProduct(variantAndLabel, dinnerId);
		ProductAction productAction = new ProductAction(ProductAction.ACTION_DETAIL);

		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("Shopping steps")
				.setAction("View order dinner screen")
				.setLabel(variantAndLabel)
				.addProduct(product)
				.setProductAction(productAction)
				.build());
	}

	private static Product newProduct(final String variantAndLabel, final String dinnerId){
		return new Product().setName("Dinner")
		                    .setPrice(5)
		                    .setVariant(variantAndLabel)
		                    .setId(dinnerId)
		                    .setQuantity(1);
	}

	public static void sendProductView(){

		tracker.setScreenName("screen name: order dinner activity");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());


	}
}

