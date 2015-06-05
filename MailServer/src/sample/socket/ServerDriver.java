package sample.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class ServerDriver 
{
	private static boolean _isLoaded = false;
	private static Thread _t;
	
    public static void main( String[] args )
    {
    	int ctrl = 0;
        do{
        	printMenu();
        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        	int option = 0;
			try 
			{
				option = Integer.valueOf( bufferedReader.readLine().trim() );
			} 
			catch (NumberFormatException | IOException e) 
			{
				e.printStackTrace();
			}
        	
        	switch (option)
        	{
	        	case 0:
	        			ctrl = -1;
	        			shutdownServer();
	        		break;
	        	case 1 :
	        			loadAndStart();
	        		break;
	        	case 2:
	        			createMailBox();
	        		break;
	        	case 3:
	        			shutdownServer();
	        		break;
        	
        	}
        	
        } while(ctrl == 0);
        
        System.out.println("Goodbye!");
    }
    
    private static void printMenu()
    {
    	System.out.println("*************************************");
    	System.out.println("*************************************");
    	System.out.println("Enter Options :");
    	if ( ! _isLoaded )
    	{
    		System.out.println("Enter 1 to start server from configuration");
    	}
    	
    	System.out.println("Enter 2 to create MailBox");
    	
    	if ( _isLoaded )
    	{
    		System.out.println("Enter 3 to shutdown server");
    		
    	}
    	
    	System.out.println("Enter 0 to Exit application and close server");
    }
    
    private static void loadAndStart()
    {
    	try
    	{
    		ServerConfiguration configs = ServerConfiguration.getInstance();
    		System.out.println("Configuration Loaded: ");
    		System.out.println("Domain : " + configs.getDomain() );
    		System.out.println("IP : " + configs.getIp());
    		System.out.println("Port : " + configs.getPort());
    		System.out.println("Starting Server on Port " + configs.getPort() + "....");
    		_t = Server.getInstance();
    		_t.start();
    		_isLoaded = true;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }
    
    private static void shutdownServer()
    {
    	System.out.println("Shutting down server ...");
    	Server s = Server.getInstance();
    	s.closeServer();
    	_t = null;
    	_isLoaded = false;
    }
    
    private static void createMailBox()
    {
    	try
    	{
    		System.out.println("*************************************");
    		ServerConfiguration configs = ServerConfiguration.getInstance();
    		System.out.println("Enter username for domain : " + configs.getDomain() );
    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    		String username = bufferedReader.readLine().trim();
    		MailBox.createUser(username);
    		System.out.println(username + "@" + configs.getDomain() + " Created Successfully");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
