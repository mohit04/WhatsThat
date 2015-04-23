package com.anirban.monulens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
	Drawable bg=getResources().getDrawable(R.drawable.background);
	bg.setAlpha(80);

		
	//	final Animation animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
		final Animation animfade = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final TextView bt = (TextView)findViewById(R.id.button1);
		bt.startAnimation(animfade);
		
		
		
		final Handler handler = new Handler();
        
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		       
		
		 Intent i = new Intent();
		    i.setClass(SplashActivity.this,MainActivity.class);
		    startActivity(i);
		    finish();
		    }
		}, 500);

	}

	
	
}
