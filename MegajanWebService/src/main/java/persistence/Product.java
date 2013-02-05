package persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The persistent class for the products database table.
 * 
 */
@Entity
@Table(name="products")
public class Product implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Product";//Product.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String name;
	private String brandName;
	private String manufacturerName;
	//private Brand brandBean;

    public Product() {
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
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(length=255)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="brand",length=255)
	public String getBrandName() {
		return this.brandName;
	}
	public void setBrandName(String name) {
		this.brandName = name;
	}

	@Column(name="manufacturer",length=255)
	public String getManufacturerName() {
		return this.manufacturerName;
	}
	public void setManufacturerName(String manufacturer) {
		this.manufacturerName = manufacturer;
	}

	//bi-directional many-to-one association to Brand
//	@JsonBackReference("brand-products")
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="brand", referencedColumnName="brand_name")
//	public Brand getBrandBean() {
//		return this.brandBean;
//	}
//	@JsonBackReference("brand-products")
//	public void setBrandBean(Brand brandBean) {
//		this.brandBean = brandBean;
//	}
	
}