package sample.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MailBox 
{
	public static final String ROOT = "MailBox";
	public static final String INBOX = "inbox";
	public static final String SENT = "sent";
	
	/**
	 * Create directory structure inside ROOT folder
	 * domain and username
	 * 
	 * @param dir
	 * @return boolean
	 * @throws SecurityException
	 */
	private static boolean createStructure(String dir) throws SecurityException
	{
		File structure = new File(MailBox.ROOT + File.separator + dir );
		
		if ( ! structure.exists() )
		{
			structure.mkdir();
			return true;
		}
		
		return false;
	}
	
	/**
	 * read the domain on which the server is sitting and receiving emails
	 * from server.properties file 
	 * 
	 * @return String
	 */
	public static String getDomain()
	{
		ServerProperty props = new ServerProperty(ServerConfiguration.CON_FILE);
		
		return props.getPropertyByKey(ServerConfiguration.DOMAIN_KEY);
		
	}
	
	/**
	 * Get the inbox directory inside the username directory
	 * inside the domain directory
	 * 
	 * @param username
	 * @return path
	 */
	public static String getInbox(String username)
	{
		String path = MailBox.ROOT + File.separator + getDomain() + File.separator +
				username.toLowerCase() + File.separator + MailBox.INBOX;
		
		File dir = new File(path);
		
		if (dir.exists())
		{
			return path;
		}
		
		return null;
	}
	
	/**
	 * read the sent directory which is inside the username directory
	 * that's inside the domain directory
	 * 
	 * @param username
	 * @return
	 */
	public static String getSent(String username)
	{
		String path = MailBox.ROOT + File.separator + getDomain() + File.separator +
				username.toLowerCase() + File.separator + MailBox.SENT;
		
		File dir = new File(path);
		
		if (dir.exists())
		{
			return path;
		}
		
		return null;
	}
	
	/**
	 * check if the username directory exists inside the domain directory
	 * 
	 * @param username
	 * @return boolean
	 */
	public static boolean isUserExist(String username)
	{
		String dir = MailBox.ROOT + File.separator + getDomain() + File.separator + username.toLowerCase();
		File home = new File(dir);
		return  home.exists();
	}
	
	/**
	 * check if the domain exists given string domain name
	 * 
	 * @param domain
	 * @return
	 */
	public static boolean isDomainExists(String domain)
	{
		String dir = MailBox.ROOT + File.separator + domain;
		
		File domainDir = new File(dir);
		return  domainDir.exists();
	}
	
	/**
	 * Create the domain directory inside the MailBox.ROOT directory
	 * 
	 */
	public static void createDomain()
	{
		createStructure( getDomain() );
	}
	
	/**
	 * Create the username directory given username inside the domain directory which
	 * is read from server.properties inside the MailBox.ROOT directory
	 * 
	 * @param username
	 */
	public static void createUser(String username)
	{
		String dir = getDomain() + File.separator + username.toLowerCase();
		boolean isCreated = createStructure(dir);
		
		if ( isCreated )
		{
			String inbox = dir + File.separator + MailBox.INBOX;
			String sent = dir + File.separator + MailBox.SENT;
			
			createStructure(inbox);
			createStructure(sent);
		}
	}
	
	/**
	 * Store the serializable object ser into a given username directory under server
	 * domain inside inbox or sent directory
	 * 
	 * @param username
	 * @param isSent
	 * @param ser
	 * @throws IOException
	 */
	public static void store(String username, String type, Email ser) 
			throws IOException
	{
		
		String path = null;
		
		if ( type.equals(Email.FROM_KEY) )
		{
			path = MailBox.getSent(username) + File.separator + ser.getFromFilename();
		}
		else if ( type.equals(Email.TO_KEY) )
		{
			path = MailBox.getInbox(username) + File.separator + ser.getToFilename();
		}
		else if ( type.equals(Email.CC_KEY) )
		{
			path = MailBox.getInbox(username) + File.separator + ser.getCcFilename();
		}
		
		if (path != null)
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(ser);
			if (out != null)
			{
				out.close();
			}
		}
		
	}
	
	public static HashMap<String,ArrayList<Email>> readMailBox(String username)
			throws Exception
	{
		HashMap<String,ArrayList<Email>> map = new HashMap<String, ArrayList<Email>>();
		String inboxPath = getInbox(username);
		String sentPath = getSent(username);
		
		final File inboxDir = new File(inboxPath);
		final File sentDir = new File(sentPath);
		
		ArrayList<Email> inbox = new ArrayList<Email>();
		ArrayList<Email> sent = new ArrayList<Email>();
		
		for (final File f : inboxDir.listFiles() )
		{
			
			ObjectInputStream oin = new ObjectInputStream(
						new FileInputStream( f.getAbsolutePath() )
					);
			
			Object inboxEmail = oin.readObject();
			
			if (inboxEmail instanceof Email)
			{
				inbox.add((Email) inboxEmail);
			}
			
			if (oin != null ) oin.close();
		}
		
		for (final File f : sentDir.listFiles() )
		{
			ObjectInputStream oin = new ObjectInputStream(
						new FileInputStream(f.getAbsolutePath())
					);
			Object sentEmail = oin.readObject();
			
			if (sentEmail instanceof Email)
			{
				sent.add((Email) sentEmail);
			}
			
			if (oin != null ) oin.close();
		}
		
		map.put(SyncInbox.INBOX_KEY, inbox);
		map.put(SyncInbox.SENT_KEY, sent);
		
		return map;
	}
}
