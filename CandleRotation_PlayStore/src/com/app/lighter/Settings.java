package com.app.lighter;



import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Settings extends Activity {

	
	
	ListView lv;
	int image[] = { R.drawable.rate_us,R.drawable.about,R.drawable.more_apps};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

	
		ListView l1 = (ListView) findViewById(R.id.list_settings);
	//	l1.setAlpha((float) 0.80);
		l1.setAdapter(new MyCustomAdapter(image));
		
		
	
	}
	class MyCustomAdapter extends BaseAdapter {

		int[] data_image;

		MyCustomAdapter() {
			data_image = null;
		}

		MyCustomAdapter(int[] image) {
			data_image = image;
		}

		public int getCount() {
			return data_image.length;
		}

		public String getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row;

			row = inflater.inflate(R.layout.settings_listrow, parent, false);
			row.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					
					if (position == 0) {
						
						Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.app.lighter");   
					     Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
					     startActivity(intent);
				                            		
						}

					if (position == 1) {

						Intent intent2 = new Intent(getApplicationContext(),
								About.class);
						startActivity(intent2);

					}

					if (position == 2) {
						Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=kewl+apps");   
					     Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
					     startActivity(intent);
              
					}
				
				}
			});
		
			ImageView imageview = (ImageView) row
					.findViewById(R.id.ImageViewSettingListRow);

			imageview.setImageResource(data_image[position]);

			return (row);

		}
	}
	
	
	
	
	
	
	
	
	
	
	}


