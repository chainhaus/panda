package models.panda;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import io.ebean.Finder;

import be.objectify.deadbolt.java.models.Permission;

@Entity
public class UserPermission extends BaseModel implements Permission {

	public static Finder<Long, UserPermission> find = new Finder<>(UserPermission.class);
	
	@ManyToMany (mappedBy="permissions")
	List<AuthenticatedUser> autheneticatedUsers = new ArrayList<>();
	
	@Column(name = "permission_value")
	private String value;
	
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public static UserPermission findByValue(String value) {
		return find.query().where().eq("value", value).findOne();
	}
	

}
