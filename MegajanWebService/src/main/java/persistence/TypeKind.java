package persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The persistent class for the service_task_kinds database table.
 * 
 */
@Entity
@Table(name="service_task_kinds")
public class TypeKind implements Serializable
{
	/**
	 * Global entity unique identifier.
	 */
	public static final String ENTITY_QUALIFIED_NAME = "persistence.TypeKind";//TypeKind.class.getName();
	private static final long serialVersionUID = 1L;
	private String id;
	private String description;

    public TypeKind() {
    }


	@Id
	@Column(unique=true, nullable=false, length=50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(length=255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}