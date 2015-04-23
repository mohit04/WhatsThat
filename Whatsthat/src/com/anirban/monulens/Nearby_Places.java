package com.anirban.monulens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Nearby_Places extends FragmentActivity implements LocationListener {
	
	Spinner mSprPlaceType;
	 GoogleMap mGoogleMap;
	
	String[] mPlaceType=null;
	String[] mPlaceTypeName=null;
	public String type;
	
	double mLatitude=0;
    double mLongitude=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("nearby_places","on_create");
		setContentView(R.layout.nearby_places);
		
		mPlaceType = getResources().getStringArray(R.array.place_type);
		mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);
		
		mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);
		
        mSprPlaceType.setAdapter(adapter);
        
        Button btnFind=( Button ) findViewById(R.id.btn_find);
        
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        
        if(status!=ConnectionResult.SUCCESS){ 
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { 
        	
       // 	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        	ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        	        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        	        // if no network is available networkInfo will be null
        	        // otherwise check if we are connected
        	        if ((networkInfo != null && networkInfo.isConnected())) {
        	        	 Toast.makeText(this, "Datao is Enabled in your devise", Toast.LENGTH_SHORT).show();
        	        }
                   else{
                showDataDisabledAlertToUser();
            }
            
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
          	 mGoogleMap = fragment.getMap();
          	 
          	 mGoogleMap.setMyLocationEnabled(true);
          	 
          	 
               LocationManager locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);
              
               Criteria criteria = new Criteria();
                
               String provider = locationManager1.getBestProvider(criteria, true);
   
               Location location = locationManager1.getLastKnownLocation(provider);
    
               if(location!=null){
                   onLocationChanged(location);
               }
    
               locationManager1.requestLocationUpdates(provider,20000, 0, this);
               
               btnFind.setOnClickListener(new OnClickListener() {
              	 
                   @Override
                   public void onClick(View v) {
    
                       int selectedPosition = mSprPlaceType.getSelectedItemPosition();
                       type = mPlaceType[selectedPosition];
    
                       StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                       sb.append("location="+mLatitude+","+mLongitude);
                       sb.append("&radius=5000");
                       sb.append("&types="+type);
                       sb.append("&sensor=true");
                       sb.append("&key=AIzaSyCOM58lOB2Amsszjz4yEimi0kr1jWl7n84");
                       
                       PlacesTask placesTask = new PlacesTask();
    
                       placesTask.execute(sb.toString());
    
                   }
               });
            
        	   	 
        }
        
        }
	
	
	private void showDataDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Data is disabled in your device. Would you like to enable it?")
        .setCancelable(false)
        .setPositiveButton("Yes",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callDataSettingIntent = new Intent(
                        android.provider.Settings.ACTION_SETTINGS);
                startActivity(callDataSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
           
            urlConnection = (HttpURLConnection) url.openConnection();
 
           
            urlConnection.connect();
 
            
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
 
        return data;
    }
    
  
    private class PlacesTask extends AsyncTask<String, Integer, String>{
 
        String data = null;

        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        
        @Override
        protected void onPostExecute(String result){
          ParserTask parserTask = new ParserTask();
 
      
            parserTask.execute(result);
        }
 
    }
    

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        JSONObject jObject;
 
       
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
 
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
 
            try{
                jObject = new JSONObject(jsonData[0]);
 
              
                places = placeJsonParser.parse(jObject);
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
 
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
 
        
           
            mGoogleMap.clear();
 
            for(int i=0;i<list.size();i++){
 
            	
            
                MarkerOptions markerOptions = new MarkerOptions();
 
                HashMap<String, String> hmPlace = list.get(i);
 
                double lat = Double.parseDouble(hmPlace.get("lat"));
 
                double lng = Double.parseDouble(hmPlace.get("lng"));
 
                String name = hmPlace.get("place_name");
 
              
                String vicinity = hmPlace.get("vicinity");
 
                LatLng latLng = new LatLng(lat, lng);
 
                markerOptions.position(latLng);
                
                markerOptions.title(name + " : " + vicinity);

             
              //  if(type=="airport")
                	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                	
                if(type=="atm")
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                     	
                if(type=="bank")
                  	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                         	
                if(type=="bus_station")
                 	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.transport));
                             	
                if(type=="church")
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                                 	
                if(type=="doctor")
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.doctors));
                                     	
                if(type=="hospital")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.medical));
                                         	
                if(type=="mosque")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                                             	
                if(type=="movie_theatre")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.movies));
                                                 	
                if(type=="hindu_temple")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def));
                                                     	
                if(type=="restaurant")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurants));
                   
                if(type=="taxi_stand")
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.automotive));
                   
                 if(type=="train_station")
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.def)); 

                 if(type=="metro_station")
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.tours));
                        
                 if(type=="shopping_mall")
                     markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.shopping));
 
                // Placing a marker on the touched position
                mGoogleMap.addMarker(markerOptions);
            }
        }
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
 
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
 
		 
		
		
		 
		
	}
	
	


