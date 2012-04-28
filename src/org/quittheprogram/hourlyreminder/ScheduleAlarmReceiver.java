package org.quittheprogram.hourlyreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleAlarmReceiver extends BroadcastReceiver{
	final private int REQUESTCODE = 1345265;

    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(this.getClass().toString(), "alarm received");
    	NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	Intent notificationIntent = new Intent(context, HourlyreminderActivity.class);    	  
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUESTCODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	Notification.Builder builder = new Notification.Builder(context);
    	builder.setContentIntent(pendingIntent)
    	            .setSmallIcon(R.drawable.alarm_icon)
    	            .setTicker(context.getString(R.string.notification_status_bar_text))
    	            .setWhen(System.currentTimeMillis())
    	            .setAutoCancel(true)
    	            .setDefaults(Notification.DEFAULT_ALL)
    	            .setContentTitle(context.getString(R.string.notification_title))
    	            .setContentText(context.getString(R.string.notification_message));
    	Notification notification = builder.getNotification();        
        //TODO: text updaten bij meerdere
        
        notificationManager.notify(1, notification);      
        Log.d(this.getClass().toString(), "notification send");
    }	

}