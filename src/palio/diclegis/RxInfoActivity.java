/**
 * @(#)RxInfoActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/30
 */
package palio.diclegis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ToggleButton;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;

public class RxInfoActivity extends PatientInfoActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
	final static String dateFormat="MM/dd/yyy";
	Dialog scriptCountDialog;
	Dialog refillCountDialog;
	GregorianCalendar cal = new GregorianCalendar();
	TextView amDose;
	ToggleButton morningDoseToggle;
	TextView pmDose;
	ToggleButton afternoonDoseToggle;
	TextView btDose;
	ToggleButton bedtimeDoseToggle;
	
	TextView scriptDate;
	TextView noOfTabs;
	TextView noOfRefill;
	
	Dialog labelIndication;
	Dialog fillForm;
	boolean formIsFilled;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_info_screen);
        setTitle(getResources().getString(R.string.script_info));
        findViewById(R.id.set_script_date).setOnClickListener(this);
        findViewById(R.id.set_script_count).setOnClickListener(this);
        findViewById(R.id.set_refill_count).setOnClickListener(this);
        findViewById(R.id.set_pin).setOnClickListener(this);
        
        morningDoseToggle=(ToggleButton)findViewById(R.id.am_dose_toggle);
        morningDoseToggle.setOnClickListener(this);
        morningDoseToggle.setChecked(true);
        amDose = (TextView) findViewById(R.id.set_am_dose);
        cal.setTime(getPatientInfo().amDose);
        amDose.setText(getFormattedTime(cal));
        amDose.setOnClickListener(this);
        
        afternoonDoseToggle=(ToggleButton)findViewById(R.id.pm_dose_toggle);
        afternoonDoseToggle.setOnClickListener(this);
        afternoonDoseToggle.setChecked(true);
        pmDose = (TextView) findViewById(R.id.set_pm_dose);
        cal.setTime(getPatientInfo().pmDose);
        pmDose.setText(getFormattedTime(cal));
        pmDose.setOnClickListener(this);
        
        bedtimeDoseToggle=(ToggleButton)findViewById(R.id.bt_dose_toggle);
        bedtimeDoseToggle.setOnClickListener(this);
        bedtimeDoseToggle.setChecked(true);
        btDose = (TextView) findViewById(R.id.set_bt_dose);
        cal.setTime(getPatientInfo().btDose);
        btDose.setText(getFormattedTime(cal));
        btDose.setOnClickListener(this);
        
        scriptDate = (TextView) findViewById(R.id.script_date);
        String sd = getResources().getString(R.string.script_date)+" "+getDate(getPatientInfo().refillDate);
        scriptDate.setText(sd);
        
        noOfTabs = (TextView) findViewById(R.id.script_count);
        noOfTabs.setText(getResources().getString(R.string.script_count)+" "+getPatientInfo().numberOfTablets);
        
        noOfRefill = (TextView) findViewById(R.id.refill_count);
        noOfRefill.setText(getResources().getString(R.string.refill_count)+" "+getPatientInfo().numberOfRefills);
        setDoseVis();
        
        labelIndication = new Dialog(this);
        labelIndication.setContentView(R.layout.label_indication_view);
        labelIndication.findViewById(R.id.label_indication_close).setOnClickListener(this);
        labelIndication.setTitle(R.string.label_indication_remind);
        
        fillForm = new Dialog(this);
        fillForm.setContentView(R.layout.fill_form_view);
        fillForm.findViewById(R.id.fill_form_close).setOnClickListener(this);
        fillForm.setTitle(R.string.fill_form_title);
        
    }
    
    public void onClick(View v) 
    {	
    	int id = v.getId();
    	switch(id)
    	{
    		case R.id.set_script_date:
    			cal.setTime(getPatientInfo().refillDate);
    			new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    			break;
    		case R.id.set_script_count:
    		    scriptCountDialog = new Dialog(this);
    		    scriptCountDialog.setContentView(R.layout.script_count_screen);
    		    TextView tv = (TextView) scriptCountDialog.findViewById(R.id.enter_script_count);
    		    tv.setText(getPatientInfo().numberOfTablets+"");
    		    scriptCountDialog.findViewById(R.id.save_script_count).setOnClickListener(this);
    		    scriptCountDialog.show();
    			break;
    		case R.id.set_refill_count:
    		    refillCountDialog = new Dialog(this);
    		    refillCountDialog.setContentView(R.layout.refill_count_screen);
    		    TextView tvr = (TextView) refillCountDialog.findViewById(R.id.enter_refill_count);
    		    tvr.setText(getPatientInfo().numberOfRefills+"");
    		    refillCountDialog.findViewById(R.id.save_refill_count).setOnClickListener(this);
    		    refillCountDialog.show();
    			break;
    		case R.id.save_script_count:
    			TextView ttv = (TextView) scriptCountDialog.findViewById(R.id.enter_script_count);
    			Integer I = new Integer(ttv.getText().toString());
    			int i = I.intValue();
    			getPatientInfo().numberOfTablets=i;
    			updatePatientInfo();
    			scriptCountDialog.dismiss();
    			noOfTabs.setText(getResources().getString(R.string.script_count)+" "+getPatientInfo().numberOfTablets);
    			break;
    		case R.id.save_refill_count:
    			TextView ttvr = (TextView) refillCountDialog.findViewById(R.id.enter_refill_count);
    			Integer II = new Integer(ttvr.getText().toString());
    			int ii = II.intValue();
    			getPatientInfo().numberOfRefills=ii;
    			updatePatientInfo();
    			refillCountDialog.dismiss();
    			noOfRefill.setText(getResources().getString(R.string.refill_count)+" "+getPatientInfo().numberOfRefills);
    			break;
    		case R.id.set_am_dose:
    			cal = new GregorianCalendar();
    			cal.setTime(getPatientInfo().amDose);
    			new TimePickerDialog(this, new AMTimeSetListener(), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    			break;
    		case R.id.am_dose_toggle:
    			amDose.setEnabled(morningDoseToggle.isChecked());
    			if (!morningDoseToggle.isChecked()) labelIndication.show();
    			getPatientInfo().amRemind = morningDoseToggle.isChecked();
    			updatePatientInfo();
    			refreshReminders();
    			break;
    		case R.id.set_pm_dose:
    			cal = new GregorianCalendar();
    			cal.setTime(getPatientInfo().pmDose);
    			new TimePickerDialog(this, new PMTimeSetListener(), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    			break;
    		case R.id.pm_dose_toggle:
    			pmDose.setEnabled(afternoonDoseToggle.isChecked());
    			if (!afternoonDoseToggle.isChecked()) labelIndication.show();
    			getPatientInfo().pmRemind = afternoonDoseToggle.isChecked();
    			updatePatientInfo();
    			refreshReminders();
    			break;
    			
    		case R.id.set_bt_dose:
    			cal = new GregorianCalendar();
    			cal.setTime(getPatientInfo().btDose);
    			new TimePickerDialog(this, new BTTimeSetListener(), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    			break;
    		case R.id.bt_dose_toggle:
    			btDose.setEnabled(bedtimeDoseToggle.isChecked());
    			if (!bedtimeDoseToggle.isChecked()) labelIndication.show();
    			getPatientInfo().btRemind = bedtimeDoseToggle.isChecked();
    			updatePatientInfo();
    			refreshReminders();
    			break;
    		case R.id.set_pin:
    			if (!formIsFilled) 
    			{
    				fillForm.show();
    				break;
    			}
        		startActivity("PinEntryActivity");
    			break;
    		case R.id.label_indication_close:
    			labelIndication.dismiss();
    			break;
    		case R.id.fill_form_close:
    			fillForm.dismiss();
    			break;
    	}
    
    }
    
    public String getAmPm(int i)
    {
    	String s ="";
    	switch(i)
    	{
    		case 0:
    			s = getResources().getString(R.string.am);
    			break;
    		case 1:
    			s = getResources().getString(R.string.pm);
    			break;
    	}
    	return s;
    }
    
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
    	GregorianCalendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
    	getPatientInfo().refillDate = cal.getTime();
    	updatePatientInfo();
    	String sd = getResources().getString(R.string.script_date)+" "+getDate(getPatientInfo().refillDate);
        scriptDate.setText(sd);	
    }
    
    public void refreshReminders()
    {
    	Intent intent = new Intent(getResources().getString(R.string.system_refresh_notify));
    	sendBroadcast(intent);
    }
    
    class AMTimeSetListener implements TimePickerDialog.OnTimeSetListener
    {
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    	{
    		formIsFilled=true;
    		cal = new GregorianCalendar();
    		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
    		cal.set(Calendar.MINUTE, minute);
    		getPatientInfo().amDose = cal.getTime();
    		updatePatientInfo();
    		amDose.setText(getFormattedTime(cal));
    		refreshReminders();
    		Toast.makeText(RxInfoActivity.this, getResources().getString(R.string.reminder_set) + getFormattedTime(cal), 900).show();
    	}
    } 
    
    class PMTimeSetListener implements TimePickerDialog.OnTimeSetListener
    {
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    	{
    		formIsFilled=true;
    		cal = new GregorianCalendar();
    		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
    		cal.set(Calendar.MINUTE, minute);
    		getPatientInfo().pmDose = cal.getTime();
    		updatePatientInfo();
    		pmDose.setText(getFormattedTime(cal));
    		refreshReminders();
    		Toast.makeText(RxInfoActivity.this, getResources().getString(R.string.reminder_set) + getFormattedTime(cal), 900).show();
    	}
    } 
    	
    class BTTimeSetListener implements TimePickerDialog.OnTimeSetListener
    {
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    	{
    		formIsFilled=true;
    		cal = new GregorianCalendar();
    		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
    		cal.set(Calendar.MINUTE, minute);
    		getPatientInfo().btDose = cal.getTime();
    		updatePatientInfo();
    		btDose.setText(getFormattedTime(cal));
    		refreshReminders();
    		Toast.makeText(RxInfoActivity.this, getResources().getString(R.string.reminder_set) + getFormattedTime(cal), 900).show();
    	}
    }
    
    public String getFormattedTime(Calendar cl)
    {
    	return get12Hr(cl.get(Calendar.HOUR))+":"+getFormattedMinute(cl.get(Calendar.MINUTE))+" "+getAmPm(cl.get(Calendar.AM_PM));
    }
    
    public String getFormattedMinute(int min)
    {
    	if (min < 10) return "0"+min;
    	else return ""+min;
    }
    
    public int get12Hr(int hr) 
    {
    	if (hr==0) return 12;
    	else return hr;
    }
    
    public String getDate(Date date)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	StringBuffer buffer = new StringBuffer();
    	sdf.format(date, buffer, new FieldPosition(0));
    	return buffer.toString();
    }
    
    private void setDoseVis()
    {
    	if ( getPatientInfo().doseFrequency==0 ) 
    	{
    		findViewById(R.id.am_dose).setVisibility(View.GONE);
    		findViewById(R.id.am_dose_label).setVisibility(View.GONE);
    	}
    }
}