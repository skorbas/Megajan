package jpautil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import business.FilterExpression;



/**
 * Creates JPQL query using entity qualified name and filter expression.
 * @author skorbas
 *
 */
public class JpqlQueryCreator 
{ 
	private EntityManager em;
	
	/**
	 * Default constructor.
	 * @param aEntityManager
	 */
	public JpqlQueryCreator( EntityManager aEntityManager )
	{
		em = aEntityManager;
	}
	 
	
	/**
	 * Return entity class name from passed fully qualified name. 
	 * @param aEntityQn
	 * @return
	 */
	public String getEntityClassNameFromQn( String aEntityQn )
	{
		return aEntityQn.substring( aEntityQn.lastIndexOf('.') + 1, aEntityQn.length()  );
	}

	/**
	 * Get Class object for entity FQN.
	 * @param aEntityQn
	 * @return
	 */
	public Class<?> getEntityClass( String aEntityQn )
	{
		Class<?> entityClass = null;
		try
		{
			entityClass = Class.forName( aEntityQn );
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entityClass;
	}
	
	/**
	 * Creates JPQL criteria query using entity qualified name and filter expression.
	 * @param aEntityQn
	 * @param aFilter
	 */
	public CriteriaQuery<?> create( String aEntityQn, FilterExpression aFilter )
	{	
		Class<?> entityClass = getEntityClass( aEntityQn );
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<?> cq = cb.createQuery( entityClass );
		Root<?> queryRoot = cq.from( entityClass );

		cq.multiselect( queryRoot ); 
		
		if( aFilter != null )
		{
			// build AND predicate with conditions for criteria query  
			List<Predicate> predicateList = new ArrayList<Predicate>();
			HashMap<String, Object> conditionsMap = aFilter.getConditions();
			for( Map.Entry<String,Object> entry : conditionsMap.entrySet() ) 
			{
				Predicate equalPredicate = cb.equal( queryRoot.get( entry.getKey() ), entry.getValue() );
				predicateList.add( cb.and( equalPredicate ) );
			}
		
			if( predicateList.size() > 0  )
			{
				Predicate[] predicates = new Predicate[ predicateList.size() ];
				predicateList.toArray( predicates );
				cq.where( predicates );
			}
		}

		return cq;
	}
	
}
