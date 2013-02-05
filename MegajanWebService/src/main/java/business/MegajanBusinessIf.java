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
	 * @return system response object containing data and detailed system respone
	 */
	public SystemResponse updateEntity( Object aEntityObj );
}
