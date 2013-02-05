package business;


import java.io.Serializable;
import java.util.HashMap;

public class FilterExpression implements Serializable
{
	private static final long serialVersionUID = -6227697773417581287L;
	
	/**
	 * Map of conditions. Entity property name is the key to queried value.
	 */
	private HashMap< String, Object > mapConditions = new HashMap< String, Object >();
	
	/**
	 * Add condition to expression.
	 * @param aPropName
	 * @param aValueObj
	 */
	public void addCondition( String aPropName, Object aValueObj )
	{
		mapConditions.put( aPropName, aValueObj );
	}
	
	/**
	 * Returns HashMap of conditions defined in filter object. 
	 * @return
	 */
	public HashMap< String, Object > getConditions()
	{
		return mapConditions;
	}
}
