package embedded;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.Job;
import persistence.Product;
import business.FilterExpression;
import business.MegajanBusiness;
import business.MegajanBusinessIf;
import business.SystemResponse;


/**
 * Perform unit tests using GLassfish embedded container.
 * @author skorbas
 *
 */
public class SystemTestEmbedded extends TestCase
{
	private EJBContainer ejbContainer;
	private EntityManager entityManager;
	private MegajanBusinessIf system;
	
	

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
		
		// Using the EJB 3.1 Embeddable API with Embedded GlassFish Server for Unit-Testing
		
		/*
		To specify GlassFish Server as the Container Provider, include glassfish-embedded-static-shell.jar in the class path of 
		your embeddable EJB application. Reference the glassfish-embedded-static-shell.jar file from the as-install/glassfish/lib/embedded 
		directory of a GlassFish Server installation. Do not move this file or it will not work.
		 */
        final Properties props = new Properties();    
        props.put( "org.glassfish.ejb.embedded.glassfish.installation.root" , "C:\\glassfish3\\glassfish" );
        props.put( "org.glassfish.ejb.embedded.glassfish.instance.root"     , "C:\\glassfish3\\glassfish\\domains\\megajan" );
        props.put( "org.glassfish.ejb.embedded.glassfish.configuration.file", "C:\\glassfish3\\glassfish\\domains\\megajan\\config\\domain.xml");		
        
        
		ejbContainer = EJBContainer.createEJBContainer( props );
		final Context context = ejbContainer.getContext();
		
		//EmProviderIf provider = (EmProviderIf)context.lookup( "test.EmProviderIf" );
		//entityManager = provider.getEntityManager();

		system = new MegajanBusiness( entityManager );
		
	}

	@After
	public void tearDown() throws Exception 
	{
		// close EJB container after test
		ejbContainer.close();
		
		super.tearDown();
	}

	@Test
	public void testJobCRUD()
	{
		// Read Job entity from the system
		FilterExpression filter = new FilterExpression();
		filter.addCondition( "id", "1822" );
        List<?> jobList = getEntityList( filter, Job.ENTITY_QUALIFIED_NAME, true );
        
		assertEquals( 1, jobList.size() );
		
		Job job = (Job)jobList.get( 0 );
		assertEquals( "1822", job.getId() );
		
		// Modify product list in Job entity
		List<?> availableProductList = getEntityList( new FilterExpression(), Product.ENTITY_QUALIFIED_NAME, false );
		job.getProducts().clear();
		Product newProduct = (Product)availableProductList.get( 0 );
		job.getProducts().add( newProduct );
		
		// Update modified Job in DB
		system.updateEntity( job );
		
		// Read again modified job from the system
		List<?> newJobList = getEntityList( filter, Job.ENTITY_QUALIFIED_NAME, false );
	
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
		SystemResponse result = system.getEntities( aEntityQn, aFilter, loadLazy );
		
		System.out.print( result.statusInfo.errorMsg );
		
		assertNotNull( result );
		assertNotNull( result.dataContainer );
		assertNotNull( result.dataContainer.data );
		
		return (List<?>)result.dataContainer.data;
	}

}
