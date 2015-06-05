package test.sample.socket;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import sample.socket.Email;
import sample.socket.MailBox;
import sample.socket.ServerConfiguration;
import sample.socket.ServerProperty;
import sample.socket.SyncInbox;

public class MailBoxTest 
{

	@Test
	public void testDomainDirectoryExists()
	{
		MailBox.createDomain();
		
		ServerProperty props = new ServerProperty(ServerConfiguration.CON_FILE);
		
		String dir = MailBox.ROOT + File.separator + 
				props.getPropertyByKey(ServerConfiguration.DOMAIN_KEY);
		
		File domain = new File(dir);
		assertEquals("Should be boolean true", true,domain.exists());
	}
	
	
	@Test
	public void testUserDirectoryExists()
	{
		MailBox.createUser("TEST");
		
		ServerProperty props = new ServerProperty(ServerConfiguration.CON_FILE);
		
		String dir = MailBox.ROOT + File.separator + 
				props.getPropertyByKey(ServerConfiguration.DOMAIN_KEY)
				+ File.separator + "test";
		
		File mailboxUser = new File(dir);
		assertEquals("Should be boolean true", true, mailboxUser.exists());
	}
	
	@Test
	public void testPaths()
	{
		MailBox.createDomain();
		MailBox.createUser("Test");
		
		String inbox = MailBox.getInbox("test");
		String sent = MailBox.getSent("test");
		
		File inboxDir = new File(inbox);
		File sentDir = new File(sent);
		
		assertEquals("Inbox shld ret true", true, inboxDir.exists());
		assertEquals("Sent shld ret true", true, sentDir.exists());
		
	}
	
	@Test
	public void testStore()
	{
		Email email = new Email();
		email.To = "test@oracle.com";
		email.Subject = "Hello";
		email.Message = "World";
		email.generateFileNames();
		try
		{
			MailBox.store("test", Email.TO_KEY, email);
			String path = MailBox.getInbox("test");
			FileInputStream fin = new FileInputStream(path + File.separator + email.getToFilename());
			ObjectInputStream oin = new ObjectInputStream(fin);
			Email emailRead = (Email) oin.readObject();
			 if (oin != null)
			 {
				 oin.close();
			 }
			assertEquals("email shld be test@oracle.com", "test@somenet.com", emailRead.To);
			assertEquals("subject shld be Hello", "Hello", emailRead.Subject);
			assertEquals("message shld be World", "World", emailRead.Message);
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMailBoxEmails()
	{
		try
		{
			HashMap<String,ArrayList<Email>> map = MailBox.readMailBox("test");
		
			ArrayList<Email> inboxEmails = map.get(SyncInbox.INBOX_KEY);
		
			assertEquals("shld be 2", 4, inboxEmails.size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
