package util;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


public class EmProducer
{
	@PersistenceContext( unitName="MegajanWebService", type = PersistenceContextType.EXTENDED )
    private EntityManager entityManager = null;

    @Produces
    @MyEm
    public EntityManager getEntityManager()
    {
    	if( entityManager == null )
    	{
    		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "TestUnmanaged" );
    		entityManager = emFactory.createEntityManager();
            return entityManager;
    	}
    	
    	return entityManager;
    }
    
    public void close(@Disposes @MyEm EntityManager em)
    {
        em.close();
    }
}