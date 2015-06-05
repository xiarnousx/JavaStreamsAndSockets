package sample.socket.validators;

public class PortValidator 
{
	
	
	/**
	 * the port number start sequence
	 * 
	 */
	private static final int INITIAL_VAL = 1;
	
	/**
	 * The low bound port number
	 */
	private static final int LOW_BOUND_MAX = 1023;
	
	/**
	 * The Max bound port number
	 */
	private static final int HI_BOUND_MAX = 65535;
	
	
	/**
	 * The port to validate against
	 */
	private int _port;
	
	/**
	 * 
	 * class constructor
	 */
	public PortValidator(int port)
	{
		_port = port;
	}
	
	/**
	 *  GETTER
	 *  
	 * @return _port
	 */
	public int getPort()
	{
		return _port;
	}
	
	/**
	 * SETTER
	 * 
	 * @param port
	 */
	public void setPort(int port)
	{
		_port = port;
	}
	
	public boolean isValid(boolean isHi)
	{
		if (isHi && _port > LOW_BOUND_MAX && _port <= HI_BOUND_MAX)
		{
			return true;
		} 
		else if ( ! isHi && _port >= INITIAL_VAL && _port <= LOW_BOUND_MAX )
		{
			return true;
		} 
		else 
		{
			return false;
		}
		
	}

}
