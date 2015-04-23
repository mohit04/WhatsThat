package com.anirban.monulens;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.capricorn.ArcMenu;



public class MainActivity extends Activity implements CvCameraViewListener2 {

	
	private static final int[] ITEM_DRAWABLES = {R.drawable.history,R.drawable.places,R.drawable.vt ,
		   R.drawable.share,R.drawable.downloads};
	 
	
	Point point1 ;
	
	  private ToggleButton toggle;
	  private PopupWindow popWindow;
	private int screenWidth;
	private int screenHeight;
	private Mat mGray;
	private ArcMenu menu;

//	private ArrayList<SatelliteMenuItem> items;
	private static Display display;
	private CameraBridgeViewBase mOpenCvCameraView;  //This is a basic class, implementing the interaction with Camera and OpenCV library. The main responsibility of it - is to control when camera can be enabled, process the frame, call external listener to make any adjustments to the frame and then draw the resulting frame to the screen. 
	                                                //The clients shall implement CvCameraViewListener.
	
	//In order for an application to use the OpenCV Manager, it must explicitly bind to its service upon startup. This can be achieved by importing the necessary Java classes for the OpenCV Manager into your Android application.
	/*
	 The Android application first attempts to bind to the OpenCV Manager service. If the OpenCV Manager is not already installed on the device, the binding is unsuccessful and the application attempts to download the OpenCV Manager service from Google Play. 
	 If Google Play is not accessible, the user must manually install the .apk package for the OpenCV Manager using adb install as described earlier. On the other hand, if Google Play is accessible, the OpenCV Manager is automatically downloaded from Google Play and installed.The service then determines if the specific OpenCV for Tegra libraries required by the application are available on the device.
	  If they are available, they are correctly initialized and the application runs successfully by linking dynamically to them.
	*/
	 private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	

