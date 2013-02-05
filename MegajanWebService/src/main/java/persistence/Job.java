package persistence;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




/**
 * The persistent class for the jobs database table.
 * 
 */
@Entity
@Table(name="jobs")
public class Job implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Job"; //Job.class.getName();
	
	private static final long serialVersionUID = 1L;
	
	// typy us³ug wewnêtrznych
	public static final String SERVICE_INTERN_TYPE_SENDING = "wysy³ka";
	public static final String SERVICE_INTERN_TYPE_REPAIR = "naprawa";
	public static final String SERVICE_INTERN_TYPE_EXPERTISE = "ekspertyza";
	public static final String SERVICE_INTERN_TYPE_EXPOSITION = "monta¿ ekspozycji";
	public static final String SERVICE_INTERN_TYPE_FURNITURE = "monta¿ mebli";
	public static final String SERVICE_INTERN_TYPE_INDIVIDUAL = "monta¿ indywidualny";

	// rodzaje zadania
	public static final String SERVICE_KIND_EXPOSITION = "obs³uga ekspozycji";
	public static final String SERVICE_KIND_RECLAMATION = "reklamacja";
	public static final String SERVICE_KIND_MONTAGE = "monta¿";
	public static final String SERVICE_KIND_PREPARATION = "prace przygotowawcze";

	//typ zadania
	public static final String TASK_GUARANTEE = "dzia³anie gwarancyjne";
	public static final String TASK_AFTER_GUARANTEE = "dzia³anie pogwarancyjne";
	public static final String TASK_OWN_RECLAMATION = "reklamacja w³asna";
	public static final String TASK_OWN_SERVICE = "us³uga";
	
	private String id;
	private String brandName;
	private String clCity;
	private String clEmail;
	private String clName;
	private String clNip;
	private String clPhone;
	private String clPostCode;
	private String clRegion;
	private String clStreet;
	private String clSurname;
	private String clientNumber;
	private String damage;
	
	private Date dateCreate;
	private Date dateDeadline;
	private Date dateDsContact;
	//@JsonSerialize
	private Date dateModify;
	private Date dateRealize;
	private Date dateSrContact;
	private Date dateSrSend;
	private Date dateSrVisit;
	
	private String description;
	private Boolean flagAdmApproved;
	private String infoClient;
	private String infoService;
	private String lastUserName;
	private Integer nextJobId;
	private Integer previousJobId;
	private Integer price;
	private String manufacturerName;
	private BigInteger randClNo;
	private String reclamationId;
	private User serviceGroupUser;			// u¿ytkownik grupy serwisowej
	private Status status;					//status zlecenia
	private TypeGeneral type;			 // typ zadania
	private TypeKind typeGeneral;		 // rodzaj zadania
	private TypeInternal typeIntern; // typ wewnêtrzny zadania
	private List<Attachment> attachments;
	private List<Product> products;
	private List<Service> services;
