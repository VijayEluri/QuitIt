package com.quitit.android;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunningTally extends Activity {
	// Debug tag
	private final String DEB_TAG = "RunningTally.java";
	private TimeBreakDown bd;
	private TimeZone tz  = TimeZone.getDefault();
	private TextView tvStartDate;
	private TextView tvDays;
    private TextView tvMonths;
    private TextView tvYrs;
    private TextView tvHours;
    private TextView tvMins;
    private TextView tvSecs;
    private TextView tvMilSecs;
    private int mAppId = 0;
    
    private Timer autoUpdate;
    
   // private TimeDifference td;
    
    final Context context = RunningTally.this;
    private static final String PREFS_NAME = "com.quitit.appwidget.AppWidget";
    private static final String PREF_PREFIX_KEY = "id_prefix_";
    
    String appWidgetId;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_tally);
        /*startService(new Intent(RunningTally.this,
                NotifyingService.class));
        Button button = (Button) findViewById(R.id.btnStartListener);
        button.setOnClickListener(mStartListener);
        button = (Button) findViewById(R.id.btnStopListener);
        button.setOnClickListener(mStopListener);
        updateTally();*/
        
        
        // Set the default time zone
        TimeZone.setDefault(tz);
        Toast.makeText(this, tz.getDisplayName(), Toast.LENGTH_LONG).show();

    }
    @Override
    public void onResume() {
    	super.onResume();
    	autoUpdate = new Timer();
    	autoUpdate.schedule(new TimerTask() {
	      @Override
	      public void run() {
	    	  	runOnUiThread(new Runnable() {
	    	  		public void run() {
	    	  			updateTally();
	    	  		}
	    });
      }
     }, 0, 1000); // updates each second
    }
    
    @Override
    public void onPause() {
    	autoUpdate.cancel();
    	super.onPause();
    }


    private void updateTally(){
    	
        Log.d(DEB_TAG, "Inside RUNNINGTALLY UPDATETALLY");
        
        
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	Log.d(DEB_TAG, "INSIDE IF EXTRAS NOT NULL updateTally");
        	mAppId = extras.getInt("widgetId");
        	Log.d(DEB_TAG, "Value of widget id grabbed from intent " + mAppId);
        }
        
        SharedPreferences prefs = context.getSharedPreferences(QUITIT.Preferences.PREF_NAME, 0);
        String prefix = prefs.getString(QUITIT.Preferences.WIDGET_PREFIX + mAppId, null);

        bd = new TimeBreakDown();
        bd.calculate(prefix);
        
        Log.d(DEB_TAG, "########Value of prefix is " + prefix);
        Log.d(DEB_TAG, "#########appWidgetId" + mAppId);

        tvStartDate	= (TextView)findViewById(R.id.cleanDate);
        tvDays 		= (TextView)findViewById(R.id.days);
        //tvMonths	= (TextView)findViewById(R.id.months);
        //tvYrs		= (TextView)findViewById(R.id.years);
        tvHours		= (TextView)findViewById(R.id.hours);
        tvMins		= (TextView)findViewById(R.id.minutes);
        tvSecs		= (TextView)findViewById(R.id.seconds);
        //tvMilSecs	= (TextView)findViewById(R.id.millisecs);
        //btnExtras	= (Button)findViewById(R.id.btnExtra);
        
        /* tvDays.setText(TimeDifference.getDaysDifference(prefix));
        tvHours.setText(TimeDifference.getHoursDifference(prefix));
        tvMins.setText(TimeDifference.getMinutesDifference(prefix));
        tvSecs.setText(TimeDifference.getSecondsDifference(prefix));
        tvMilSecs.setText(TimeDifference.getMilisecondsDifference(prefix));*/
        
        tvStartDate.setText(prefix);
        tvDays.setText(Double.toString(bd.daysOld));
        tvHours.setText(Double.toString(bd.hrsOld));
        tvMins.setText(Double.toString(bd.minsOld));
        tvSecs.setText(Double.toString(bd.secOld));
       // tvMilSecs.setText(Double.toString(bd.msOld));
    }
    /*private OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v)
        {
            // Make sure the service is started.  It will continue running
            // until someone calls stopService().  The Intent we use to find
            // the service explicitly specifies our service component, because
            // we want it running in our own process and don't want other
            // applications to replace it.
            startService(new Intent(RunningTally.this,
                    NotifyingService.class));
        }
    };

    private OnClickListener mStopListener = new OnClickListener() {
        public void onClick(View v)
        {
            // Cancel a previous call to startService().  Note that the
            // service will not actually stop at this point if there are
            // still bound clients.
            stopService(new Intent(RunningTally.this,
                    NotifyingService.class));
        }
    };*/

	/* ------------------ LocationListener Interface functions ---------------------- */
	public void onLocationChanged(Location location) {
		
		Log.d(DEB_TAG, "*************In 'onLocationChanged'.");
		
		// get new time zone
		TimeZone tz  = TimeZone.getDefault();
		
		// Set the default time zone
        TimeZone.setDefault(tz);
        Toast.makeText(this, tz.getDisplayName(), Toast.LENGTH_LONG).show();
		
	}
	
	/** ---------------------- options menu ------------------------- */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){

    	// About
		case R.id.main_menu_about:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		
		// Preferences
		case R.id.main_menu_edit:
			Log.d("###########", "inside edit pref choice");
			 // Find the widget id from the intent. 
	        Intent configIntent = new Intent(this, AppWidgetConfigure.class);
	        configIntent.putExtra("widgetId", mAppId);
	        //SharedPreferences prefs = context.getSharedPreferences(QUITIT.Preferences.PREF_NAME, 0);
	        //String prefix = prefs.getString(QUITIT.Preferences.WIDGET_PREFIX + id, null);
	        Log.d(DEB_TAG, "id in edit " + mAppId);
			startActivity(configIntent);
			updateTally();
			break;
		}
		return(super.onOptionsItemSelected(item));
	}
	
}
