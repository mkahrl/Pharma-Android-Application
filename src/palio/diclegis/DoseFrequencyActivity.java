/**
 * @(#)DoseFrequencyActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/26
 */
package palio.diclegis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RadioButton;

public class DoseFrequencyActivity extends PatientInfoActivity implements View.OnClickListener, AdapterView.OnItemClickListener 
{
	ListView dosage;
	Button rxNext;
	String[] freqs;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dose_frequency_screen);
        setTitle(getResources().getString(R.string.app_title));
        dosage = (ListView) findViewById(R.id.dosage_frequency);
        freqs = getResources().getStringArray(R.array.dosage_frequency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dosage_frequency, android.R.layout.simple_list_item_single_choice);
        dosage.setAdapter(adapter);
        dosage.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        rxNext = (Button) findViewById(R.id.rxnext);
        rxNext.setEnabled(false);
        rxNext.setOnClickListener(this);
        dosage.setOnItemClickListener(this);
    }
    
    public void onClick(View v) 
    {	
    	int dose = dosage.getCheckedItemPosition();
    	getPatientInfo().doseFrequency=dose;
    	updatePatientInfo();
        startActivity("RxInfoActivity");
    }
    
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {
    	if (rxNext!=null) rxNext.setEnabled(true);
    }
}