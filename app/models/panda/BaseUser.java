package models.panda;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.apache.commons.codec.binary.Base64;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import play.Logger;
import play.data.format.Formats;

@MappedSuperclass
public abstract class BaseUser extends BaseModel implements Subject {
	
	private static String globalSalt = "8ffc9477-23e4-4a41-8a87-6a77c52c0de1";
	
	@ManyToOne
	private Organization org;

	@ManyToMany
    private List<SecurityRole> roles = new ArrayList<>();
    
    @ManyToMany
    private List<UserPermission> permissions = new ArrayList<>();

	private String password;
	private byte[] userSalt;
	
	// profile
	private String fname;
	private String lname;
	private String email;
	private String mobilePhoneNumber;
	private String picURL;
	private String slackID;
	
	// location	
	private String lastIP;
	private String lastBrowser;
	
	// --- sign up process
	
	// mobile verification -- why do we need this??
	private boolean mobileVerified;

	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date mobileVerificationCodeLastSent;
	private boolean mobileVerificationCodeSent;
	
	private String mobileVerificationCode;
	private boolean mobileVerificationCodeGenerated;
	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date mobileVerificationCodeDateGenerated;
	
	// reset password semantics
	private String linkUUID = generateUUID();
	
	// email verification
	private boolean emailVerified;

	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date emailVerifiedDate;

	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date emailVerificationLastSent;
	private boolean emailVerificationSent;
	
	private String emailVerificationTicket;
	private boolean emailVerificationTicketGenerated;
	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date emailVerificationTicketDateGenerated;
		
	// account management
	private boolean probation = true; // allowed to use system but needs to confirm email & mobile
	private boolean emailBounced;	
	private String proxyEmailID = generateUUID().substring(0, 15);
	private String sessionUUID = generateUUID(); // used for mobile app session tracking
	
	private String publishableUUID = generateUUID();

	private boolean forceChangePassword;
	
	// Token for JWT
	private String jtiToken = generateUUID();
	
	
	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date lastLogin;
	
	@Column(columnDefinition = "datetime")
	@Formats.DateTime(pattern="MM/dd/yyyy")
	private Date lastLogout;
	
	
	public final Date getLastLogout() {
		return lastLogout;
	}

	public final void setLastLogout(Date lastLogout) {
		this.lastLogout = lastLogout;
	}

	// Deadbolt
	@Override
	public String getIdentifier() {
		return email;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		return permissions;
	}

	@Override
	public List<? extends Role> getRoles() {
		return roles;
	}
	
	public void removeRole(SecurityRole role) {
		this.roles.remove(role);
	}
	
	public void addRole(SecurityRole role) {
		this.roles.add(role);
	}

	public void setRoles(List<SecurityRole> roles) {
		this.roles = roles;
	}