//	private User user; // uzytkownik, który utworzy³ zlecenie
	
    public Job() 
    {
    }


	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name="brand",length=256)
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brand) {
		this.brandName = brand;
	}


	@Column(name="cl_city", length=256)
	public String getClCity() {
		return this.clCity;
	}

	public void setClCity(String clCity) {
		this.clCity = clCity;
	}


	@Column(name="cl_email", length=256)
	public String getClEmail() {
		return this.clEmail;
	}

	public void setClEmail(String clEmail) {
		this.clEmail = clEmail;
	}


	@Column(name="cl_name", length=256)
	public String getClName() {
		return this.clName;
	}

	public void setClName(String clName) {
		this.clName = clName;
	}


	@Column(name="cl_nip", length=256)
	public String getClNip() {
		return this.clNip;
	}

	public void setClNip(String clNip) {
		this.clNip = clNip;
	}


	@Column(name="cl_phone", length=256)
	public String getClPhone() {
		return this.clPhone;
	}

	public void setClPhone(String clPhone) {
		this.clPhone = clPhone;
	}


	@Column(name="cl_post_code", length=256)
	public String getClPostCode() {
		return this.clPostCode;
	}

	public void setClPostCode(String clPostCode) {
		this.clPostCode = clPostCode;
	}


	@Column(name="cl_region", length=256)
	public String getClRegion() {
		return this.clRegion;
	}

	public void setClRegion(String clRegion) {
		this.clRegion = clRegion;
	}


	@Column(name="cl_street", length=256)
	public String getClStreet() {
		return this.clStreet;
	}

	public void setClStreet(String clStreet) {
		this.clStreet = clStreet;
	}


	@Column(name="cl_surname", length=256)
	public String getClSurname() {
		return this.clSurname;
	}

	public void setClSurname(String clSurname) {
		this.clSurname = clSurname;
	}


	@Column(name="client_number", length=256)
	public String getClientNumber() {
		return this.clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}


    @Lob()
	@Column(nullable=false)
	public String getDamage() {
		return this.damage;
	}

	public void setDamage(String damage) {
		this.damage = damage;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_create", nullable=false)
	public Date getDateCreate() {
		return this.dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_deadline")
	public Date getDateDeadline() {
		return this.dateDeadline;
	}

	public void setDateDeadline(Date dateDeadline) {
		this.dateDeadline = dateDeadline;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_ds_contact")
	public Date getDateDsContact() {
		return this.dateDsContact;
	}

	public void setDateDsContact(Date dateDsContact) {
		this.dateDsContact = dateDsContact;
	}


    //@Temporal( TemporalType.DATE )
	@Column(name="date_modify", nullable=false)
	public Date getDateModify() {
		return this.dateModify;
	}

	public void setDateModify(Date dateModify) {
		this.dateModify = dateModify;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_realize")
	public Date getDateRealize() {
		return this.dateRealize;
	}

	public void setDateRealize(Date dateRealize) {
		this.dateRealize = dateRealize;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_sr_contact")
	public Date getDateSrContact() {
		return this.dateSrContact;
	}

	public void setDateSrContact(Date dateSrContact) {
		this.dateSrContact = dateSrContact;
	}


    //@Temporal( TemporalType.DATE)
	@Column(name="date_sr_send")
	public Date getDateSrSend() {
		return this.dateSrSend;
	}

	public void setDateSrSend(Date dateSrSend) {
		this.dateSrSend = dateSrSend;
	}


   //@Temporal( TemporalType.DATE)
	@Column(name="date_sr_visit")
	public Date getDateSrVisit() {
		return this.dateSrVisit;
	}

	public void setDateSrVisit(Date dateSrVisit) {
		this.dateSrVisit = dateSrVisit;
	}


    @Lob()
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="flag_adm_approved", nullable=false)
	public Boolean getFlagAdmApproved() {
		return this.flagAdmApproved;
	}

	public void setFlagAdmApproved(Boolean flagAdmApproved) {
		this.flagAdmApproved = flagAdmApproved;
	}

    @Lob()
	@Column(name="info_client")
	public String getInfoClient() {
		return this.infoClient;
	}

	public void setInfoClient(String infoClient) {
		this.infoClient = infoClient;
	}


    @Lob()
	@Column(name="info_service")
	public String getInfoService() {
		return this.infoService;
	}

	public void setInfoService(String infoService) {
		this.infoService = infoService;
	}


	@Column(name="last_user_name", length=50)
	public String getLastUserName() {
		return this.lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}


	@Column(name="next_job_id")
	public Integer getNextJobId() {
		return this.nextJobId;
	}

	public void setNextJobId(Integer nextJobId) {
		this.nextJobId = nextJobId;
	}


	@Column(name="previous_job_id")
	public Integer getPreviousJobId() {
		return this.previousJobId;
	}

	public void setPreviousJobId(Integer previousJobId) {
		this.previousJobId = previousJobId;
	}


	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}


	@Column(name="producer",length=256)
	public String getManufacturerName() {
		return this.manufacturerName;
	}
	public void  setManufacturerName(String producer) {
		this.manufacturerName = producer;
	}


	@Column(name="rand_cl_no")
	public BigInteger getRandClNo() {
		return this.randClNo;
	}

	public void setRandClNo(BigInteger randClNo) {
		this.randClNo = randClNo;
	}


	@Column(name="reclamation_id", length=255)
	public String getReclamationId() {
		return this.reclamationId;
	}

	public void setReclamationId(String reclamationId) {
		this.reclamationId = reclamationId;
	}

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_sr_group",nullable=true)
	public User getServiceGroupUser() {
		return this.serviceGroupUser;
	}
	public void setServiceGroupUser(User user ) {
		this.serviceGroupUser = user;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="status",nullable=false)
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="type")
	public TypeGeneral getType() {
		return this.type;
	}
	public void setType(TypeGeneral type) {
		this.type = type;
	}


	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="type_general")
	public TypeKind getTypeGeneral() {
		return this.typeGeneral;
	}
	public void setTypeGeneral(TypeKind typeGeneral) {
		this.typeGeneral = typeGeneral;
	}


	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="type_intern")
	public TypeInternal getTypeIntern() {
		return this.typeIntern;
	}
	public void setTypeIntern(TypeInternal typeIntern) {
		this.typeIntern = typeIntern;
	}
	
	
	//bi-directional many-to-one association to Attachment
//	@OneToMany(mappedBy="job")
//	public List<Attachment> getAttachments() {
//		return this.attachments;
//	}
//	public void setAttachments(List<Attachment> attachments) {
//		this.attachments = attachments;
//	}
	
	//unidirectional one-to-many association to Attachments
//	@OneToMany(fetch=FetchType.LAZY)
//	@JoinColumn(name="job_id", referencedColumnName="id")
//	public List<Attachment> getAttachments() {
//		return this.attachments;
//	}
//	public void setAttachments(List<Attachment> attachments) {
//		this.attachments = attachments;
//	}

	//bi-directional many-to-many association to Product
    @ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(	name="job_products"
		, joinColumns={	@JoinColumn(name="job_id", nullable=false)	}
		, inverseJoinColumns={	@JoinColumn(name="product_name", referencedColumnName="name", nullable=false)}	)
	public List<Product> getProducts() {
		return this.products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}

	//bi-directional many-to-many association to Service
    @ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(	name="job_services"
		, joinColumns={	@JoinColumn(name="job_id", nullable=false)	}
		, inverseJoinColumns={@JoinColumn(name="service_name", referencedColumnName="name", nullable=false)	} )
	public List<Service> getServices() {
		return this.services;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}

	//bi-directional many-to-one association to User
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="id_user")
//	public User getUser() {
//		return this.user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
	
}