package jersey.clientapi;

import java.io.File;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.TestCase;

import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.junit.Test;

import persistence.Job;
import persistence.Status;
import util.JsonMapperUtil;
import business.EntityUpdateInfo;
import business.MegajanBusiness;
import business.SystemResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MegajanRESTfulTest extends TestCase
{
	private GlassFish glassfish;
	private GlassFishRuntime runtime;
	private String serviceUrl = "http://localhost:8080/MegajanWebService/jaxrs/system/";
	private final String TESTED_JOB_ID = "1822";
	
	@Override
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		URI baseURI =  new URI( serviceUrl );
		
		// start embedded Glassfish server
		BootstrapProperties bootstrapProperties = new BootstrapProperties();
	    bootstrapProperties.setInstallRoot( "C:\\glassfish3\\glassfish" );    
	    runtime = GlassFishRuntime.bootstrap( bootstrapProperties );
		
	    GlassFishProperties glassfishProperties = new GlassFishProperties();
	    glassfishProperties.setInstanceRoot( "C:\\glassfish3\\glassfish\\domains\\megajan" );
	    glassfish = runtime.newGlassFish( glassfishProperties );
	    
	    glassfish.start();
	    
	    // deploy web application to Glassfish
	    File war = new File( "C:\\Users\\skorbas\\git\\skorbas\\Megajan\\MegajanWebService\\target\\MegajanWebService-0.0.1-SNAPSHOT.war" );
	    Deployer deployer = glassfish.getDeployer();
	    deployer.deploy(war, "--name=simple", "--contextroot=simple", "--force=true"); // deployer.deploy(war) can be invoked instead. Other parameters are optional.
	}
	
    @Override
    protected void tearDown() throws Exception
    {
         super.tearDown();
        
         // stop embedded Glassfish 
         glassfish.dispose();
         glassfish.stop();
         runtime.shutdown();
         
    }
   
    
    @Test 
    public void testStartPage() throws Exception
    {
    	Client c = Client.create();
    	WebResource megajanServiceRes = c.resource( serviceUrl );
    	ClientResponse response = megajanServiceRes.path("start").get( ClientResponse.class );

    	assertEquals( 200, response.getStatus() );
 	    assertEquals("test ok!!!", response.getEntity( String.class ) );
 	    
 	   response.close();
    } 
    
    @Test 
    public void testAuthenticate() throws Exception
    {
	    Client c = Client.create();
	    WebResource megajanServiceRes = c.resource( serviceUrl );
	    
	    // first check valid user credentials - expected result: BIZ_RESULT_OK
	    String user    = "megajan";
	    String pasword = "megajan";
	    
	    ClientResponse response = megajanServiceRes.path("authenticate")
	    		.queryParam("user", user )
	    		.queryParam("password", pasword).get( ClientResponse.class );
	    
	    assertEquals( 200, response.getStatus() );
	    
	    SystemResponse sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    assertEquals( MegajanBusiness.BIZ_RESULT_OK, sysResponse.statusInfo.errorMsg ); 
	    
	    
	    // next check invalid user credentials - expected result: BIZ_RESULT_FAILED
	    user    = "FALSE_User";
	    pasword = "megajan";
	    
	    response = megajanServiceRes.path("authenticate")
	    		.queryParam("user", user )
	    		.queryParam("password", pasword).get( ClientResponse.class );
	    
	    assertEquals( 200, response.getStatus() );
	    
	    sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    assertEquals( MegajanBusiness.BIZ_RESULT_FAILED, sysResponse.statusInfo.errorMsg ); 
	    
	    
	    // next check invalid password credentials - expected result: BIZ_RESULT_FAILED
	    user    = "megajan";
	    pasword = "FALSE_Password";
	    
	    response = megajanServiceRes.path("authenticate")
	    		.queryParam("user", user )
	    		.queryParam("password", pasword).get( ClientResponse.class );
	    
	    assertEquals( 200, response.getStatus() );
	    
	    sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    assertEquals( MegajanBusiness.BIZ_RESULT_FAILED, sysResponse.statusInfo.errorMsg ); 
	    
	    response.close();
    } 
    
   @Test 
   public void testGetJobs() throws Exception
   {
	    Job job = getJob( TESTED_JOB_ID );
	    
	    assertEquals( TESTED_JOB_ID, job.getId() );
   }

   @Test 
   public void testGetStatuses() throws Exception
   {
	    List< Status > statusList = getStatuses();
	    assertTrue( statusList.size() > 0 );
   }
   
   
   @Test 
   public void testUpdateJobStatus() throws Exception
   {
	    // get the Job and status list first
	    Job job = getJob( TESTED_JOB_ID );
	   
	    //store old 'Status' and set new 'Status' for the job
	    List< Status > statusList = getStatuses();
	    Status newStatus = (Status)JsonMapperUtil.convertValue( statusList.get( 0 ), Status.class );
	    Status oldStatus = job.getStatus();
	    job.setStatus( newStatus );
	    
	    //update job
	    updateEntity( Job.ENTITY_QUALIFIED_NAME, job );
	    
	    //get job again and compare status
	    Job updatedJob = getJob( TESTED_JOB_ID );
	    assertEquals( job.getStatus().getId(), updatedJob.getStatus().getId() );
	    
	    //restore old status and update 'Job' again
	    job.setStatus( oldStatus );
	    updateEntity( Job.ENTITY_QUALIFIED_NAME, job );
   }
   
   private void updateEntity( String aEntityQualifiedName, Object aEntityObj )
   {
	    Client c = Client.create();
	    WebResource megajanServiceRes = c.resource( serviceUrl );
	   
	 // convert 'Entity' object to JSON and wrap it with entity qualified name into 'EntityUpdateInfo' JSON string object 
	    EntityUpdateInfo euInfo = new EntityUpdateInfo();
	    euInfo.entityQn  = aEntityQualifiedName;
	    euInfo.entityObj = aEntityObj;
	    String euInfoJsonString = new String( JsonMapperUtil.java2JsonObj( euInfo.getClass(), euInfo ) );
	        
	    //update entity
	    ClientResponse response = megajanServiceRes.path( "putJsonEntity" )
	    		.accept( MediaType.APPLICATION_JSON )
	    		.type( MediaType.APPLICATION_JSON )
	    		.entity( euInfoJsonString )
	    		.put( ClientResponse.class ) ;//, euInfoJsonString );

	    assertEquals( 200, response.getStatus() );
	    SystemResponse sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    assertEquals( MegajanBusiness.BIZ_RESULT_OK, sysResponse.statusInfo.errorMsg ); 
	    
	    response.close();
   }
   
   private Job getJob( String aJobId ) throws Exception
   {
	    Client c = Client.create();
	    WebResource megajanServiceRes = c.resource( serviceUrl );
	    
	    ClientResponse response = megajanServiceRes.path("getJobs")
	    		.queryParam("id", aJobId )
	    		.queryParam("status", "null")
	    		.queryParam("typeIntern", "null")
	    		.queryParam("serviceGroup", "null")
	    		.queryParam("loadLazy", "false").get( ClientResponse.class );
	    
	    assertEquals( 200, response.getStatus() );
	    
	    SystemResponse sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    List< Job > jobList = (List< Job >)sysResponse.dataContainer.data;
	    Job job = (Job)JsonMapperUtil.convertValue( jobList.get( 0 ), Job.class );
   
	    response.close();
	    
	    return job;
   }
	
   private List< Status > getStatuses( )
   {
	    Client c = Client.create();
	    WebResource megajanServiceRes = c.resource( serviceUrl );
	   
	    ClientResponse response = megajanServiceRes.path("getStatuses").get( ClientResponse.class );
	    SystemResponse sysResponse = (SystemResponse)JsonMapperUtil.json2JavaObject( response.getEntity(String.class), SystemResponse.class );
	    List< Status > statusList = (List< Status >)sysResponse.dataContainer.data;
	    
	    response.close();
	    
	    return statusList;
   }
}