	public void setPermissions(List<UserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public void setRole(SecurityRole role) {
		roles.add(role);
	}
	
	public void setPassword(String password) {
		Logger.info("Receive password " + password);
		try {
			this.password = hash(password,generateUserSalt());	
			Logger.info("Now hashed into: " + this.password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] generateUserSalt() {
		 try {
			this.userSalt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 return this.userSalt;
	}
	
    public static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        password+=globalSalt;
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, 20000, 256)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }
    
	public boolean authenticate(String enteredPassword) {
		String hashedEnteredPassword;
		try {
			Logger.info("Check with password: " + enteredPassword);
			hashedEnteredPassword = hash(enteredPassword, getUserSalt());
			Logger.info("Computed hash is " + hashedEnteredPassword);
			return hashedEnteredPassword.equals(getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	} 
	
	// account mgmt
	
	public final byte[] getUserSalt() {
		return userSalt;
	}

	public final void setUserSalt(byte[] userSalt) {
		this.userSalt = userSalt;
	}

	public String getProxyEmailID() {
		return proxyEmailID;
	}

	public void setProxyEmailID(String proxyEmailID) {
		this.proxyEmailID = proxyEmailID;
	}

	public boolean isEmailBounced() {
		return emailBounced;
	}

	public void setEmailBounced(boolean emailBounced) {
		this.emailBounced = emailBounced;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getSlackID() {
		return slackID;
	}
	public void setSlackID(String slackID) {
		this.slackID = slackID;
	}

	public String getFNameLInitial() {
		StringBuffer result = new StringBuffer(12);
		result.append(fname);
		if(lname!=null) {
			result.append(" ");
			result.append(lname.charAt(0));
			result.append(".");
		} else {
			result.append(" X.");
		}
		return result.toString();
	}
	public String getPicURL() {
		return picURL;
	}

	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}

	public String getName() {

		String f = null;
		String l = null;
		if(this.fname==null) 
			f="";
		else 
			f = this.fname;
		if(this.lname==null) 
			l="";
		else 
			l = this.lname;
		
		return f + " " + l;
		
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase().trim();
	}



	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public boolean isForceChangePassword() {
		return forceChangePassword;
	}

	public void setForceChangePassword(boolean forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	
	public String generateEmailVerificationTicket() {
		try {
			emailVerificationTicket = URLEncoder.encode(generateUUID()+generateUUID(),"UTF-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		emailVerificationTicketDateGenerated = new java.util.Date();
		emailVerificationTicketGenerated = true;
		return emailVerificationTicket;
	}
	
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	
	public static boolean isValidMobilePhoneNumber(String mobilePhoneNumber ) {
		if(mobilePhoneNumber==null || mobilePhoneNumber.isEmpty())
			return false;
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber pn = null;
		try {
			pn = phoneUtil.parse(mobilePhoneNumber, "US");
		} catch (NumberParseException e) {
			Logger.error("NumberParseException was thrown: " + e.toString());
		}
		return phoneUtil.isValidNumber(pn);
		
	}
	
	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public String getLastBrowser() {
		return lastBrowser;
	}

	public void setLastBrowser(String lastBrowser) {
		this.lastBrowser = lastBrowser;
	}

	public boolean isMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public String getMobileVerificationCode() {
		return mobileVerificationCode;
	}

	public void setMobileVerificationCode(String mobileVerificationCode) {
		this.mobileVerificationCode = mobileVerificationCode;
	}

	public boolean isMobileVerificationCodeGenerated() {
		return mobileVerificationCodeGenerated;
	}

	public void setMobileVerificationCodeGenerated(boolean mobileVerificationCodeGenerated) {
		this.mobileVerificationCodeGenerated = mobileVerificationCodeGenerated;
	}

	public String getEmailVerificationTicket() {
		return emailVerificationTicket;
	}

	public void setEmailVerificationTicket(String emailVerificationTicket) {
		this.emailVerificationTicket = emailVerificationTicket;
	}

	public Date getMobileVerificationCodeLastSent() {
		return mobileVerificationCodeLastSent;
	}

	public void setMobileVerificationCodeLastSent(Date mobileVerificationCodeLastSent) {
		this.mobileVerificationCodeLastSent = mobileVerificationCodeLastSent;
	}

	public Date getEmailVerificationLastSent() {
		return emailVerificationLastSent;
	}

	public void setEmailVerificationLastSent(Date emailVerificationLastSent) {
		this.emailVerificationLastSent = emailVerificationLastSent;
	}

	public Date getMobileVerificationCodeDateGenerated() {
		return mobileVerificationCodeDateGenerated;
	}

	public void setMobileVerificationCodeDateGenerated(Date mobileVerificationCodeDateGenerated) {
		this.mobileVerificationCodeDateGenerated = mobileVerificationCodeDateGenerated;
	}

	public boolean isEmailVerificationTicketGenerated() {
		return emailVerificationTicketGenerated;
	}

	public void setEmailVerificationTicketGenerated(boolean emailVerificationTicketGenerated) {
		this.emailVerificationTicketGenerated = emailVerificationTicketGenerated;
	}


	public boolean isMobileVerificationCodeSent() {
		return mobileVerificationCodeSent;
	}

	public void setMobileVerificationCodeSent(boolean mobileVerificationCodeSent) {
		this.mobileVerificationCodeSent = mobileVerificationCodeSent;
	}

	public boolean isEmailVerificationSent() {
		return emailVerificationSent;
	}

	public void setEmailVerificationSent(boolean emailVerificationSent) {
		this.emailVerificationSent = emailVerificationSent;
	}

	public Date getEmailVerifiedDate() {
		return emailVerifiedDate;
	}

	public void setEmailVerifiedDate(Date emailVerifiedDate) {
		this.emailVerifiedDate = emailVerifiedDate;
	}

	public Date getEmailVerificationTicketDateGenerated() {
		return emailVerificationTicketDateGenerated;
	}

	public void setEmailVerificationTicketDateGenerated(Date emailVerificationTicketDateGenerated) {
		this.emailVerificationTicketDateGenerated = emailVerificationTicketDateGenerated;
	}

	public boolean isProbation() {
		return probation;
	}

	public void setProbation(boolean probation) {
		this.probation = probation;
	}

	public String getSessionUUID() {
		return sessionUUID;
	}

	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}

	public String getLinkUUID() {
		return linkUUID;
	}

	public void setLinkUUID(String linkUUID) {
		this.linkUUID = linkUUID;
	}
/*
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
*/
	public String getPublishableUUID() {
		return publishableUUID;
	}

	public void setPublishableUUID(String publishableUUID) {
		this.publishableUUID = publishableUUID;
	}

	public static final String getGlobalSalt() {
		return globalSalt;
	}

	public static final void setGlobalSalt(String globalSalt) {
		BaseUser.globalSalt = globalSalt;
	}

	public final String getJtiToken() {
		return jtiToken;
	}

	public final void setJtiToken(String jtiToken) {
		this.jtiToken = jtiToken;
	}
	
}
