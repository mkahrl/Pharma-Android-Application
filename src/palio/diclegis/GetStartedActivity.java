/**
 * @(#)GetStartedActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/29
 */
package palio.diclegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class GetStartedActivity extends PatientInfoActivity implements View.OnClickListener
{
    Button next;
    Button find;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_screen);
        setTitle(getResources().getString(R.string.get_started));
        
        next = (Button)findViewById(R.id.have_script);
        next.setOnClickListener(this);
        find = (Button)findViewById(R.id.need_script);
        find.setOnClickListener(this);
        findViewById(R.id.dashboard).setOnClickListener(this);
        /// when the user reaches this activity for the first time, consider the application started for the first time
        getPatientInfo().firstStart=false;
        updatePatientInfo();
    }
    
    public void onClick(View v) 
    {
    	switch(v.getId())
    	{
    		case R.id.have_script:
        		startActivity("DoseFrequencyActivity");
    			break;
    		case R.id.dashboard:
        		startActivity("DashboardActivity");
    			break;
    		case R.id.need_script:
    			Intent intent = new Intent();
    			/// Starts Google Maps for now, needs to be replaced with search call to Google Maps, or custom 
    			/// activity using embedded MapView / Google Places
    			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        		startActivity(intent);
    			break;
    	}
    }
}
