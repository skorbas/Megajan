package persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The persistent class for the statuses database table.
 * 
 */
@Entity
@Table(name="statuses")
public class Status implements Serializable 
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.Status";//Status.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String description;
	private String name;

    public Status() {
    }


	@Id
	@Column(unique=true, nullable=true) //nullable must be TRUE because there are statuses equals "0" - e.g. "utworzone"
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(length=45)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(length=45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}