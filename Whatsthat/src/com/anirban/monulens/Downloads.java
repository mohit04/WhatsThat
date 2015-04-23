package com.anirban.monulens;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Downloads extends ListActivity {
	
	   private ProgressDialog pDialog;
       public static final int progress_bar_type = 0; 

	String downloadable_items[];
	ListView listv;
	public String[] sdfile2={"/JIITBrochure2014.pdf","/AdmissionAnnouncementAdJIIT2014.pdf","/ACADEMICCALENDAR201415.pdf"};
	public String[] sdfiles={"/sdcard/JIITBrochure2014.pdf","/sdcard/AdmissionAnnouncementAdJIIT2014.pdf","/sdcard/ACADEMICCALENDAR201415.pdf"};
    String[] urld={"http://www.jiit.ac.in/uploads/JIITBrochure2014.pdf","http://www.jiit.ac.in/uploads/AdmissionAnnouncementAdJIIT2014.pdf","http://www.jiit.ac.in/uploads/ACADEMICCALENDAR201415.pdf"};
    private static String file_url ;
    public int pos=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloads);
		
	
		
				downloadable_items = getResources().getStringArray(R.array.downloads);
		
		ArrayAdapter<String> listadap = new ArrayAdapter<String>(this,R.layout.custom_listview,downloadable_items);
 listv= (ListView) findViewById(android.R.id.list);
		
        listv.setAdapter(listadap);
        
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
         if(position==0)
         {
         file_url = urld[position];
         pos=0;
         }
         if(position==1){
             file_url = urld[position];
             pos=1;}
         if(position==2) {
             file_url = urld[position];
             pos=2;}
         
         new DownloadFileFromURL().execute(file_url);
         
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case progress_bar_type:
	        pDialog = new ProgressDialog(this);
	        pDialog.setMessage("Downloading file. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setMax(100);
	        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        pDialog.setCancelable(true);
	        pDialog.show();
	        return pDialog;
	    default:
	        return null;
	    }
	}
	
	
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		 
	    /**
	     * Before starting background thread
	     * Show Progress Bar Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        showDialog(progress_bar_type);
	    }
	 
	    /**
	     * Downloading file in background thread
	     * */
	    @Override
	    protected String doInBackground(String... f_url) {
	        int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();
	 
	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            String file=sdfiles[pos];
	            // Output stream to write file
	            OutputStream output = new FileOutputStream(file);
	 
	            byte data[] = new byte[1024];
	 
	            long total = 0;
	 
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	 
	                // writing data to file
	                output.write(data, 0, count);
	            }
	 
	            // flushing output
	            output.flush();
	 
	            // closing streams
	            output.close();
	            input.close();
	 
	        } catch (Exception e) {
	            Log.e("Error: ", e.getMessage());
	        }
	 
	        return null;
	    }
	 
	    /**
	     * Updating progress bar
	     * */
	    protected void onProgressUpdate(String... progress) {
	        // setting progress percentage
	        pDialog.setProgress(Integer.parseInt(progress[0]));
	   }
	 
	    /**
	     * After completing background task
	     * Dismiss the progress dialog
	     * **/
	    @Override
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog after the file was downloaded
	        dismissDialog(progress_bar_type);
	        
	        String file1=sdfiles[pos];
	 Intent intent=new Intent();
	    File file3=new File(file1);
	   intent.setDataAndType(Uri.fromFile(file3),"application/pdf");
	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
       startActivity(intent);
	    }
	 
	}
}
