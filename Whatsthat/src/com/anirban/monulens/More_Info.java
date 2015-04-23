package com.anirban.monulens;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class More_Info extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent browser=new Intent(Intent.ACTION_VIEW);
		browser.setData(Uri.parse(getString(R.string.jiit128)));
		startActivity(browser);
	}
	
	

}
