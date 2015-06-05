package sample.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ServerProperty 
{
	/**
	 * define the logger
	 */
	private static final Logger _logger = Logger.getLogger(ServerProperty.class.getName());
	
	
	private String _propertyFile;
	
	public ServerProperty(String file)
	{
		_propertyFile = file;
	}
	
	/**
	 * load properties from file
	 * 
	 * @return
	 */
	private Properties loadPorperties()
	{
		Properties props = new Properties();
		InputStream ins = null;
		try
		{
			File f = new File(_propertyFile);
			ins = new FileInputStream(f);
		}
		catch(Exception e)
		{
			_logger.error( e.getMessage() );
		}
		
		try 
		{
			props.load(ins);
		}
		catch (IOException e) 
		{
			_logger.error(e.getMessage());
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
					_logger.warn(e.getMessage() );
				}
			}
		}
		
		return props;
	}
	
	/**
	 *  write properties to properties file after set call or 
	 *  get instance with force flag to true
	 * @param key
	 * @param value
	 */
	public void writePropertyByKey(String key, Object value)
	{
		Properties props = loadPorperties();
		for (String sKey : props.stringPropertyNames() )
		{
			if ( sKey.equals(key) ) 
			{
				props.setProperty(key, "" + value);
			}
			else
			{
				props.setProperty(sKey, props.getProperty(sKey));
			}
			
		}
		
		File f = new File(_propertyFile);
		OutputStream ots = null;
		try 
		{
			ots = new FileOutputStream( f );
			props.store(ots, null);
		} 
		catch (Exception e) 
		{
			_logger.error( e.getMessage() );
		}
		finally
		{
			if (ots != null)
			{
				try
				{
					ots.close();
				}
				catch(Exception e)
				{
					_logger.warn(e.getMessage());
				}
			}
		}
		
	}
	

	
	public void writeProperties(HashMap map)
	{
		Iterator it = map.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			writePropertyByKey(pair.getKey().toString(), pair.getValue().toString());
		}
	}
	
	
	/**
	 * parse properties from server.properties file
	 */
	public HashMap parseProperties(String[] keys)
	{
		final Properties props = loadPorperties();
		
		HashMap<String,String> m = new HashMap<String, String>();
		
		for (int i = 0; i < keys.length; i++)
		{
			m.put(keys[i], props.getProperty(keys[i]));
		}
		
		return m;
	}
	
	
	public String getPropertyByKey(String key)
	{
		final Properties props = loadPorperties();
		
		return props.getProperty(key);
	}

}
