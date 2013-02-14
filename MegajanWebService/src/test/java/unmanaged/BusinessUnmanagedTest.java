package unmanaged;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.Job;
import persistence.Product;
import util.MyEm;
import business.FilterExpression;
import business.MegajanBusiness;
import business.MegajanBusinessIf;
import business.SystemResponse;

/**
 * Perform unit tests using "unamanaged" EntityManager. This kind of unit test is faster than those launched in embedded container.
 * Since the EntityManager runs outside the container, the transactions can be managed only by the unit tests.
 * In order to perform this test another persistence-unit ('TestUnmanaged') has been created in persistence.xml file.
 * This persistence unit is without <jta-data-source> and uses transaction type RESOURCE_LOCAL.
 * 
 * @author skorbas
 *
 */
public class BusinessUnmanagedTest extends TestCase
{
	public final String PERSISTENCE_UNIT = "TestUnmanaged";
	
	/**
	 * @MyEm annotation is used to differentiate how EntityManager should be injected. see class 'EmProducer'. 
	 */
	@Inject @MyEm
	private EntityManager entityManager;
	
	private MegajanBusinessIf megajanBiz;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		super.setUp();

		//EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		//entityManager = emFactory.createEntityManager();
		megajanBiz = new MegajanBusiness( entityManager );
	}

	@After
	public void tearDown() throws Exception 
	{
		super.tearDown();
	}

	@Test
	public void testJobCRUD()
	{
		boolean withLazyFields = true;
		boolean noLazyFields = false;
		
		// Read Job entity from the system
		FilterExpression filter = new FilterExpression();
		filter.addCondition( "id", "1822" );
        List<?> jobList = getEntityList( filter, Job.ENTITY_QUALIFIED_NAME, withLazyFields );
        
		assertEquals( 1, jobList.size() );
		
		Job job = (Job)jobList.get( 0 );
		assertEquals( "1822", job.getId() );
		
		// Modify product list in Job entity
		List<?> availableProductList = getEntityList( new FilterExpression(), Product.ENTITY_QUALIFIED_NAME, noLazyFields );
		job.getProducts().clear();
		Product newProduct = (Product)availableProductList.get( 0 );
		job.getProducts().add( newProduct );
		
		// Update modified Job in DB
		megajanBiz.updateEntity( job );
		
		// Read again modified job from the system
		List<?> newJobList = getEntityList( filter, Job.ENTITY_QUALIFIED_NAME, withLazyFields );
	
		// Compare changed data
		Job jobUpdated = (Job)newJobList.get( 0 );
		
		// compare products
		assertEquals( jobUpdated.getProducts().get(0).getId(), job.getProducts().get(0).getId()  );
		
		// compare service group
		if( jobUpdated.getServiceGroupUser() != null && job.getServiceGroupUser() != null )
		{
			assertEquals( jobUpdated.getServiceGroupUser().getId(), job.getServiceGroupUser().getId()  );
		}
		
		// dates
		assertEquals( jobUpdated.getDateCreate()      , job.getDateCreate()        );
		assertEquals( jobUpdated.getDateDeadline()    , job.getDateDeadline()      );
		assertEquals( jobUpdated.getDateDsContact()   , job.getDateDsContact()     );
		assertEquals( jobUpdated.getDateModify()      , job.getDateModify()        );
		assertEquals( jobUpdated.getDateRealize()     , job.getDateRealize()       );
		assertEquals( jobUpdated.getDateSrContact()   , job.getDateSrContact()     );
		assertEquals( jobUpdated.getDateSrSend()      , job.getDateSrSend()        );
		assertEquals( jobUpdated.getDateSrVisit()     , job.getDateSrVisit()       );
		
		// client data
		assertEquals( jobUpdated.getClCity()          , job.getClCity()            );
		assertEquals( jobUpdated.getClEmail()         , job.getClEmail()           );
		assertEquals( jobUpdated.getClientNumber()    , job.getClientNumber()      );
		assertEquals( jobUpdated.getClName()          , job.getClName()            );
		assertEquals( jobUpdated.getClPhone()         , job.getClPhone()           );
		assertEquals( jobUpdated.getClPostCode()      , job.getClPostCode()        );
		assertEquals( jobUpdated.getClPostCode()      , job.getClPostCode()        );
		assertEquals( jobUpdated.getClRegion()        , job.getClRegion()          );
		assertEquals( jobUpdated.getClStreet()        , job.getClStreet()          );	
		assertEquals( jobUpdated.getClSurname()       , job.getClSurname()         );
		
		// job primary data (simple types)
		assertEquals( jobUpdated.getId()	          , job.getId()                );
		assertEquals( jobUpdated.getBrandName()       , job.getBrandName()         );
		assertEquals( jobUpdated.getDamage()          , job.getDamage()            );
		assertEquals( jobUpdated.getDescription()     , job.getDescription()       );
		assertEquals( jobUpdated.getInfoClient()      , job.getInfoClient()        );
		assertEquals( jobUpdated.getInfoService()     , job.getInfoService()       );
		assertEquals( jobUpdated.getLastUserName()    , job.getLastUserName()      );
		assertEquals( jobUpdated.getManufacturerName(), job.getManufacturerName()  );
		assertEquals( jobUpdated.getReclamationId()   , job.getReclamationId()     );
		assertEquals( jobUpdated.getFlagAdmApproved() , job.getFlagAdmApproved()   );
		assertEquals( jobUpdated.getNextJobId()       , job.getNextJobId()         );
		assertEquals( jobUpdated.getPreviousJobId()   , job.getPreviousJobId()     );
		assertEquals( jobUpdated.getPrice()           , job.getPrice()             );
		assertEquals( jobUpdated.getRandClNo()        , job.getRandClNo()          );
		assertEquals( jobUpdated.getServices()		  , job.getServices()          );
		
		// job primary data (complex types) - compare using Id
		assertEquals( jobUpdated.getStatus().getId()	  , job.getStatus().getId()            );
		assertEquals( jobUpdated.getType().getId()        , job.getType().getId()              );
		assertEquals( jobUpdated.getTypeGeneral().getId() , job.getTypeGeneral().getId()       );
		assertEquals( jobUpdated.getTypeIntern().getId()  , job.getTypeIntern().getId()        );
		
	} 
	
	private List<?> getEntityList( FilterExpression aFilter, String aEntityQn, boolean loadLazy )
	{
		SystemResponse result = megajanBiz.getEntities( aEntityQn, aFilter, loadLazy );
		
		System.out.print( result.statusInfo.errorMsg );
		
		assertNotNull( result );
		assertNotNull( result.dataContainer );
		assertNotNull( result.dataContainer.data );
		
		return (List<?>)result.dataContainer.data;
	}

}
