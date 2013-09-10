package com.app.lighter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;

public class Interstitial extends Activity implements AdListener {

  private InterstitialAd interstitial;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   // setContentView(R.layout.main);

    // Create the interstitial
    interstitial = new InterstitialAd(this, "a15047259791b18");

    // Create ad request
    AdRequest adRequest = new AdRequest();

    // Begin loading your interstitial
    interstitial.loadAd(adRequest);

    // Set Ad Listener to use the callbacks below
    interstitial.setAdListener(this);
  }

  
  public void onReceiveAd(Ad ad) {
    Log.d("OK", "Received ad");
    if (ad == interstitial) {
      interstitial.show();
    }
  }

public void onDismissScreen(Ad arg0) {
	// TODO Auto-generated method stub
	Intent i=new Intent(getApplicationContext(), LighterSelectionActivity.class);
	startActivity(i);
	
}

public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
	// TODO Auto-generated method stub
	
}

public void onLeaveApplication(Ad arg0) {
	// TODO Auto-generated method stub
	
}

public void onPresentScreen(Ad arg0) {
	// TODO Auto-generated method stub
	
}
}