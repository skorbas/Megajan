package persistence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * The persistent class for the job_attachments database table.
 * 
 */
@Entity
@Table(name="job_attachments")
public class Attachment implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Attachment";//Attachment.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private byte[] content;
	private String extension;
	private String mimeType;
	private String name;
	private Integer size;
	private Job job;
	//private String jobId;

    public Attachment() {
    }


	@Id
	@Column(unique=true, nullable=false)
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable=false, length=5)
	public String getExtension() {
		return this.extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}


	@Column(name="mime_type", nullable=false, length=30)
	public String getMimeType() {
		return this.mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	@Column(nullable=false, length=256)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Column(nullable=false)
	public Integer getSize() {
		return this.size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
//	@Column(name="job_id",nullable=false)
//	public String getJobId() {
//		return this.jobId;
//	}
//	public void setJobId(String id) {
//		this.jobId = id;
//	}
	
	//bi-directional many-to-one association to Job
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="job_id", nullable=false)
//	public Job getJob() {
//		return this.job;
//	}
//	public void setJob(Job job) {
//		this.job = job;
//	}
//	
	// Content is a big data that is why we are fetching it lazily
    @Lob @Basic (fetch = FetchType.LAZY) 
	@Column(nullable=false)
	public byte[] getContent() {
		return this.content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
}