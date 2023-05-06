/**
 * @(#)PinEntryActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/26
 */
package palio.diclegis;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinEntryActivity extends PatientInfoActivity implements View.OnClickListener
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_entry_screen);
        setTitle(getResources().getString(R.string.app_title));
        findViewById(R.id.savepin).setOnClickListener(this);
    }
    
    public void onClick(View v) 
    {
    	EditText pt = (EditText) findViewById(R.id.pin);
    	String s = pt.getText().toString();
    	getPatientInfo().pin=s;
    	s=s.trim();
    	
    	if ( s.length() < 4)
    	{
    		Toast.makeText(this, R.string.enter_4dig_pen, 800).show();
    		return;
    	}
    
    	updatePatientInfo();
    	Toast.makeText(this, R.string.pin_saved, 800).show();
        startActivity("StartupActivity");
    }
}