			@Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i("baseloader_callback", "OpenCV loaded successfully");
	                    mOpenCvCameraView.enableView();
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	    };


													
	
	
	
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("oncreate", "inside_oncreate");
		
		
		getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );//keeps the screen on..
		setContentView(R.layout.activity_main);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById ( R.id.tutorial1_activity_java_surface_view );

        mOpenCvCameraView.setVisibility( SurfaceView.VISIBLE );
        mOpenCvCameraView.setCvCameraViewListener(this);
		
		 toggle = (ToggleButton) findViewById(R.id.toggleButton1);
		 toggle.setChecked(false);
		 
		  display = getWindowManager().getDefaultDisplay();
		Point size=new Point(); 
		display.getSize(size);
		screenWidth=size.x;
		screenHeight=size.y;

		
		//toggle.setOnClickListener(this);
		
	
		
		
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		          
                      // toggle.setButtonDrawable(R.drawable.processing);
		        	
		        	//Thread.sleep(1000);
		    		
		        	final Handler handler = new Handler();
		        
		    		handler.postDelayed(new Runnable() {
		    		    @Override
		    		    public void run() {
		    		       
		    		    	
		    		    	int temp =(int) Math.ceil((Math.random()*2));
					    	if(temp==1)
					    	{
					    	//	 toggle.setChecked(false);
					    		
					    		Toast.makeText(MainActivity.this, "Match Not Found",
			                	        Toast.LENGTH_SHORT).show();
					    	
					    		 toggle.setChecked(false);

					    		// toggle.setButtonDrawable(R.drawable.whatsthat);
					    		
						    		
					    		
					    	}
					    	else
					    	{
					    		
					    		Toast.makeText(MainActivity.this, "Match Found",
			                	        Toast.LENGTH_SHORT).show();
					    		 toggle.setChecked(false);
					    		 toggle.setEnabled(false);
					    		
					    		 Log.i("onClick","before_on_show_popup");
							    
					    		// toggle.setChecked(false);
							     	onShowPopup();
							    	
					    	}
		    		    	
		    		        
		    		    }
		    		}, 1000);
		        	
		    	
		     	
		        }
		        else {
		            // The toggle is disabled
		        }
		       
		    }
		});
		
		 
	

	}
	
	
	 
	
	
	public void onShowPopup(){
		
		 Log.i("OnshowPopup","before_inflating");
		 LinearLayout viewGroup = (LinearLayout) findViewById(R.id.arclayout);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View inflatedView = layoutInflater.inflate(R.layout.arc, viewGroup);
		Log.i("OnshowPopup","after_inflating");
		
	
		
		 popWindow = new PopupWindow(inflatedView,screenWidth,screenHeight, true );
	        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
	        Log.i("OnshowPopup","before_show_at_location");
	      //  popWindow.setOutsideTouchable(false);
	        
	        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM, 0,100);
	        
	        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
	            @Override
	            public void onDismiss() {
	              
	            	toggle.setEnabled(true);
	            	toggle.setChecked(false);
	            }
	        });
	        
		
		 menu = (ArcMenu)inflatedView.findViewById(R.id.arc_menu);
		 
		 
	      
    	 
    	 initArcMenu(menu, ITEM_DRAWABLES);
    	 
		
		
	        
	       
	       
	}
	
	
	
	public void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		 Log.i("initarcmenu","inside arc menu");
	        final int itemCount = itemDrawables.length;
	        for (int i = 0; i < itemCount; i++) {
	            ImageView item = new ImageView(this);
	            item.setImageResource(itemDrawables[i]);
	            Log.i("initarcmenu","after setting image resource");
	            final int position = i;
	            menu.addItem(item, new OnClickListener() {

	                @Override
	                public void onClick(View v) {
	                	Log.i("initarcmenu","on click method");
	                	 if (position == 0) {
	                	      Toast.makeText(MainActivity.this, "More Information",
	                	        Toast.LENGTH_SHORT).show();
	                	      Intent i = new Intent();
		              		    i.setClass(MainActivity.this,More_Info.class);
		              		    startActivity(i);
	                	     } else if (position == 1) {
	                	      Toast.makeText(MainActivity.this, "Nearby Places",
	                	        Toast.LENGTH_SHORT).show();
	                	      Intent i = new Intent();
	              		    i.setClass(MainActivity.this,Nearby_Places.class);
	              		    startActivity(i);
	                	     } else if (position == 2) {
	                	      Toast.makeText(MainActivity.this, "Virtual Tour/Gallery",
	                	        Toast.LENGTH_SHORT).show();
	                	      
	                	     } else if (position == 3){
	                	      Toast.makeText(MainActivity.this, "Share",
	                	        Toast.LENGTH_SHORT).show();
	                	      Intent i = new Intent();
		              		    i.setClass(MainActivity.this,ShareBarActivity.class);
		              		    startActivity(i);
	                	     }else {
	                   	      Toast.makeText(MainActivity.this, "Downloads",
	                      	        Toast.LENGTH_SHORT).show();
	                   	   Intent i = new Intent();
	              		    i.setClass(MainActivity.this,Downloads.class);
	              		    startActivity(i);
	                	     }
	                }
	            });
	        }
	        
	    }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
		
	}
	
/*	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	 
	        // Checks the orientation of the screen for landscape and portrait
	        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	        }
	    }*/
	
	/*protected void onRestart() {
        super.onRestart();
        Log.d("main_activity", "The onRestart() event");
        toggle = (ToggleButton) findViewById(R.id.toggleButton1);
        toggle.setChecked(false);
     }
     
*/
	
	 @Override
     public void onPause()
     {
         super.onPause();
         if (mOpenCvCameraView != null)
             mOpenCvCameraView.disableView();//This method is provided for clients, so they can disable camera connection and stop the delivery of frames even though the surface view itself is not destroyed and still stays on the screen
     }
	 
	@Override
    public void onResume()
    {
        super.onResume();
        Log.i("onresume", "inside_onresume");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }
	
	 public void onCameraViewStarted(int width, int height) {
		 mGray = new Mat();
		 
	    }
	 
	 

	    public void onCameraViewStopped() {
	    	
	    }
	    
	    
	    //It is callback function and it is called on retrieving frame from camera. 
	    //The callback input is object of CvCameraViewFrame class that represents frame from camera.
	    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
	    	mGray = inputFrame.rgba();
	        return mGray;//It has rgba() and gray() methods that allows to get frame as RGBA and one channel gray scale Mat respectively. It expects that onCameraFrame function returns RGBA frame that will be drawn on the screen.
	    }


}
