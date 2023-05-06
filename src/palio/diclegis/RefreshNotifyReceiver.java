/**
 * @(#)RefreshNotifyReceiver.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/10
 */
package palio.diclegis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshNotifyReceiver extends BroadcastReceiver
{
	ReminderService service;
	public RefreshNotifyReceiver(ReminderService service)
	{
		this.service=service;
	}
	
	public void onReceive(Context context, Intent intent) 
	{
		service.refreshNotifications();
	}
}