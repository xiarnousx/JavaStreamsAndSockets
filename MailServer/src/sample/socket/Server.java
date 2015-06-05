package sample.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

public class Server extends Thread
{
	/**
	 * define the logger
	 */
	private static final Logger _logger = Logger.getLogger(Server.class.getName());
	
	private int _port;
	private String _ip;
	private static Server _server;
	private ServerSocket _ss;
	
	private Server()
	{
		try
		{
			ServerConfiguration config = ServerConfiguration.getInstance();
			_ip = config.getIp();
			_port = config.getPort();
			_ss = new ServerSocket(_port);
			// this flag is set to true in order for the os to reuse the port
			// helpful when we want to shutdown the server and then restart it
			_ss.setReuseAddress(true);
		}
		catch (Exception ex)
		{
			_logger.error( ex.getMessage() );
		}
		
	}
	
	public static Server getInstance()
	{
		if ( _server == null)
		{
			_server = new Server();
		}
		
		return _server;
	}
	
	public void closeServer()
	{
		try 
		{
			_ss.close();
			_server = null;
		} 
		catch (IOException e) 
		{
			_logger.error(e.getMessage());
		}
	}

	@Override
	public void run()
	{
		Socket connection = null;
		ObjectOutputStream out = null;
		GZIPOutputStream zus = null;
		ByteArrayOutputStream bos = null;
		ObjectInputStream in = null;
		
		while (_ss.isBound() && ! _ss.isClosed()) {
			try {

				// make the server socket available for listening
				connection = _ss.accept();
				// get the input stream of the server socket
				in = new ObjectInputStream(connection.getInputStream());
				// read object of the inputStream -- socket
				Object obj = in.readObject();
				
				// check object type using instanceof and apply
				// logic depending on object type
				if (obj instanceof Email) 
				{
					// build the incoming email obj
					Email incomingEmail = (Email) obj;
					// generate unique file names base on email
					incomingEmail.generateFileNames();
					// create new message object
					Message message = new Message(incomingEmail);
					// create serversocket output stream
					out = new ObjectOutputStream(connection.getOutputStream());
					// write the status of message.process() into output stream
					out.writeBoolean(message.process());
					// flush the stream
					out.flush();
				} 
				else if (obj instanceof SyncInbox) 
				{
					// build the email object
					SyncInbox syncEmails = (SyncInbox) obj;
					// get the sent/inbox emails into hashmap
					HashMap<String,ArrayList<Email>> mailBox = MailBox.readMailBox(syncEmails.username);
					// assign the mailbox to syncEmails.inbox
					syncEmails.inbox = mailBox;
					
					// below writes the synEmail into output stream comptessed
					
					// get the bytes for syncEmail
					bos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(bos);
					// write the serialized object into array byte output stream
					out.writeObject(syncEmails);
					// get bytes into array
					byte[] syncEmailsBytes = bos.toByteArray();
					
					// get hold of the socket output stream
					zus = new GZIPOutputStream(connection.getOutputStream());
					// write to connection output stream
					zus.write(syncEmailsBytes, 0, syncEmailsBytes.length );
					//call finish method on GZipOutputStream
					zus.finish();
					zus.flush();
					
				}

			} 
			catch (Exception ex) 
			{
				_logger.error(ex.getMessage());
			} 
			finally 
			{
				if (connection != null)
				{
					try 
					{
						// closes the socket channels implicitly 
						connection.close();
					} catch (Exception ex) {
						_logger.error(ex.getMessage());
					}
				}
			}
		}
		
	}
}
