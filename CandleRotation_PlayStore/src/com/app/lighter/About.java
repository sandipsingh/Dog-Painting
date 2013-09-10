package com.app.lighter;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	 ImageView his, tip, log, setting;
	 TextView tv;
	@Override            
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		tv=(TextView)findViewById(R.id.textView_about);
		tv.getBackground().setAlpha(180);
	}

                               
}
