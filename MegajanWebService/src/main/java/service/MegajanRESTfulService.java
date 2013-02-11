package service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.io.JsonStringEncoder;
import org.codehaus.jackson.map.ObjectMapper;

import persistence.Job;
import persistence.Status;
import persistence.TypeInternal;
import persistence.User;
import util.ExceptionUtil;
import util.JsonMapperUtil;
import business.FilterExpression;
import business.MegajanBusiness;
import business.MegajanBusinessIf;
import business.SystemResponse;

@Stateless
@LocalBean
@Path("system")
public class MegajanRESTfulService 
{
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;
    
    private static final String JSON_UNDEFINED = "undefined";
    private static final String NULL_VALUE = "null";
    
    // Injection of MegajanBusinessIf is possible because MegajnaBusiness is annotated as @ManagedBean 
    @Inject
    private MegajanBusinessIf system;
    
    /**
     * Default constructor. 
     */
    public MegajanRESTfulService() 
    {
        // TODO Auto-generated constructor stub
    }

    @GET
    @Path("/start")
    @Produces("text/plain")
    public String startPage() 
    {
        return "test ok!!!";
    }
    
    
	@GET
	@Path("/authenticate")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] authenticate( @QueryParam("user") String aUser, 
			                    @QueryParam("password") String aPassword )
	{
    	SystemResponse resp =  system.authenticate( aUser, aPassword );
    	
    	return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}
    
	@GET
	@Path("/getJobs")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] getJobs( @DefaultValue(JSON_UNDEFINED) @QueryParam("id") String jobId,
						   @DefaultValue(JSON_UNDEFINED) @QueryParam("status") String statusJson, 
						   @DefaultValue(JSON_UNDEFINED) @QueryParam("typeIntern") String typeInternJson,
						   @DefaultValue(JSON_UNDEFINED) @QueryParam("serviceGroup") String serviceGroupJson,
						   @DefaultValue("false") @QueryParam("loadLazy") boolean loadLazy )
	{
		//Jackson JSON parser accepts data only in UTF-8 format so the parameters have to be converted to UTF-8
		statusJson = new String(JsonStringEncoder.getInstance().encodeAsUTF8(statusJson));
		typeInternJson = new String(JsonStringEncoder.getInstance().encodeAsUTF8(typeInternJson));
		serviceGroupJson = new String(JsonStringEncoder.getInstance().encodeAsUTF8(serviceGroupJson));

		FilterExpression filter = new FilterExpression();
		if( isJsonValueValid( jobId ) )
		{
    		filter.addCondition("id", jobId );
		}
		if( isJsonValueValid( statusJson ) ) 
		{
    		Object statusObj = JsonMapperUtil.json2JavaObject( statusJson.getBytes(), Status.ENTITY_QUALIFIED_NAME );
    		filter.addCondition("status", statusObj );
		}
		if( isJsonValueValid( typeInternJson ) )
		{
    		Object typeInternalObj = JsonMapperUtil.json2JavaObject( typeInternJson.getBytes(), TypeInternal.ENTITY_QUALIFIED_NAME );
    		filter.addCondition("typeIntern", typeInternalObj );
		}
		if( isJsonValueValid( serviceGroupJson ) )
		{
    		Object srGroupUserObj = JsonMapperUtil.json2JavaObject( serviceGroupJson.getBytes(), User.ENTITY_QUALIFIED_NAME );
    		filter.addCondition("serviceGroupUser", srGroupUserObj );
		}
			
		SystemResponse resp = system.getEntities( Job.ENTITY_QUALIFIED_NAME, filter, loadLazy );	
		
		return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}
	/**
	 * Checks if JSON value from application has valid value.
	 * @param jsonValue
	 * @return
	 */
	private boolean isJsonValueValid( String jsonValue )
	{
		return (jsonValue != null /*&& !jsonValue.equals(JSON_UNDEFINED)*/ && !jsonValue.isEmpty() && !jsonValue.equals(NULL_VALUE));
	}
	
	@GET
	@Path("/getStatuses")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] getStatuses( )
	{ 	
    	SystemResponse resp = system.getEntities( Status.ENTITY_QUALIFIED_NAME, null, false );

    	return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}

	@GET
	@Path("/getInternalTypes")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] getInternalTypes( )
	{
		SystemResponse resp = system.getEntities( TypeInternal.ENTITY_QUALIFIED_NAME, null, false );
		
		return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}
	
	@GET
	@Path("/getServiceGroups")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] getServiceGroups( )
	{
    	FilterExpression filter = new FilterExpression();
    	filter.addCondition("type", new Integer(3) );
    		
    	SystemResponse resp = system.getEntities( User.ENTITY_QUALIFIED_NAME, null, false );
  
    	return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}
	
	@GET
	@Path("/getUsers")
	@Produces( MediaType.APPLICATION_JSON )
	public byte[] getUsers( )
	{
    	//MegajanBusinessIf system = new MegajanBusiness(entityManager);
    	SystemResponse resp =  system.getEntities( User.ENTITY_QUALIFIED_NAME, null, false );
    	
    	return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
	}
	
    /**
     * PUT method for updating or creating an instance of MegajanRESTfulService
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("/putJsonEntity")
    @Consumes("application/json")
    @Produces( MediaType.APPLICATION_JSON )
    public byte[] putJsonEntity( String jsonData ) //@QueryParam("entityQn") String entityQualifiedName ) 
    {
    	SystemResponse resp = null;
    	
		//Jackson JSON parser accepts data only in UTF-8 format so the parameters have to be converted to UTF-8
		jsonData = new String(JsonStringEncoder.getInstance().encodeAsUTF8( jsonData ));
		
		// test
		ObjectMapper jsonMapper = new ObjectMapper(); 
		try 
		{
			JsonNode readTree = jsonMapper.readTree( jsonData );
			JsonNode jsonEntityQnNode = readTree.get( "entityQn");
			JsonNode jsonEntityObjectNode = readTree.get( "entityObj");
			
			String entityQn = jsonEntityQnNode.getTextValue();//.toString(); <- can't be toString it doesn't convert properly simple string value
			String entityObject = jsonEntityObjectNode.toString();
			
			Object entityObj = JsonMapperUtil.json2JavaObjectNoCatch( entityObject.getBytes(), entityQn );
			
			resp = system.updateEntity( entityObj );
		} 
		catch (Exception e) 
		{
			resp = new SystemResponse();
			resp.dataContainer.data = null;
			resp.statusInfo.errorMsg = ExceptionUtil.getStackTraceAsString( e );
			e.printStackTrace();
		}
		
		return JsonMapperUtil.java2JsonObj( SystemResponse.class, resp );
    }
}