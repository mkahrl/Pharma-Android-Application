/**
 * @(#)RefillActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/14
 */
package palio.diclegis;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RefillActivity extends PatientInfoActivity implements View.OnClickListener
{   
    int notid;
    NotificationManager notManager;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill_screen);
        setTitle(R.string.refill_script);
        findViewById(R.id.refill_dismiss_reminder).setOnClickListener(this);
        findViewById(R.id.find_pharma).setOnClickListener(this);
        notid = getIntent().getIntExtra(getResources().getString(R.string.system_notid), 0);
        notManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    public void onClick(View v) 
    {	
    	int id = v.getId();
    	switch(id)
    	{
    		case R.id.refill_dismiss_reminder:
    			notManager.cancel(notid);
    			finish();
    			break;
    		case R.id.find_pharma:
    			Intent intent = new Intent();
    			/// Starts Google Maps for now, needs to be replaced with search call to Google Maps, or custom 
    			/// activity using embedded MapView / Google Places
    			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        		startActivity(intent);
    			break;
    	}
    }
}