package sample.socket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Simple serializable Email class
 * Used for populating emails and saving them
 * inside the MailBox directory
 * 
 * the save path is determined from email address
 * username@domain
 * 
 * This object is replica of the client side
 * Email object that is fed to Message Object
 *
 */
public class Email implements Serializable,Nameable
{
	public static final String TO_KEY = "To";
	public static final String FROM_KEY = "From";
	public static final String CC_KEY = "CC";
	
	public String From;
	public String To;
	public String CC;
	public String Subject;
	public String Message;
	
	private HashMap<String,String> _files;
	
	public void generateFileNames()
	{
		_files = new HashMap<String,String>();
		
		_files.put(Email.TO_KEY, UUID.randomUUID().toString() );
		_files.put(Email.FROM_KEY, UUID.randomUUID().toString() );
		_files.put(Email.CC_KEY, UUID.randomUUID().toString() );
	}
	
	public String getToFilename()
	{
		return _files.get(Email.TO_KEY);
	}
	
	public String getFromFilename()
	{
		return _files.get(Email.FROM_KEY);
	}
	
	public String getCcFilename()
	{
		return _files.get(Email.CC_KEY);
	}

}
