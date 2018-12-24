package models.panda;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import io.ebean.Finder;

import be.objectify.deadbolt.java.models.Role;

@Entity
public class SecurityRole extends BaseModel implements Role {
	
	private String name;
	private boolean publicFacing;
	private String publicDescription;
	
	@ManyToMany (mappedBy="roles")
	List<AuthenticatedUser> autheneticatedUsers = new ArrayList<>();

	public static Finder<Long, SecurityRole> find = new Finder<>(SecurityRole.class);

	public String getName() {
		return name;
	}
	
	public static SecurityRole findByName(String name) {
		return find.query().where().eq("name", name).findOne();
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AuthenticatedUser> getAutheneticatedUsers() {
		return autheneticatedUsers;
	}

	public void setAutheneticatedUsers(List<AuthenticatedUser> autheneticatedUsers) {
		this.autheneticatedUsers = autheneticatedUsers;
	}
	
	public static List<SecurityRole> getAll() {
		return find.all();
	}
	
	public static List<SecurityRole> getPublicAll() {
		return find.query().where().eq("publicFacing", true).findList();
	}

	public boolean isPublicFacing() {
		return publicFacing;
	}

	public void setPublicFacing(boolean publicFacing) {
		this.publicFacing = publicFacing;
	}

	public String getPublicDescription() {
		return publicDescription;
	}

	public void setPublicDescription(String publicDescription) {
		this.publicDescription = publicDescription;
	}
	
}
