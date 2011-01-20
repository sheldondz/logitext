package tk.logistics.gateway;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSStatusUpdate extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("KALSMS", "SMS ACTION:\"" + intent.getAction() );
		Log.d("KALSMS", "SMS OUTPUT:\"" + getResultCode() );
		if(intent.getAction().equals("tk.logistics.gateway.SMS_SENT")) {
			switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS sent", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Generic failure", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "No service", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "Null PDU", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "Radio off", 
                            Toast.LENGTH_SHORT).show();
                    break;
            }
		}else if(intent.getAction().equals("tk.logistics.gateway.SMS_DELIVERED")) {
			switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS delivered", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered", 
                            Toast.LENGTH_SHORT).show();
                    break;                        
            }
        }
	}
}
