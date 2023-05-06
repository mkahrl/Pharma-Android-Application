/**
 * @(#)PatientInfo.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/25
 */
package palio.diclegis;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;

public class PatientInfo implements Serializable
/* Data structure used to store and access patient information */
{
    public PatientInfo() 
    {
    }
    public String pin;
    public String securityQuestion = "what is your favorite color?";
    public String securityAnswer = "aardvark";
    public Date lastAppStart = new Date();
    public String notes = "";
    	
    public String productName="Diclegis";
    public String description=" as desribed";
    public String productNotes="";
    public int dose=10;
    public String doseUnits="mg";
    public int doseFrequency;// index of dose frequency as described in dosage_frequency String array in resources.
    public int numberOfRefills=5;
    public int numberOfTablets=32;
    public Date refillDate = new Date();
    public Date scriptFillDate = new Date();
    public Date amDose = new Date();
    public Date pmDose = new Date();
    public Date btDose = new Date();
    public boolean amRemind=true;
    public boolean pmRemind=true;
    public boolean btRemind=true;
    public boolean use24hr=false;
    public boolean firstStart=true;
    public Hashtable<String, String> extras  = new Hashtable<String, String>(); 
    
    @Override
    public String toString()
    {
    	return "PatientInfo: PIN: "+pin+" "+notes+" "+productName+description+" "+dose+doseUnits+" "+doseFrequency+" "+securityQuestion;
    }
}