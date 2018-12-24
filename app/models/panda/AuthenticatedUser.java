package models.panda;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Entity;

import io.ebean.Finder;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import com.typesafe.config.Config;
import play.Logger;
import play.mvc.Http;
//import forms.ion.SignUpForm;
//import security.ion.SessionKeys;


@Entity
public class AuthenticatedUser extends BaseUser  {
		
	public static Finder<Long, AuthenticatedUser> find = new Finder<>(AuthenticatedUser.class);
	
	public static AuthenticatedUser findByUUID(String uuid) {
		return find.query().where().eq("uuid",uuid).findOne();
	}	

	public static AuthenticatedUser findUserByEmail(String email) {
		if(email==null)
			return null;
		return find.query().where().eq("email",email.toLowerCase().trim()).findOne();
	}
	
//	public static boolean checkIfUserExists(SignUpForm suf) {
//		String email = suf.getEmail().trim().toLowerCase();
//		AuthenticatedUser user = findUserByEmail(email);
//		if(user==null)
//			return false;
//		else
//			return true;
//		
//	}
	
	public static AuthenticatedUser findUserByEmailTicket(String emailVerificationTicket) {
		return find.query().where().eq("emailVerificationTicket", emailVerificationTicket.toLowerCase().trim()).findOne();
	}
	public static AuthenticatedUser findUserByLinkUUID(String linkUUID) {
		return find.query().where().eq("linkUUID", linkUUID).findOne();
	}
	
	public static List<AuthenticatedUser> getAllCurrentAuthenticatedUsers() {
		return find.all();
	}
	
	public static AuthenticatedUser getUserByID(String id) {
		return find.query().where().eq("id",id).findOne();
	}
	
	
//	public static AuthenticatedUser getCurrentUser(Http.Session session) {
//		String uid = session.get(SessionKeys.ID);
//		AuthenticatedUser user = find.query().where().eq("id",uid).findOne();
//		return user;
//	}
	
//	public boolean hasRole(SecurityRole role) {
//		List<? extends Role> roles = getRoles();
//		return roles.contains(role);
//	}
//	
//	public boolean hasPermission(UserPermission permission) {
//		List<? extends Permission> permissions = getPermissions();
//		return permissions.contains(permission);
//	}
	
	
}