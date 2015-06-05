package sample.socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SyncInbox implements Serializable
{

	public static String INBOX_KEY = "INBOX";
	public static String SENT_KEY = "SENT";
	
	public String username;
	public HashMap<String, ArrayList<Email> > inbox;
	
	public SyncInbox()
	{
		inbox = new HashMap<String, ArrayList<Email>>();
	}
	
	
}
