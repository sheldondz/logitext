package tk.logistics.gateway;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSSender extends BroadcastReceiver  {

	public void onReceive(Context context, Intent intent) {
		
		// acquiring the wake clock to prevent device from sleeping while request is processed
		final PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wake = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "http_request");
		wake.acquire();

		// get settings
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String targetUrl =  settings.getString("pref_target_url", "");
		Log.d("KALSMS", "url:\"" + targetUrl);	
		TargetUrlRequest url = new TargetUrlRequest();
		// send the message to the URL
		String resp = url.openURL("","",targetUrl).toString();
		
		Log.d("KALSMS", "RESP:\"" + resp);
		
		// SMS back the response
		if (resp.trim().length() > 0) {
			ArrayList<ArrayList<String>> items = url.parseXML(resp);
			
			SmsManager smgr = SmsManager.getDefault();
			
			for (int j = 0; j < items.size(); j++) {
				String sendTo = items.get(j).get(0);
				String sendMsg = items.get(j).get(1);
				
				try {
					Log.d("KALSMS", "SEND MSG:\"" + sendMsg + "\" TO: " + sendTo);
					Intent sintent = new Intent(context, SMSStatusUpdate.class);
					sintent.setAction("tk.logistics.gateway.SMS_SENT");
			    	PendingIntent sentIntent = PendingIntent.getBroadcast(context,0,sintent, 0);
			    	Intent dintent = new Intent(context, SMSStatusUpdate.class);
			    	dintent.setAction("tk.logistics.gateway.SMS_DELIVERED");
			    	PendingIntent deliveryIntent = PendingIntent.getBroadcast(context,0,dintent, 0);
					smgr.sendTextMessage(sendTo, null, sendMsg, sentIntent, deliveryIntent);
				} catch (Exception ex) {
					Log.d("KALSMS", "SMS FAILED");
				}
			}
		}
		wake.release();
	}
}
