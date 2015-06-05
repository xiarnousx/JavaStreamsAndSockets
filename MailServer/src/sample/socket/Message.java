package sample.socket;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class Message 
{
	/**
	 * define the logger
	 */
	private static final Logger _logger = Logger.getLogger(Message.class.getName());
	
	private Email _email;
	
	/**
	 * Message constructor
	 * takes in argument of object email
	 * 
	 * @param email
	 */
	public Message(Email email)
	{
		_email = email;
	}
	
	/**
	 * Given emailAddress string is split into two parts email and domain
	 * 
	 * @param emailAddress
	 * @return
	 */
	private String[] getRecepientsPair(String emailAddress)
	{
		return emailAddress.split("@");
	}
	
	/**
	 * check if the email address is on this server domain
	 * 
	 * @param emailAddress
	 * @return
	 */
	private boolean isServerDomain(String emailAddress)
	{
		String[] emailParts = getRecepientsPair(emailAddress);
		
		String domain = emailParts[1].toLowerCase();
		
		return MailBox.isDomainExists(domain);
	}
	
	/**
	 * if email trageted to user on domain check if
	 * the user exists
	 * 
	 * @param emailAddress
	 * @return
	 */
	private boolean isUserExist(String emailAddress)
	{
		String[] emailParts = getRecepientsPair(emailAddress);
		String username = emailParts[0].toLowerCase();
		
		return MailBox.isUserExist(username);
	}
	
	/**
	 * Send the actual message based on email address and 
	 * target type
	 * 
	 * @param emailAddress
	 * @param type
	 * @return
	 */
	private boolean sendMessage(String emailAddress, String type)
	{
		if (isServerDomain(emailAddress))
		{
			if(isUserExist(emailAddress))
			{
				String[] emailParts = emailAddress.split("@");
				String username = emailParts[0].toLowerCase();
				try
				{
					MailBox.store(username, type, _email);
					return true;
				}
				catch(IOException ex)
				{
					_logger.error(ex.getMessage());
					return false;
				}
			}
			else
			{
				// the user does not exist
				return false;
			}
		}
		else 
		{
			// the email address is outside our server domain
			// we need to open socket to port 25 and send it
			// however for our server demo purposes we will
			// just return true
			return true;
		}
	}
	
	/**
	 * process the sending of the actual message
	 * 
	 * @return
	 */
	public boolean process()
	{
		boolean isSent = false;
		
		String to = _email.To;
		String from = _email.From;
		String cc = _email.CC;
		
		sendMessage(from, Email.FROM_KEY);
		// in our case just monitor the to address
		isSent = sendMessage(to, Email.TO_KEY);
		
		// bonus question
		if (! cc.equals("") )
		{
			sendMessage(cc, Email.CC_KEY);
		}
		
		return isSent;
	}
}
