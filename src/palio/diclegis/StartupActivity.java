/**
 * @(#)StartupActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/24
 */
package palio.diclegis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StartupActivity extends PatientInfoActivity
{
	long delay = 1200;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler(getMainLooper()).postDelayed(new CheckStartDate(), 200);
        new Handler(getMainLooper()).postDelayed(new SaveStartDate(), 400);
        new Handler(getMainLooper()).postDelayed(new StartActivity(), delay);
        new Handler(getMainLooper()).postDelayed(new StartReminderService(), 500);
        
    }
    
    class StartActivity extends Thread
    {
    	public void run()
    	{
    		if ( getPatientInfo().firstStart ) 
    		{
    			setInitialDoseTimes();
    			
    			startActivity("AboutActivity");
    			return;
    		}
    		if ( dayPassed ) startActivity("AboutActivity");
    		else startActivity("AgreementsActivity");
            
    	}
    }
    
    class CheckStartDate extends Thread
    {
    	public void run()
    	{
    		checkLastAppStart();
    	}
    }
    
    class SaveStartDate extends Thread
    {
    	public void run()
    	{
    		saveStartDate();
    	}
    }
    
    private void setInitialDoseTimes()
    {
    	GregorianCalendar  cal = new GregorianCalendar();
    	cal.setTime(getPatientInfo().amDose);
    	cal.set(Calendar.HOUR_OF_DAY, 8);
    	cal.set(Calendar.MINUTE, 0);
    	getPatientInfo().amDose = cal.getTime();
    	
    	cal = new GregorianCalendar();
    	cal.setTime(getPatientInfo().pmDose);
    	cal.set(Calendar.HOUR_OF_DAY, 13);
    	cal.set(Calendar.MINUTE, 0);
    	getPatientInfo().pmDose = cal.getTime();
    	
    	cal = new GregorianCalendar();
    	cal.setTime(getPatientInfo().btDose);
    	cal.set(Calendar.HOUR_OF_DAY, 21);
    	cal.set(Calendar.MINUTE, 0);
    	getPatientInfo().btDose = cal.getTime();
    	
    	updatePatientInfo();
    }
    
    public void startReminderService()
    {
    	if (!getPatientInfo().firstStart) return;
    	String pack = getPackageName();
    	String s = pack+".ReminderService";
    	Intent intent = new Intent();
        intent.setClassName(pack, s);
        startService(intent);
    }
    
    public class StartReminderService extends Thread
    {
    	public void run()
    	{
    		startReminderService();
    	}
    }
}