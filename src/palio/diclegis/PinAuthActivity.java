/**
 * @(#)PinAuthActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/30
 */
package palio.diclegis;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinAuthActivity extends PatientInfoActivity implements View.OnClickListener
{
	EditText enterPin;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_auth_screen);
        setTitle(getResources().getString(R.string.app_title));
        findViewById(R.id.submitpin).setOnClickListener(this);
        enterPin = (EditText)findViewById(R.id.pin_auth);
    }
    
    public void onClick(View v) 
    {
    	String s = enterPin.getText().toString();
    	if (getPatientInfo().pin.equals(s)) startActivity("StartupActivity");
    	else 
    	{
    		Toast.makeText(this, R.string.pin_incorrect, 800).show();
    		enterPin.setText("");
    	}	
    }
}