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

	// refunds are also available but expire in analytics after 6 months,
	// you must keep track of the product id of purchases, which are required
	// as the refund parameter

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

	public static void sendStartCheckoutProcessHit(String variantAndLabel, String dinnerId){
		// variantAndLabel is the dinner
		Product product = newProduct(variantAndLabel, dinnerId);
		ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT);

		tracker.setScreenName("Checkout");
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("Checkout")
				.setAction("Checkout Items")
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

	public static void purchase(final String dinnerId){
		Product product = newProduct(dinnerId, dinnerId);
		ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
				.setTransactionId(Utility.getUniqueTransactionId(dinnerId));
//				.setTransactionRevenue()
//				.setTransactionTax()
//				.setProductActionList("All Products"); // if multi products

		tracker.send(new HitBuilders.EventBuilder()
				.setCategory("purchase")
				.setAction("purchase")
				.setLabel("purchase")
				.addProduct(product)
						// .addProduct(moreProducts) which should be add products(list)
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

	public static void timingOfTask(long elapsedTime){
		long nanoToMili = elapsedTime / 1000000;
		tracker.send(new HitBuilders.TimingBuilder()
				.setCategory("list all dinners")
				.setValue(nanoToMili)
				.setLabel("display list")
				.setVariable("duration")
				.build());


	}

	private static void checkoutSteps(){
		ProductAction pa = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTION)
				.setCheckoutStep(1); // correspond with the funnel steps in
		// admin > ecommerce > Enhanced ecommerce settings > funnels created
	}

	public static void sendProductView(){

		tracker.setScreenName("screen name: order dinner activity");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	// 	<bool name = "ga_reportUncaughtExceptions">true</bool>
// in xml to catch uncaught
	public static void sendCaughtException(String msg){
		tracker.send(new HitBuilders.ExceptionBuilder()
				.setDescription(msg)
				.setFatal(true)
				.build());
	}

	// https://developers.google.com/analytics/devguides/collection/android/v4/customdimsmets
	// for custom metrics
}

