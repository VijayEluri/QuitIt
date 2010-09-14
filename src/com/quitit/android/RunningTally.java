package com.quitit.android;

import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunningTally extends Activity {
	// Debug tag
	private final String DEB_TAG = "RunningTally.java";
	
	private TimeZone tz  = TimeZone.getDefault();TextView tvDays;
    private TextView tvMonths;
    private TextView tvYrs;
    private TextView tvHours;
    private TextView tvSecs;
    private TextView tvMilSecs;
    private Button btnExtras;
    
   // private TimeDifference td;
    
    final Context context = RunningTally.this;
    private static final String PREFS_NAME = "com.quitit.appwidget.AppWidget";
    private static final String PREF_PREFIX_KEY = "id_prefix_";
    
    String appWidgetId;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = 0;
        Log.d(DEB_TAG, "Inside DAYCOUNT ONCREATE");
        setContentView(R.layout.running_tally);
        
     // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	id = extras.getInt("widgetId");
        }
        
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + id, null);
        
        Log.d(DEB_TAG, "########Value of prefix is " + prefix);
        if (prefix != null) {
        } else {
        }
        
        Log.d(DEB_TAG, "#########appWidgetId" + id);
       
        //td = new TimeDifference();
        
        tvDays 		= (TextView)findViewById(R.id.days);
        tvMonths	= (TextView)findViewById(R.id.months);
        tvYrs		= (TextView)findViewById(R.id.years);
        tvHours		= (TextView)findViewById(R.id.hours);
        tvSecs		= (TextView)findViewById(R.id.seconds);
        tvMilSecs	= (TextView)findViewById(R.id.millisecs);
        btnExtras	= (Button)findViewById(R.id.btnExtra);
        
        tvDays.setText(TimeDifference.getDaysDifference(prefix));
        tvHours.setText(TimeDifference.getHoursDifference(prefix));
        tvSecs.setText(TimeDifference.getSecondsDifference(prefix));
        tvMilSecs.setText(TimeDifference.getMilisecondsDifference(prefix));
        
        btnExtras.setOnClickListener(storeString);
        
        // Set the default time zone
        TimeZone.setDefault(tz);
        Toast.makeText(this, tz.getDisplayName(), Toast.LENGTH_LONG).show();

    }

    //  Set onClickListener to then call the calculate method
    private View.OnClickListener onCalculate = new View.OnClickListener() {
    	@Override
		public void onClick(View v) {
		}
	};
 
	/* ------------------ LocationListener Interface functions ---------------------- */
	public void onLocationChanged(Location location) {
		
		Log.d(DEB_TAG, "*************In 'onLocationChanged'.");
		
		// get new time zone
		TimeZone tz  = TimeZone.getDefault();
		
		// Set the default time zone
        TimeZone.setDefault(tz);
        Toast.makeText(this, tz.getDisplayName(), Toast.LENGTH_LONG).show();
		
	}
	private View.OnClickListener storeString = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent(context, GetExtraActivity.class);
	        myIntent.putExtra("foo", "wtf");
			startActivity(myIntent);
	        
		}
	};
	
}
