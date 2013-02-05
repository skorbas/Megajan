package persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;




/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
public class User implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.User";//User.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String address;
	private String cooperationRange;
	private String email;
	private String informations;
	private String name;
	private String password;
	private String phone;
	private String region;
	private String status;
	private Integer type;
	private List<Brand> brands;

    public User() {
    }


	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(length=255)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	@Column(name="cooperation_range", length=255)
	public String getCooperationRange() {
		return this.cooperationRange;
	}

	public void setCooperationRange(String cooperationRange) {
		this.cooperationRange = cooperationRange;
	}


	@Column(nullable=false, length=256)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


    @Lob()
	public String getInformations() {
		return this.informations;
	}

	public void setInformations(String informations) {
		this.informations = informations;
	}


	@Column(nullable=false, length=50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(nullable=false, length=256)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(length=30)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Column(length=255)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}


	@Column(length=50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(nullable=false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	//bi-directional many-to-many association to Brand
	//@JsonManagedReference("user-brands")
    @ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(	name="users_brands"
		, joinColumns={	@JoinColumn(name="user_id", nullable=false)	}
		, inverseJoinColumns={@JoinColumn(name="brand_name", referencedColumnName="brand_name", nullable=false)})
	public List<Brand> getBrands() {
		return this.brands;
	}
	//@JsonManagedReference("user-brands")
	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
	
}