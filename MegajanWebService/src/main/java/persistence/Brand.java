package persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The persistent class for the brands database table.
 * 
 */
@Entity
@Table(name="brands")
public class Brand implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Brand";//Brand.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String manufacturerName;
	//private List<Product> products;
	//private List<User> users;

    public Brand() {
    }


	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="brand_name",length=255)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="brand_manufacturer",length=255)
	public String getManufacturerName() {
		return this.manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	
//	//bi-directional many-to-one association to Product
//	@JsonManagedReference("brand-products")
//	@OneToMany(mappedBy="brandBean",fetch=FetchType.LAZY)
//	public List<Product> getProducts() {
//		return this.products;
//	}
//	@JsonManagedReference("brand-products")
//	public void setProducts(List<Product> products) {
//		this.products = products;
//	}
	
	//bi-directional many-to-many association to User
//	@JsonManagedReference("brand-users")
//	@ManyToMany(mappedBy="brands",fetch=FetchType.LAZY)
//	public List<User> getUsers() {
//		return this.users;
//	}
//	@JsonManagedReference("brand-user")
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
	
}