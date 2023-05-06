/**
 * @(#)ReminderActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/9
 */
package palio.diclegis;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class ReminderActivity extends PatientInfoActivity implements View.OnClickListener
{   
    int notid;
    NotificationManager notManager;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_screen);
        setTitle(R.string.dosing_reminder);
        findViewById(R.id.dismiss_reminder).setOnClickListener(this);
        findViewById(R.id.rate).setOnClickListener(this);
        notid = getIntent().getIntExtra(getResources().getString(R.string.system_notid), 0);
        notManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    public void onClick(View v) 
    {	
    	int id = v.getId();
    	switch(id)
    	{
    		case R.id.dismiss_reminder:
    			notManager.cancelAll();
    			finish();
    			break;
    		case R.id.rate:
    			break;
    	}
    }
}