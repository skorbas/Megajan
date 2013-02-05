package business;

import java.util.List;

import javax.persistence.EntityManager;
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
public class MegajanBusiness implements MegajanBusinessIf
{
	// EnititManager can be only injected to EJB beans. If this class is annotated e.g. as @Stateless session bean
	// then GlassFish is not loading it because of security policy. EntityManager is injected in main web service class which 
	// can annotated as a EJB bean and loaded to Glassfish.
	//@PersistenceContext( unitName="MegajanWebService" )
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
    public MegajanBusiness( EntityManager aEntityManager )
    {
    	 entityManager = aEntityManager;
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
			response.statusInfo.errorMsg = "OK";
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
	public SystemResponse updateEntity( Object aEntityObj ) 
	{
		SystemResponse response = new SystemResponse();
		try 
		{
			//entityManager.getTransaction().begin();
			Object updatedObject = entityManager.merge( aEntityObj );
			//entityManager.getTransaction().commit();
		
			response.dataContainer.data = updatedObject;
			response.statusInfo.errorMsg = "OK";
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
}
