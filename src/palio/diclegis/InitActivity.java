/**
 * @(#)InitActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/30
 */
package palio.diclegis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InitActivity extends PatientInfoActivity
/* This activity is the entry point for the application. Displays a splash screens, initalizes data and
 * checks if the user has a pin. */
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setTitle(getResources().getString(R.string.app_title));
        new Handler(getMainLooper()).postDelayed(new CheckForPin(), 200);// Asynch check for pin, gives time for data to load.
    }
    
    public void checkForPin()
    {
    	/// if no pin exists, force the user to enter one.
    	if ((getPatientInfo().pin==null) || (getPatientInfo().pin.length()<4)) startActivity("PinEntryActivity");
    	/// if there is a pin, have the user enter it.
    	else startActivity("PinAuthActivity");
    }
    
    public class CheckForPin extends Thread
    {
    	public void run()
    	{
    		checkForPin();
    	}
    }
}