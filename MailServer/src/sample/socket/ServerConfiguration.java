package sample.socket;

import java.util.HashMap;

import org.apache.log4j.Logger;

import sample.socket.validators.IPAddressValidator;
import sample.socket.validators.PortValidator;

public class ServerConfiguration 
{
	
	/**
	 * instance of this class used for singleton
	 * 
	 */
	private static ServerConfiguration _configs = null;
	
	
	/**
	 * Define an exception message incase IP or Port number are 
	 * invalid
	 */
	private static final String INVALID_CONFIGS_MSG =
			"Invalid IP Address Or/And Hi Bound Port Number";

	/**
	 * define local constants used by configuration file
	 * to set properties and and keys
	 */
	public static final String IP_KEY = "IP";
	public static final String PORT_KEY = "PORT";
	public static final String DOMAIN_KEY = "DOMAIN";
	public static final String CON_FILE = "server.properties";

	private static final boolean IS_HI = true;
	/**
	 * define the logger
	 */
	private static final Logger _logger = Logger.getLogger(ServerConfiguration.class.getName());
	
	/**
	 * The server port to listen for incoming messages
	 * For our demo purposes the port number should be above the low portion
	 * of allowed port numbers _listen > 1023
	 */
	private int _listen;
	
	/**
	 * The IP address on which the server is sitting
	 * 
	 */
	private String _ip;
	
	/**
	 * The domain that identify the mail server
	 */
	private String _domain;
	
	
	/**
	 * ip4 address validator
	 */
	private IPAddressValidator _ip4Validator;
	
	
	/**
	 * TCP/IP port validator
	 * 
	 */
	private PortValidator _listenValidator;
	
	/**
	 * Property reader/writer class
	 */
	private ServerProperty _props;
	
	/**
	 * Default constructor
	 */
	private ServerConfiguration()
	{
		_props = new ServerProperty(CON_FILE);
	}
	
	/**
	 * overloaded constructor
	 * 
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	private ServerConfiguration(String ip, int port, String domain) throws Exception
	{
		_ip4Validator = new IPAddressValidator(ip);
		_listenValidator = new PortValidator(port);
		
		if ( _ip4Validator.isValid() && _listenValidator.isValid(IS_HI) )
		{
			_props = new ServerProperty(CON_FILE);
			_ip = ip;
			_listen = port;
			_domain = domain;
		}
		else
		{
			throw new Exception(INVALID_CONFIGS_MSG);
		}
	}
	
	/**
	 * load server configuration from property file
	 * 
	 * @param config
	 */
	private static void _loadConfiguration(ServerConfiguration config)
	{
		HashMap m = config._props.parseProperties(
				new String[] {IP_KEY,PORT_KEY,DOMAIN_KEY}
				);
		
		config._ip = m.get(IP_KEY).toString();
		config._domain = m.get(DOMAIN_KEY).toString();
		config._listen = Integer.valueOf(m.get(PORT_KEY).toString());
	}
	
	/**
	 * 
	 * Build instance of ServerConfiguration
	 * 
	 * @param ip
	 * @param port
	 * @return this
	 */
	public static ServerConfiguration getInstance(String ip, int port, String domain, boolean force) throws Exception
	{
		if (_configs == null)
		{
			if ( force )
			{
				_configs = new ServerConfiguration(ip, port, domain);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put(DOMAIN_KEY, domain);
				map.put(IP_KEY, ip);
				map.put(PORT_KEY, ""+port); // overloaded concatenate operator + to convert int to string
				_configs._props.writeProperties(map);
			}
			else
			{
				_configs = new ServerConfiguration();
				_loadConfiguration(_configs);
				
				
			}
		}
		
		
		return _configs;
			
	}
	
	/**
	 * overload getInstance
	 */
	public static ServerConfiguration getInstance() throws Exception
	{
		if ( _configs == null )
		{
			_configs = new ServerConfiguration();
			_loadConfiguration(_configs);
		}
		
		return _configs;
	}
	
	/**
	 * SETTER
	 * 
	 * @param listen
	 * @throws Exception
	 */
	public void setPort(int listen) throws Exception
	{
		_listenValidator.setPort(listen);
		
		if ( _listenValidator.isValid(IS_HI) )
		{
			_listen = listen;
			_props.writePropertyByKey(PORT_KEY, listen);
		}
		else
		{
			throw new Exception(INVALID_CONFIGS_MSG);
		}
	}
	
	/**
	 * GETTER
	 * 
	 * @param ip
	 * @throws Exception
	 */
	public void setIp (String ip) throws Exception
	{
		_ip4Validator.setIP(ip);
		
		if ( _ip4Validator.isValid() )
		{
			_ip = ip;
			_props.writePropertyByKey(IP_KEY,ip);
		}
		else 
		{
			throw new Exception(INVALID_CONFIGS_MSG);
		}
	}
	
	public void setDomain(String domain) throws Exception
	{
		_domain = domain;
		_props.writePropertyByKey(DOMAIN_KEY,domain);
	}
	
	/**
	 * GETTER
	 * 
	 * @return _listen
	 */
	public int getPort()
	{
		return _listen;
	}
	
	/**
	 * GETTER
	 * 
	 * @return
	 */
	public String getIp()
	{
		return _ip;
	}
	
	public String getDomain()
	{
		return _domain;
	}
}
