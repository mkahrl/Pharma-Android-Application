/**
 * @(#)PatientInfoInterface.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/7
 */
package palio.diclegis;
/* common interface for classes that access patient data, interact directly with PatientInfoManager 
 */
public interface PatientInfoInterface
{
	public PatientInfo getPatientInfo();
	public void updatePatientInfo();
}