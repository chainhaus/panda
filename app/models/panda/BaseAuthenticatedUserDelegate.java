package models.panda;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import be.objectify.deadbolt.java.models.Role;
import play.Logger;

@MappedSuperclass
public class BaseAuthenticatedUserDelegate extends BaseModel {
	@OneToOne(cascade=CascadeType.ALL)
	AuthenticatedUser authUser = new AuthenticatedUser();
	
	public AuthenticatedUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthenticatedUser authUser) {
		this.authUser = authUser;
	}

	public String getPicURL() {
		return authUser.getPicURL();
	}

	public void setPicURL(String picURL) {
		authUser.setPicURL(picURL);
	}

	public boolean hasRole(SecurityRole role) {
		return authUser.hasRole(role);
	}

	public void addRole(SecurityRole role) {
		authUser.addRole(role);
	}

	public void setRole(SecurityRole role) {
		authUser.setRole(role);
	}

	public void setEmail(String email) {
		Logger.info("email set to " + email);
		authUser.setEmail(email);
	}

	public void setFname(String fname) {
		authUser.setFname(fname);
	}

	public void setLname(String lname) {
		authUser.setLname(lname);
	}

	public void setPassword(String password) {
		authUser.setPassword(password);
	}

	public List<? extends Role> getRoles() {
		return authUser.getRoles();
	}

	public void setRoles(List<SecurityRole> roles) {
		authUser.setRoles(roles);
	}

	public String getName() {
		return authUser.getName();
	}

	public String getFname() {
		return authUser.getFname();
	}

	public String getLname() {
		return authUser.getLname();
	}

	public String getEmail() {
		return authUser.getEmail();
	}


}
