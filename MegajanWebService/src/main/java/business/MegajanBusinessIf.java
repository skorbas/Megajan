package business;





/**
 * Main interface of for the server application.
 * @author skorbas
 *
 */
public interface MegajanBusinessIf
{
	/**
	 * Returns entities list identified by entity qualified name and filtered by  filterExpression.
	 * @param aEntityQn
	 * @param aFilter
	 * @param loadLazyFields
	 * @return
	 */
	public SystemResponse getEntities( String aEntityQn, FilterExpression aFilter, boolean loadLazyFields );
	
	/**
	 * Creates new entity in database.
	 * @param aEntityQn
	 * @param aEntityObj
	 */
	public void createEntity( String aEntityQn, Object aEntityObj );
	
	/**
	 * Updates existing entity in database.
	 * @param aEntityQn
	 * @param aEntityObj
	 * @return system response object containing data and detailed system response.
	 */
	public SystemResponse updateEntity( Object aEntityObj );
	
	
	/**
	 * Checks if user/password exists within system and then return valid user ID for further use by the client application.
	 * User ID is needed to call other services from MegajanBusiness.
	 * @param aUserName
	 * @param aPassword
	 * @return
	 */
	public SystemResponse authenticate( String aUserName, String aPassword );
}
