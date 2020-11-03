package com.example.poem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutUs extends Activity{
	
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		Button toPage3 = (Button) findViewById(R.id.button1);
		toPage3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AboutUs.this, MainActivity.class);
				startActivity(i);				
			}
		});		
	}

	public void click_fb(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.facebook.com/sapto.sutardi"));
        startActivity(intent);	
	}

	public void click_twitter(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://twitter.com/saptos"));
        startActivity(intent);	
	}

}
