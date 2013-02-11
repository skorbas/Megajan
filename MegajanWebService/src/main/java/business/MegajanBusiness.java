package business;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import jpautil.JpqlQueryCreator;
import persistence.Attachment;
import persistence.Job;
import persistence.Manufacturer;
import persistence.User;
import util.ExceptionUtil;

/**
 * Session Bean implementation class System
 */
// Tell the container that this class is container managed
// A reference to a managed bean can be obtained through resource injection
@ManagedBean   
public class MegajanBusiness implements MegajanBusinessIf
{
	@PersistenceContext( unitName="MegajanWebService" )
	private EntityManager entityManager;
	
	public static String BIZ_RESULT_OK = "OK";

    public MegajanBusiness(  )
    {
    }
	
	/**
	 * Default constructor.
	 */
    public MegajanBusiness( EntityManager aEntityManager )
    {
    	 entityManager = aEntityManager;
    }
    
    @Override
    public SystemResponse authenticate( String aUserName, String aPassword )
    {
    	SystemResponse resp = new SystemResponse();
    	
    	FilterExpression filter = new FilterExpression();
    	filter.addCondition("name", aUserName );
    	filter.addCondition("password", md5( aPassword ) ); // converts password to MD5 
    	
    	SystemResponse response = getEntities( User.ENTITY_QUALIFIED_NAME, filter, false ); 
    	
    	if( response.dataContainer.data != null && 
    		response.statusInfo.errorMsg.equals( BIZ_RESULT_OK ) )
    	{
    		List<User> users = (List<User>)response.dataContainer.data;
    		if( users.size() > 0 )
    		{
    			// user exists so return its ID to the client (needed for further business calls)
    			response.dataContainer.data = users.get( 0 ).getId();
    		}
    	}
    	else if( response.statusInfo.errorMsg.equals( BIZ_RESULT_OK ) )
    	{
    		resp.dataContainer.data = "Ivalid user or password. Authentication failed.";
    	}
    	
    	return response;
    }
 
	@Override
	public SystemResponse getEntities( String aEntityQn, FilterExpression aFilter, boolean loadLazyFields ) 
	{
		SystemResponse response = new SystemResponse();
		
		try
		{
			List<?>  entityList = queryData( aEntityQn, aFilter, loadLazyFields );
			if( entityList != null )
			{
				if( !loadLazyFields )
				{
					clearLazyFiledsForJson( entityList, aEntityQn );
				}
			}
			response.dataContainer.data = entityList;
			response.statusInfo.errorMsg = BIZ_RESULT_OK;
		}
		catch( Exception ex )
		{
			// in case of error save stack trace in response object to make it diagnosable on client site
			response.dataContainer.data = null;
			response.statusInfo.errorMsg = ExceptionUtil.getStackTraceAsString( ex );
		}
		return response;
	}

	private List<?> queryData(String aEntityQn, FilterExpression aFilter, boolean loadLazyFields) 
	{
		//execution of criteria query
		JpqlQueryCreator queryCreator = new JpqlQueryCreator( entityManager ); 
		CriteriaQuery<?> criteriaQuery = queryCreator.create( aEntityQn, aFilter );
		 
		List<?> entityList = entityManager.createQuery( criteriaQuery ).getResultList();

		// Init lazy loaded fields in case there is only one entity in list
		// Otherwise lazy loaded fields will be not available on client side - on client side
		// the entity object is detached from the entityManager.
		if( loadLazyFields )
		{
			for( Object entity : entityList )
			{
				if( aEntityQn.equals( Job.ENTITY_QUALIFIED_NAME ) )
				{
					Job job = (Job)entity;
					job.getProducts().size();
					job.getServices().size();
				//	job.getAttachments().size();
				}
				else if( aEntityQn.equals( Attachment.ENTITY_QUALIFIED_NAME ) )
				{
					Attachment attachment = (Attachment)entity;
					attachment.getContent();
				}
				else if( aEntityQn.equals( User.ENTITY_QUALIFIED_NAME ) )
				{
					User user = (User)entity;
					user.getBrands();
				}
				else if( aEntityQn.equals( Manufacturer.ENTITY_QUALIFIED_NAME ) )
				{
					Manufacturer man = (Manufacturer)entity;
					man.getBrands();
				}
			}
		}
		
		return entityList;
	}
	
	
	@Override
	public void createEntity( String aEntityQn, Object aEntityObj ) 
	{
		try 
		{
			//entityManager.getTransaction().begin();
			
			// make the instance managed and persistent
			entityManager.persist( aEntityObj );
			
			//entityManager.flush();
			//entityManager.getTransaction().commit();
		}
		catch( Exception e ) 
		{
			e.getCause();
		}
	}

	@Override
	@TransactionAttribute( TransactionAttributeType.REQUIRED ) // transaction declaration when JTA transaction type is used in persistence.xml
	public SystemResponse updateEntity( Object aEntityObj ) 
	{
		SystemResponse response = new SystemResponse();
		try 
		{
			//entityManager.getTransaction().begin(); // should be used when RESOURCE_LOCAL transaction type is used in persistence.xml
			Object updatedObject = entityManager.merge( aEntityObj );
			//entityManager.getTransaction().commit();
		
			response.dataContainer.data = updatedObject;
			response.statusInfo.errorMsg = BIZ_RESULT_OK;
		}
		catch( Exception ex )
		{
			// in case of error save stack trace in response object to make it diagnosable on client site
			response.dataContainer.data = null;
			response.statusInfo.errorMsg = ExceptionUtil.getStackTraceAsString( ex );
			ex.printStackTrace();
		}	
		
		return response;
	}
	
	
	private void clearLazyFiledsForJson( List<?> entityList, String aEntityQn )
	{
		// Detach entities from entity manager first.
		// This is because Jackson JSON always serializes LAZY fields regardless of FetchType.LAZY annotation from JPA.
		//entityManager.clear();
		for( Object entity : entityList )
		{
			//entityManager.detach( entity );
			if( aEntityQn.equals( Job.ENTITY_QUALIFIED_NAME ) )
			{
				Job job = (Job)entity;
				if( job.getProducts() != null )
					job.getProducts().clear();
				if( job.getServices() != null )
					job.getServices().clear();
			//	if( job.getAttachments() != null )
			//		job.getAttachments().clear();
			}
			else if( aEntityQn.equals( Attachment.ENTITY_QUALIFIED_NAME ) )
			{
				Attachment attachment = (Attachment)entity;
				if( attachment.getContent() != null )
					attachment.setContent( null );
			}
			else if( aEntityQn.equals( User.ENTITY_QUALIFIED_NAME ) )
			{
				User user = (User)entity;
				if( user.getBrands() != null )
					user.getBrands().clear();
			}
			else if( aEntityQn.equals( Manufacturer.ENTITY_QUALIFIED_NAME ) )
			{
				Manufacturer man = (Manufacturer)entity;
				if( man.getBrands() != null )
					man.getBrands().clear();
			}
		}
	}
	
	/**
	 * Converts password to MD5 representation.
	 * @param aPassword
	 * @return
	 */
	private String md5( String aPassword )
	{
        String md5Pasword = null;

        try
        {    
	        //Create MessageDigest object for MD5
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	         
	        //Update input string in message digest
	        digest.update( aPassword.getBytes(), 0, aPassword.length());
	 
	        //Converts message digest value in base 16 (hex)
	        md5Pasword = new BigInteger(1, digest.digest()).toString(16);
 
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return md5Pasword;
    }
}
