/**
 * @(#)PatientInfoActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/26
 */
package palio.diclegis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import java.util.Date;
/* Base class for Activities needing access to PatientInfo, initializes / retrieves PatientInfo asynchronously on creation.
 * Saves patient data by calling asynch update method in PatientInfoManager
 * Also has methods for date handling and activity starting. */

public class PatientInfoActivity extends Activity implements PatientInfoInterface
{
	private static PatientInfoManager manager;
    private static PatientInfo info; 
	private static Handler backgroundHandler;
	private int sessionTime = 24;
	boolean dayPassed;// tracks whether or not a day has passed since Activity start
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        backgroundHandler = new Handler(getMainLooper());
        backgroundHandler.post(new GetData()); // get data asynchrously to avoid blocking UI thread
    }
    
    
    class GetData extends Thread  // Thread for asycnh init and data retrieval
    {
    	public void run()
    	{
    		manager = PatientInfoManager.getInstance(PatientInfoActivity.this.getApplicationContext());
    	    info = manager.getPatientInfo();
    	}
    }
    
    public void updatePatientInfo()  
    {
    	manager.updatePatientInfo(info);
    }
    
    public void startActivity(String act) // convienence method for starting activities in the same package
    {
    	String pack = getPackageName();
    	act = pack+"."+act;
    	Intent intent1 = new Intent();
        intent1.setClassName(pack, act);
        startActivity(intent1);
    }
    
    public void checkLastAppStart()
    {
    	Date now = new Date();
    	long lnow = now.getTime();
    	long last = info.lastAppStart.getTime();
    	long elapsed = lnow-last;
    	elapsed = elapsed/1000;
    	long hrs = elapsed/(3600);
    	dayPassed = (hrs > sessionTime);
    }
    
    public void saveStartDate()
    {
    	info.lastAppStart = new Date();
    	updatePatientInfo();
    }
    
    public PatientInfo getPatientInfo()
    {
    	return info;
    }	
}