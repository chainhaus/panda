package models.panda;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import io.ebean.Ebean;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;

@MappedSuperclass
public class BaseModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	private Long version;
	
	private String uuid = generateUUID();
	
	// Proxy for a hard delete
	private boolean invalid = false;
	
	// Available to user, false means it is archived
	private boolean current = true;
	private boolean disabledByUser = false;
	private boolean disabledByAdmin = false;
	
	@Column(columnDefinition = "DATETIME")
	@CreatedTimestamp
    private Date whenCreated = new Date();
	
    @Column(columnDefinition = "DATETIME")
    @UpdatedTimestamp
    private Date whenUpdated;
    
	public final boolean isCurrent() {
		return current;
	}

	public final void setCurrent(boolean current) {
		this.current = current;
	}
    
	
    
    public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	public final void enabledByUser() {
    	this.disabledByUser = false;
    	update();
    }
    
    public final void disabledByUser() {
    	this.disabledByUser = true;
    	update();
    }    
    
    public final void enabledByAdmin() {
    	this.disabledByAdmin = false;
    	update();
    }
    
    public final void disabledByAdmin() {
    	this.disabledByAdmin = true;
    	update();
    }    
    
    
    public final void save() {
    	Ebean.save(this);
    }

    public final void update() {
    	Ebean.update(this);
    }
    
	
	public final boolean isDisabledByUser() {
		return disabledByUser;
	}

	public final void setDisabledByUser(boolean disabledByUser) {
		this.disabledByUser = disabledByUser;
	}

	public final boolean isDisabledByAdmin() {
		return disabledByAdmin;
	}

	public final void setDisabledByAdmin(boolean disabledByAdmin) {
		this.disabledByAdmin = disabledByAdmin;
	}

	public Long getId() {
		return id;
	}
	
	public String getSubUuid() {
		return uuid.substring(0, 8);
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public final Date getWhenCreated() {
		return whenCreated;
	}

	public final void setWhenCreated(Date whenCreated) {
		this.whenCreated = whenCreated;
	}

	public final Date getWhenUpdated() {
		return whenUpdated;
	}

	public final void setWhenUpdated(Date whenUpdated) {
		this.whenUpdated = whenUpdated;
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	@PrePersist
	public void prePersist() throws IOException {

	}

	@PreUpdate
	public void preUpdate() throws IOException {

	}

	@PostLoad
	public void postLoad() throws IOException {
	}
}
