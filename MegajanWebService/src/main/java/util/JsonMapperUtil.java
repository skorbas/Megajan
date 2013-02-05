package util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * Adapter utility class used for bidirectional conversion Java <-> JSON (Java Script Object Notation).
 * The class encapsulates an ObjectMapper object from JacksonJson library.
 * @author skorbas
 *
 */
public class JsonMapperUtil 
{
	/**
	 * JSON object mapper from Jackson library.
	 */
	private static ObjectMapper jsonMapper = new ObjectMapper(); 
	
	/**
	 * Converts JSON data to Java POJO object.
	 * @param aJsonData
	 * @param aEntityQn
	 * @return
	 */
	public static Object json2JavaObject( byte[] aJsonData, String aEntityQn )
	{
		try 
		{
			Object object = jsonMapper.readValue( aJsonData, 0, aJsonData.length, Class.forName( aEntityQn ) );
			return object;
		} 
		catch (JsonParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Converts JSON data to Java POJO object.
	 * @param aJsonData
	 * @param aEntityQn
	 * @return
	 */
	public static Object json2JavaObjectNoCatch( byte[] aJsonData, String aEntityQn ) throws Exception
	{
		return jsonMapper.readValue( aJsonData, 0, aJsonData.length, Class.forName( aEntityQn ) );
	}
	
	/**
	 * Converts JSON data passed as byte array to Java List filled with elements identified by aEntityQn.
	 * @param aJsonData
	 * @param aEntityQn
	 * @return
	 */
	public static List<?> json2JavaList( byte[] aJsonData, String aEntityQn )
	{
		try 
		{
			JavaType collectionType =  TypeFactory.collectionType( List.class, Class.forName( aEntityQn ) );
			List<?> objectList = jsonMapper.readValue( aJsonData, 0, aJsonData.length, collectionType);
			return objectList;
		} 
		catch (JsonParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Converts Java POJO object to JSON object encoded as byte array.
	 * @param clazz
	 * @param aObject
	 * @return
	 */
	public static byte[] java2JsonObj( Class<?> clazz, Object aObject )
	{
		try 
		{
			ObjectWriter writer = jsonMapper.writer();
			if( clazz != null )
			{
				writer.withType( clazz );
			}
			byte[] jsonData  = writer.writeValueAsBytes( aObject );
			//byte[] jsonData = jsonMapper.writeValueAsBytes( aObject );
			return jsonData;
		}
		catch (JsonGenerationException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		
		return null;
	}
	
	/**
	 * Converts Java List of object to JSON objects collection encoded as byte array.
	 * @param objectList
	 * @param aEntityQn
	 * @return
	 */
	public static byte[] java2JsonObjectList( List<?> objectList, String aEntityQn )
	{
		try 
		{
			JavaType collectionType =  TypeFactory.collectionType( List.class, Class.forName( aEntityQn ) );
			ObjectWriter writer = jsonMapper.writer();
			writer.withType( collectionType );
			byte[] jsonData =  writer.writeValueAsBytes( objectList );
			return jsonData;
		} 
		catch (JsonParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object json2JavaObject( String aJsonData, Class<? extends Object> aObjectClass )
	{
		try 
		{
			Object object = jsonMapper.readValue( aJsonData, aObjectClass );
			return object;
		} 
		catch (JsonParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static Object convertValue( Object aObject, Class<?> aRequiredClass )
	{
		Object convertValue = jsonMapper.convertValue( aObject , aRequiredClass );
		
		return convertValue;
	}
}
