/**
 * @(#)PatientInfoManager.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/25
 */

package palio.diclegis;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* This class stores patient and prescription data in application files that are both permission
 * protected and encrypted with AES 128 encryption using a self generated and stored key. */
public class PatientInfoManager
{
	/// static data, could be moved to property file
	private String seed = "76rtfd5";
	private final static String filename = "picachefile";
	private final static String keyfile = "mkpiu63f";
	private final static int encyptionStrength = 128;
	private final static String encyptionMethod = "AES";
	private final static String loggerName= "pilogger";
	private final static String TAG = "PatientInfoManager";
	
	private byte[] key;
	private static SecretKeySpec skeySpec;
	private Context ctx;
	private static Logger logger;
	private static PatientInfoManager instance;
	private static Handler dataHandler;
	private static PatientInfo patientInfo;
	
	/// Singleton class, access from this static method.
	public static PatientInfoManager getInstance(Context ctx)
	{
		if (instance==null) instance = new PatientInfoManager(ctx);
		return instance;
	}
	
    private PatientInfoManager(Context ctx) 
    {
    	this.ctx=ctx.getApplicationContext();
    	logger = Logger.getLogger(loggerName);
    	// If no key exists generate one and save it. Otherwise read and used the saved key.
    	try
    	{
    		palio.diclegis.StoredKey ky = getSavedKey();
    		if (ky==null)
    		{
    			key = generateKey(seed);
    			saveKey(key);
    		}
    		else
    		{
    			key = ky.key;
    		}
    		skeySpec = new SecretKeySpec(key, encyptionMethod);
    		dataHandler = new Handler(ctx.getMainLooper());
    	}
    	catch(Exception e)
    	{
    		logger.log(Level.SEVERE, "Error generating keys !  "+e.toString(), TAG);
    	}
    	
    }
    
    private synchronized void saveKey(byte[] key)
    {
    	palio.diclegis.StoredKey ky = new palio.diclegis.StoredKey();
    	ky.key=key;
    	File dir = ctx.getCacheDir();
   	    File file = new File(dir, keyfile);
   	    try
    	{
    		file.createNewFile();
    		FileOutputStream out = new FileOutputStream(file);
    		ObjectOutputStream oout = new ObjectOutputStream(out);
    		oout.writeObject(ky);
    		oout.flush();
    		oout.close();
    		out.close();
    	}
    	catch(Exception e)
    	{
    		logger.log(Level.SEVERE, "Error saving key! "+e.toString(), TAG);
    	}
    }
    
    private synchronized palio.diclegis.StoredKey getSavedKey()
    {
    	StoredKey key=null;
    	File dir = ctx.getCacheDir();
   	    File file = new File(dir, keyfile);
   	    
   	    try
    	{
    		FileInputStream in = new FileInputStream(file);
    		ObjectInputStream oin = new ObjectInputStream(in);
    	    key = (StoredKey) oin.readObject();
    		in.close();
    		oin.close();
    	}
    	catch(Exception e)
    	{
    		logger.log(Level.SEVERE, e.toString(), TAG);
    	}
    	return key;
    }
    
    public synchronized PatientInfo getPatientInfo() // gets the stored and encypted patient data.
    /// run on non UI thread on application startup to initialize data.
    {
	    PatientInfo pi = new PatientInfo();
		File dir = ctx.getCacheDir();
   	    File file = new File(dir, filename);
   	    try
    	{
    		FileInputStream in = new FileInputStream(file);
    		ObjectInputStream oin = new ObjectInputStream(in);
    		SealedObject sealedInfo = (SealedObject) oin.readObject();
    		pi = (PatientInfo) sealedInfo.getObject(getDecryptCipher());
    		in.close();
    		oin.close();
    	}
    	catch(Exception e)
    	{
    		logger.log(Level.SEVERE, e.toString(), TAG);
    	}
    	return pi;
    }
    
    private synchronized void savePatientInfo(PatientInfo info) // serializes, encrypts and saves patient data.
    {
    	File dir = ctx.getCacheDir();
   	    File file = new File(dir, filename);
	    try
    	{
    		file.createNewFile();
    		FileOutputStream out = new FileOutputStream(file);
    		ObjectOutputStream oout = new ObjectOutputStream(out);
    		SealedObject sealedInfo = new SealedObject(info, getEncryptCipher());
    		String alg = sealedInfo.getAlgorithm();
    		oout.writeObject(sealedInfo);
    		oout.flush();
    		oout.close();
    		out.close();
    	}
    	catch(Exception e)
    	{
    		logger.log(Level.SEVERE, "Error saving data in PatientInfoManager! "+e.toString(), TAG);
    	}
    }
    
    public void updatePatientInfo(PatientInfo info) // Updates, encrypts and stores the new patient data. runs asynchronously
    {
    	dataHandler.post(new SavePatientData(info));
    }
    
    class SavePatientData extends Thread  /// Thread for encrypting , saving data asynchronously
    {
    	private PatientInfo pinfo;
    	public SavePatientData(PatientInfo info)
    	{
    		pinfo = info;
    	}
    	public void run()
    	{
    		savePatientInfo(pinfo);
    	}
    }
    
    private static synchronized byte[] generateKey(String seed) throws Exception
    {
        byte[] keyStart = seed.getBytes();
        KeyGenerator kgen = null;
        
        try
        {
        	kgen = KeyGenerator.getInstance(encyptionMethod);
        }
        catch(Exception e)
        {
        	logger.log(Level.SEVERE,"Error getting key generator: "+e.toString(), TAG);
        	return new byte[0];
        }
        SecureRandom sr = new SecureRandom();
        sr.setSeed(keyStart);
        kgen.init(encyptionStrength, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }
    
    private static Cipher getEncryptCipher() 
    {
    	Cipher cipher = null;
    	try
    	{
    		cipher = Cipher.getInstance(encyptionMethod);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    	}
        catch(Exception e)
        {
        	logger.log(Level.SEVERE, "Exception creating Encryption cipher: "+e.toString(), TAG);
        }
        return cipher;
    }
    
    private static Cipher getDecryptCipher() 
    {
    	Cipher cipher = null;
    	try
    	{
    		cipher = Cipher.getInstance(encyptionMethod);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    	}
        catch(Exception e)
        {
        	logger.log(Level.SEVERE, "Exception creating Decryption cipher: "+e.toString(), TAG);
        }
        return cipher;
    }
}