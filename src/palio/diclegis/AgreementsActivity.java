/**
 * @(#)AgreementsActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/2
 */
package palio.diclegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AgreementsActivity extends PatientInfoActivity implements View.OnClickListener
{
	Button next;
	Button view;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreements_screen);
        setTitle(getResources().getString(R.string.agreements));
       
        next = (Button)findViewById(R.id.agree_continue);
        next.setOnClickListener(this);
        view = (Button)findViewById(R.id.view_policies);
        view.setOnClickListener(this);
    }
    
    public void onClick(View v) 
    {
    	switch(v.getId())
    	{
    		case R.id.agree_continue:
        		startActivity("GetStartedActivity");
    			break;
    		case R.id.view_policies:
        		startActivity("AboutActivity");
    			break;
    	}
    }
    
    
}