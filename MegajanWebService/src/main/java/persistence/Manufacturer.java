package persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;





/**
 * The persistent class for the manufacturers database table.
 * 
 */
@Entity
@Table(name="manufacturers")
public class Manufacturer implements Serializable
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Manufacturer"; //Manufacturer.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private List<Brand> brands;

    public Manufacturer() {
    }

	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	@Column(name="man_name")
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//unidirectional mapping to Brands
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="brand_manufacturer",referencedColumnName="man_name") 
	public List<Brand> getBrands() {
		return this.brands;
	}
	//@JsonManagedReference("manufacturer-brands")
	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
	
}