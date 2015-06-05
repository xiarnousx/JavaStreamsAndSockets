package test.sample.socket;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.Test;

import sample.socket.Email;
import sample.socket.MailBox;
import sample.socket.Message;
public class MessageTest 
{
	private Email loadEmail(String filename, String username, String type)
	{
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		Email email = null;
		
		try
		{
			String path;
			
			if (type.equals(Email.TO_KEY) || type.equals(Email.CC_KEY) )
			{
			   path = MailBox.getInbox(username);
			}
			else 
			{
				path = MailBox.getSent(username);
			}
			
			fin = new FileInputStream( path + File.separator + filename );
			oin = new ObjectInputStream(fin);
			email = (Email) oin.readObject();
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (oin != null)
			{
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return email;
	}

	@Test
	public void testSend()
	{
		Email e = new Email();
		e.To = "test@somenet.com";
		e.From = "test-from@somenet.com";
		e.CC = "test-cc@somenet.com";
		e.Subject = "Hello";
		e.Message = "World!";
		
		Message m = new Message(e);
		m.process();
		
		Email toEmail = loadEmail(e.getToFilename(), "test", Email.TO_KEY);
		Email fromEmail = loadEmail(e.getFromFilename(), "test-from", Email.FROM_KEY);
		Email ccEmail = loadEmail(e.getCcFilename(), "test-cc", Email.CC_KEY);
		
		assertEquals("Shld be Hello", "Hello", toEmail.Subject);
		assertEquals("Shld be World!", "World!", toEmail.Message);
		
		assertEquals("Shld be Hello", "Hello", fromEmail.Subject);
		assertEquals("Shld be World!", "World!", fromEmail.Message);
		
		assertEquals("Shld be Hello", "Hello", ccEmail.Subject);
		assertEquals("Shld be World!", "World!", ccEmail.Message);
	}
	
}
