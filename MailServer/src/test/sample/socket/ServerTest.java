package test.sample.socket;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import sample.socket.Email;
import sample.socket.Server;
import sample.socket.SyncInbox;

public class ServerTest 
{
	private static Thread _t;
	private Socket _s;
	
	
	@BeforeClass
	public static void startServer()
	{
		_t = Server.getInstance();
		_t.start();
		try
		{
		 Thread.sleep(2000);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	
	}
	
	@AfterClass
	public static void stopServer()
	{
		_t = null;
	}
	
	@Before
	public void openSocket()
	{
		try
		{
			_s = new Socket("localhost", 2221);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	@After
	public void closeSocket()
	{
		try
		{
		 _s.close();
		 _s = null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testSendEmail()
	{
		Email email = new Email();
		email.To = "test@somenet.com";
		email.From = "test-from@somenet.com";
		email.CC = "test-cc@somenet.com";
		email.Subject = "Hello";
		email.Message = "World";
		
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try
		{
			out = new ObjectOutputStream(_s.getOutputStream());
			
			 out.writeObject(email);
			 out.flush();
			 
			in = new ObjectInputStream(_s.getInputStream());
			boolean status = false;
			status = in.readBoolean();
			
			assertEquals("shld be true", true, status);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if ( in != null ) in.close();
				if ( out != null ) out.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testSyncInbox()
	{
		ObjectInputStream oin = null;
		ObjectOutputStream out = null;
		GZIPInputStream zis = null;
		
		
		SyncInbox sync = new SyncInbox();
		sync.username = "test";
		try
		{
			out = new ObjectOutputStream(_s.getOutputStream());
			out.writeObject(sync);
			out.flush();
			
			zis = new GZIPInputStream(_s.getInputStream());
			
			oin = new ObjectInputStream(zis);
			
			Object obj = oin.readObject();
			if (obj instanceof SyncInbox)
			{
				SyncInbox mail = (SyncInbox) obj;
				// this should be changed to be read dynamically
				// or manualy depends on inbox number of files
				assertEquals("shld be 6", 6, ((ArrayList<Email>)mail.inbox.get(SyncInbox.INBOX_KEY)).size() );
				assertEquals("shld be 0", 0, ((ArrayList<Email>)mail.inbox.get(SyncInbox.SENT_KEY)).size() );
			}
			
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if ( oin != null ) oin.close();
				if ( out != null ) out.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
