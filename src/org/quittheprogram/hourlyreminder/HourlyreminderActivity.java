package org.quittheprogram.hourlyreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HourlyreminderActivity extends Activity {
    private static final String ISALARMENABLED = "isAlarmEnabled";
	private AlarmManager alarmManager;
	final private long INTERVAL = 3600000; //30000 = 30 seconds, 3600000 = 1 hour
	private SharedPreferences settings;
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView textView = (TextView) findViewById(R.id.lblMessage);
        final ToggleButton btnToggle = (ToggleButton) findViewById(R.id.btnToggleReminders);
        
        settings = PreferenceManager.getDefaultSharedPreferences(this); 
        boolean isAlarmEnabled = settings.getBoolean(ISALARMENABLED, false);
        
        if (isAlarmEnabled){
        	textView.setText("Reminders are active");
        	btnToggle.setChecked(true);
        }
        
        btnToggle.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (btnToggle.isChecked()){
                	textView.setText("Reminders are active");
                	enableAlarm();
                }
                else {
                	textView.setText("No reminders. Go to sleep!");
                	disableAlarm();
                }
            }
        });                      
       
    }

	private void enableAlarm() {
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		PendingIntent pendingAlarmIntent = getPendingIntent();
		alarmManager.cancel(pendingAlarmIntent); //cancel existing alarms
		
		//add new alarm
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVAL, pendingAlarmIntent);
        
        saveAlarmState(true);
	}

	private void saveAlarmState(boolean value) {
		settings = PreferenceManager.getDefaultSharedPreferences(this);
        Editor settingsEditor = settings.edit();
		settingsEditor.putBoolean(ISALARMENABLED, value);		
		settingsEditor.commit();
	}

	private PendingIntent getPendingIntent() {
		Intent alarmReceiver = new Intent(this, ScheduleAlarmReceiver.class);
		return PendingIntent.getBroadcast(this, 0, alarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private void disableAlarm(){
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(getPendingIntent());
		saveAlarmState(false);
	}
}