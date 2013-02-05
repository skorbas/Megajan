package persistence;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The persistent class for the services database table.
 * 
 */
@Entity
@Table(name="services")
public class Service implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Service";//Service.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private BigDecimal price;

    public Service() {
    }


	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(nullable=false, precision=10, scale=2)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}