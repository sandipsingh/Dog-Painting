package com.app.lighter;

 


import java.io.File;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
 


public class LighterSelectionActivity extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory, AdListener{
 
	private int lighterCounter;
	TextView tv;
	  private InterstitialAd interstitial;
	 ImageView setting;
	
	 private SensorManager mSensorManager;
	 SharedPreferences myPrefs;
	    File f;
	  private ShakeEventListener mSensorListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.main);                       
        
        
        setting = (ImageView)findViewById(R.id.setting);
         tv=(TextView)findViewById(R.id.textView1);
         Typeface font = Typeface.createFromAsset(getAssets(), "Chantelli_Antiqua.ttf");  
         tv.setTypeface(font);
         
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right));
        
 setting.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), Settings.class);
				startActivity(i);
			}
		});

        
        
//        
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        switch(metrics.densityDpi){
//             case DisplayMetrics.DENSITY_LOW:
//            	 
//            	 Toast.makeText(LighterSelectionActivity.this,"ldpi",      
//            	                Toast.LENGTH_SHORT).show();
//                        break;
//             case DisplayMetrics.DENSITY_MEDIUM:
//            	 
//            	 Toast.makeText(LighterSelectionActivity.this,"mdpi",      
//     	                Toast.LENGTH_SHORT).show();
//                         break;
//             case DisplayMetrics.DENSITY_HIGH:
//            	 Toast.makeText(LighterSelectionActivity.this,"hdpi",      
//     	                Toast.LENGTH_SHORT).show();
//                         break;
//        }
//        
       // making a shredprrefrence file to check the conition for adds 
/* myPrefs = this.getSharedPreferences("hello", MODE_WORLD_READABLE);
 f = new File("/data/data/com.app.lighter/shared_prefs/hello.xml");

	if(!f.exists()){
		
		 SharedPreferences.Editor prefsEditor = myPrefs.edit();
         prefsEditor.putString("level1","0");   
         prefsEditor.commit();
		
      // Create the interstitial
         interstitial = new InterstitialAd(this, "a151cd722374879");                 //making interstitial id  not null

		Toast.makeText(getApplicationContext(), "now loaded for first tym..",
				Toast.LENGTH_SHORT).show();
	}
		
	else{
		Toast.makeText(getApplicationContext(), "now loaded for later tym..i.e the directory exist",
				Toast.LENGTH_SHORT).show();
		
		// Create the interstitial
	      interstitial = new InterstitialAd(this, " ");                    //making interstitial id null
	}*/
			
 // Create the interstitial
 interstitial = new InterstitialAd(this, "a151cd722374879");                 //making interstitial id  not null

     
        // Create ad request
        AdRequest adRequest = new AdRequest();

        // Begin loading your interstitial
        interstitial.loadAd(adRequest);

        // Set Ad Listener to use the callbacks below
        interstitial.setAdListener(this);
   

     

 
        mSwitcher.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				  System.out.println("fjhsdjfhdsjf-------lighterCounter---->\t"+lighterCounter);
				  
				  if(lighterCounter==4){
					  Intent intent=new Intent(LighterSelectionActivity.this,Second.class);
						 intent.putExtra("lighter color", lighterCounter);
						 startActivity(intent);
				  }
				/*  else if(lighterCounter==8){
					  Intent intent=new Intent(LighterSelectionActivity.this,LighterWithImage.class);
						 intent.putExtra("lighter color", lighterCounter);
						 startActivity(intent);
				  }*/
			else{
					  
					
						  Intent intent=new Intent(LighterSelectionActivity.this,ParticleViewActivity.class);
							 intent.putExtra("lighter color", lighterCounter);
							 startActivity(intent);
			
					  
				  }
				 
				
			}
		});
        		
        
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);
    }
 
    
    
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        mSwitcher.setImageResource(mImageIds[position]);
        System.out.println("fjhsdjfhdsjf----------->\t"+position);
        lighterCounter=position;
    }
    
   
 
    public void onNothingSelected(AdapterView<?> parent) {
    	
    }
 
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return i;
    }
 
    private ImageSwitcher mSwitcher;
 
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        public int getCount() {
            return mThumbIds.length;
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
 
            i.setImageResource(mThumbIds[position]);                     
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            i.setBackgroundResource(R.drawable.a);
            return i;
        }
 
        private Context mContext;
    }            
 
    private Integer[] mThumbIds = {
            R.drawable.a, R.drawable.b,
            R.drawable.c, R.drawable.d,R.drawable.gold_lighter,R.drawable.e,R.drawable.f,R.drawable.g};
 
    private Integer[] mImageIds = {
    		 R.drawable.a, R.drawable.b,
             R.drawable.c,R.drawable.d,R.drawable.gold_lighter, R.drawable.e,R.drawable.f,R.drawable.g};



	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
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
	

    public void onReceiveAd(Ad ad) {
      Log.d("OK", "Received ad");
      if (ad == interstitial) {
        interstitial.show();
    
      }
      }
   /* public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	  {
	   
		 
		f.delete();
	    return super.onKeyDown(paramInt, paramKeyEvent);
	  }*/
	}
