package test.sample.socket;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sample.socket.ServerConfiguration;



public class ServerConfigurationTest 
{

	private ServerConfiguration _main = null;
	private String _ip;
	private int _port;
	private String _domain;
	
	@Ignore
	@Test
	public void testEmptyConstructorAndLoadedConfig()
	{
		try
		{
		
			ServerConfiguration configs = ServerConfiguration.getInstance();
			String ip = configs.getIp();
			int port = configs.getPort();
			
			assertEquals("IP Matches 127.0.0.1", "127.0.0.1", ip);
			assertEquals("Port Matcher 1123", 1123, port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Before
	public void setServerConfigurationMain()
	{
		try 
		{
			_main = ServerConfiguration.getInstance("198.168.1.254", 2225, "liu.com", true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConstructorWithForceAndOverwriteConfigs()
	{
		assertEquals("IP Matches 198.168.1.254", "198.168.1.254", _main.getIp() );
		assertEquals("Port Matcher 2225", 2225, _main.getPort() );
		assertEquals("Domain Matches liu.com","liu.com", _main.getDomain());
	}
	
	@Test
	public void testPropertiesFileAfterSetIpAndSetPort()
	{
		try
		{
			_main.setIp("192.168.0.1");
			_main.setPort(2221);
			_main.setDomain("oracle.com");
			loadProperties();
			
			assertEquals("IP Matches 192.168.0.1", "192.168.0.1", _ip);
			assertEquals("Port Matches 2221", 2221, _port);
			assertEquals("Domain Matcher oracle.com","oracle.com", _domain);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadProperties()
	{
		Properties props = new Properties();
		InputStream ins = null;
		try
		{
			File f = new File(ServerConfiguration.CON_FILE);
			ins = new FileInputStream(f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try 
		{
			props.load(ins);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (ins != null)
			{
				try
				{
					ins.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		_ip = props.getProperty(ServerConfiguration.IP_KEY);
		_port = Integer.valueOf(props.getProperty(ServerConfiguration.PORT_KEY));
		_domain = props.getProperty(ServerConfiguration.DOMAIN_KEY);
	}
}
