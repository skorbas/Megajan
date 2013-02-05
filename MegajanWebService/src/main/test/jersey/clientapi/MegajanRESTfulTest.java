package jersey.clientapi;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.TestCase;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.glassfish.embeddable.GlassFish;
import org.junit.Test;

import persistence.Job;
import persistence.Status;
import util.JsonMapperUtil;
import business.EntityUpdateInfo;
import business.SystemResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MegajanRESTfulTest extends TestCase
{
	private GlassFish glassfish;
	private String serviceUrl = "http://localhost:8080/MegajanWebService/jaxrs/system/";
	
	private final String TESTED_JOB_ID = "1822";
	
	@Override
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		URI baseURI =  new URI( serviceUrl );
		
        // Start Glassfish
//        glassfish = new GlassFish( baseURI.getPort());
//        
//        // Deploy Glassfish referencing the web.xml
//        ScatteredWar war = new ScatteredWar(BASE_URI.getRawPath(), new File("src/main/webapp"),
//            new File("src/main/webapp/WEB-INF/web.xml"),
//            Collections.singleton( new File("target/classes").toURI().toURL()));
//        
//        glassfish.deploy(war);
//        
	}
	
    @Override
   protected void tearDown() throws Exception
   {
        super.tearDown();
        //glassfish.stop();
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
	        
//	    String jsonJob = new String( JsonMapperUtil.java2JsonObj( aEntityObj.getClass(), aEntityObj ) );
//	    ObjectNode objNode = new ObjectNode( JsonNodeFactory.instance );
//	    objNode.put( "entityQn", aEntityQualifiedName );
//	    objNode.put( "entityObj",  jsonJob );
//	    String jsonStringData = objNode.toString();
	     		
	    //update entity
	    ClientResponse response = megajanServiceRes.path( "putJsonEntity" )
	    		.accept( MediaType.APPLICATION_JSON )
	    		.type( MediaType.APPLICATION_JSON )
	    		.entity( euInfoJsonString )
	    		.put( ClientResponse.class ) ;//, euInfoJsonString );

	    assertEquals( 200, response.getStatus() );
	    
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
