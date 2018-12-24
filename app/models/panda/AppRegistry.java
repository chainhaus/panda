package models.panda;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.annotation.Cache;


@Cache
@Entity
public class AppRegistry extends BaseModel {
	
	public static Finder<Long, AppRegistry> find = new Finder<>(AppRegistry.class);
	private String appName;
	private String apiKey;
	private int appMajorVersion;
	private int appMinorVersion;
	private String resetPasswordURL;
	private String resetPasswordEmailTemplate;
	private String confirmEmailURL;
	private String confirmEmailEmailTemplate;
	private boolean maintenanceMode;
	private String appHTMLTitle;

	public String getAppHTMLTitle() {
		return appHTMLTitle;
	}

	public void setAppHTMLTitle(String appHTMLTitle) {
		this.appHTMLTitle = appHTMLTitle;
	}

	public boolean isMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(boolean maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}

	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name = "appregistry_authenticateduser")
	private List<AuthenticatedUser> users;
	
	public static AppRegistry getAppByUUID(String uuid) {
		return find.query().where().eq("uuid", uuid).findOne();

	}
	
	public static AppRegistry getAppByAPIKey(String key) {
		return find.query().where().eq("apiKey", key).findOne();
	}
	
	public static AppRegistry getByApiKey(String key) {
		return find.query().where().eq("apiKey", key).findOne();
	}
	
	public AuthenticatedUser findUserByEmail(String email) {
		String emailLower = email.toLowerCase().trim();
		for(AuthenticatedUser u : users)
			if(u.getEmail().equals(emailLower))
				return u;
		return null;
	}
	
	public AuthenticatedUser findUserByLinkUUID(String uuid) {
		String uuidLower = uuid.toLowerCase();
		for(AuthenticatedUser u : users)
			if(u.getLinkUUID().equals(uuidLower))
				return u;
		return null;
	}
	

	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public int getAppMajorVersion() {
		return appMajorVersion;
	}

	public void setAppMajorVersion(int appMajorVersion) {
		this.appMajorVersion = appMajorVersion;
	}

	public int getAppMinorVersion() {
		return appMinorVersion;
	}

	public void setAppMinorVersion(int appMinorVersion) {
		this.appMinorVersion = appMinorVersion;
	}

	public List<AuthenticatedUser> getUsers() {
		return users;
	}
	public void setUsers(List<AuthenticatedUser> users) {
		this.users = users;
	}
	public void addUser(AuthenticatedUser u) {
		users.add(u);
	}

	public String getResetPasswordURL() {
		return resetPasswordURL;
	}

	public void setResetPasswordURL(String resetPasswordURL) {
		this.resetPasswordURL = resetPasswordURL;
	}

	public String getConfirmEmailURL() {
		return confirmEmailURL;
	}

	public void setConfirmEmailURL(String confirmEmailURL) {
		this.confirmEmailURL = confirmEmailURL;
	}

	public String getResetPasswordEmailTemplate() {
		return resetPasswordEmailTemplate;
	}

	public void setResetPasswordEmailTemplate(String resetPasswordEmailTemplate) {
		this.resetPasswordEmailTemplate = resetPasswordEmailTemplate;
	}

	public String getConfirmEmailEmailTemplate() {
		return confirmEmailEmailTemplate;
	}

	public void setConfirmEmailEmailTemplate(String confirmEmailEmailTemplate) {
		this.confirmEmailEmailTemplate = confirmEmailEmailTemplate;
	}
	
}
