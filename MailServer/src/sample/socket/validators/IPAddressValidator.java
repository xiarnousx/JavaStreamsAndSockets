package sample.socket.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator 
{

	/**
	 * Regex pattern to match IP addresses against
	 * 
	 */
	private static final String IP4_REGEX =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	/**
	 *  Pattern object to compile regular expression
	 */
	private Pattern pattern;
	
	
	/**
	 * Matcher object to match string against compiled regex
	 */
	private Matcher matcher;
	
	/**
	 * The ip address to validate
	 */
	private String _ip;
	
	/**
	 * Class constructor to intialize pattern object
	 */
	public IPAddressValidator(String ip)
	{
		_ip = ip;
		pattern = Pattern.compile(IP4_REGEX);
	}
	
	/**
	 * Setter
	 * 
	 * @param ip
	 */
	public void setIP(String ip)
	{
		_ip = ip;
	}
	
	/**
	 * Getter
	 * 
	 * @return _ip
	 */
	public String getIP()
	{
		return _ip;
	}
	
	
	/**
	 * Validate the ip address against compiled regex using
	 * matcher object
	 */
	public boolean isValid()
	{
		matcher = pattern.matcher(_ip);
		
		return matcher.matches();
	}
	
}
