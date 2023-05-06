/**
 * @(#)ReminderService.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/8
 */
package palio.diclegis;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ReminderService extends Service
{
	/* Background service to handle dosage and Rx notifications */
	Handler handler; // Handler to run threads on
	NotificationManager notManager;
	PatientInfo patientInfo;
	PatientInfoManager infoManager;
	RefreshNotifyReceiver receiver; // Broadcast receiver to receive broadcast intents from Activities
	int currentNot; // current notification id.
	int currentDay;// current day, to track new day, reset notifications.
	long pollingDelay = 30*1000;  // 30 seconds  for development purposes.
	long timeBeforeNotify = 2*60*1000; // 2 minutes , time before dose time to fire notification.
	long refillRemindTime = 5*24*60*60*1000; // 5 days , time before refill to fire Rx refill notification.
	Vector<Date> queu = new Vector<Date>(4); // queu for dode notifications.
	final static String loggerName= "pilogger";
	final static String TAG = "ReminderService";
	static Logger logger;
	
	public void onCreate()
	{
		logger = Logger.getLogger(loggerName);
		logger.log(Level.INFO, "Palio reminder service starting .. ", TAG);
		handler = new Handler(getMainLooper());
		notManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		currentDay = cal.get(Calendar.DAY_OF_MONTH);
		
		infoManager = PatientInfoManager.getInstance(this.getApplicationContext());
		handler.postDelayed(new InitDailyNotifications(), 1000);
		
		receiver = new RefreshNotifyReceiver(this);
		IntentFilter refresh = new IntentFilter(getResources().getString(R.string.system_refresh_notify));
		registerReceiver(receiver, refresh);
		updateDailyNotifications();
		
		handler.postDelayed(new QueuThread(), pollingDelay);
	}
	
	void setNotificationsToToday() // sets daily notifications to current day.
	{
		patientInfo.amDose = setToCurrentDay(patientInfo.amDose);
		patientInfo.pmDose = setToCurrentDay(patientInfo.pmDose);
		patientInfo.btDose = setToCurrentDay(patientInfo.btDose);
		infoManager.updatePatientInfo(patientInfo);
	}
	
	void updateDailyNotifications() // clears and resets all daily notifications
	{
		logger.log(Level.INFO, "updateDailyNotifications() ", TAG);
		notManager.cancelAll();
		if ( patientInfo == null ) patientInfo = infoManager.getPatientInfo();
		setNotificationsToToday();
		queu.removeAllElements();
		if (patientInfo.amRemind) queu.add(patientInfo.amDose);
		if (patientInfo.pmRemind) queu.add(patientInfo.pmDose);
		if (patientInfo.btRemind) queu.add(patientInfo.btDose);
	
	}
	
	synchronized void processQueu() // processes to daily notifications
	{
		logger.log(Level.INFO, "processQueu() ", TAG);
		logger.log(Level.INFO, "Queu size : "+queu.size(), TAG);
		try
		{
			Iterator<Date> it = queu.iterator();
			while ( it.hasNext())
			{
				Date nt = it.next();
				long ttn = getTimeTilNotify(nt);
				if (( ttn > 0) && ( ttn < timeBeforeNotify)) 
				{
					if ( getHourOfDay() < 12 )checkRefill(); // check refill, if it's a morning dose.
					addDoseNotification(nt);
					queu.remove(nt);
				}
			}
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, "ERROR processing QUEU:  "+e.toString(), TAG);
		}
		
	}
	
	void checkRefill()
	{
		logger.log(Level.INFO, "checkRefill() ", TAG);
		patientInfo = infoManager.getPatientInfo();
		long ttrf = patientInfo.refillDate.getTime() - new Date().getTime();
		if ( ttrf < 0) return;
		if (ttrf < refillRemindTime)
		{
			addRefillNotification(patientInfo.refillDate);
		}                                                                                                                                     
	}
	
	void refreshNotifications()
	{
		logger.log(Level.INFO, "refreshNotifications()  ", TAG);
		patientInfo = infoManager.getPatientInfo();
		if (patientInfo!=null) updateDailyNotifications();
	}
	
	boolean newDay()
	{
		return (getDayOfMonth() != currentDay);
	}
	
	int getDayOfMonth()
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	int getHourOfDay()
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	long getTimeTilNotify(Date date)
	{
		GregorianCalendar notify = new GregorianCalendar();
		notify.setTime(date);
		notify.set(Calendar.DAY_OF_MONTH, currentDay);
		date = notify.getTime();
		logger.log(Level.INFO, "Time til notify (minutes) : " + (date.getTime() - new Date().getTime())/60000, TAG);
		return  date.getTime() - new Date().getTime();
	}
	
	class InitDailyNotifications extends Thread
	{
		public void run()
		{
			patientInfo = infoManager.getPatientInfo();
			if (patientInfo!=null)updateDailyNotifications();
		}
	}
	
	Date setToCurrentDay(Date date)
	{
		GregorianCalendar cdate = new GregorianCalendar();
		cdate.setTime(date);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, cdate.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cdate.get(Calendar.MINUTE));
		return cal.getTime();
	}
	
	void addDoseNotification(Date date)
	{
		currentNot++;
		Notification not = new Notification(R.drawable.intro_graphic, getResources().getString(R.string.dose_reminder), date.getTime());
		try
		{
			Intent intent = new Intent();
			intent.setClassName(getPackageName(), getPackageName()+".ReminderActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(getResources().getString(R.string.system_notid), currentNot);
			PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
			not.setLatestEventInfo(this, getResources().getString(R.string.dose_reminder) ,  getResources().getString(R.string.dose_time), pending);
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, e.toString(), TAG);
		}
		
		notManager.notify(currentNot, not);
				
	}
	
	void addRefillNotification(Date date)
	{
		currentNot++;
		Notification not = new Notification(R.drawable.intro_graphic, getResources().getString(R.string.refill_remind), date.getTime());
		try
		{
			Intent intent = new Intent();
			intent.setClassName(getPackageName(), getPackageName()+".RefillActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(getResources().getString(R.string.system_notid), currentNot);
			PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
			not.setLatestEventInfo(this, getResources().getString(R.string.refill_remind) , getResources().getString(R.string.refill_time), pending);
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, e.toString(), TAG);
		}
		
		notManager.notify(currentNot, not);		
	}
	
	public void onDestroy()
	{
		unregisterReceiver(receiver);
	}
	

	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	class QueuThread extends Thread
	{
		@Override
		public void run()
		{
			processQueu();
			if (newDay())
			{
				currentDay = getDayOfMonth();
				updateDailyNotifications();
				
			}
			handler.postDelayed(new QueuThread(), pollingDelay);
		}
	}
}


