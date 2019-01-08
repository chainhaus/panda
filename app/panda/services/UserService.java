package panda.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.codec.binary.Base64;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.typesafe.config.Config;

//import exceptions.ion.AuthenticationException;
//import forms.ion.LoginForm;
import io.ebean.Ebean;

import play.Environment;
import play.Logger;
import play.data.Form;

@Singleton
public class UserService extends BaseService {
	
	@Inject
	public UserService(Config conf) {
		super(conf);
		// TODO Auto-generated constructor stub
	}
	
	
//	@Inject
//	public UserService (Environment env, Config conf) { //(Environment env, Config conf, ApplicationLifecycle al, Application app) {
//		//super(env, conf, al, app); // TODO: Fix
//		this.environment = env;
///*
//		String globalSalt = conf.getString("ion.userService.globalSalt");
//		if(globalSalt==null || globalSalt.isEmpty()) {
//			Logger.info("Loading default salt. For production, salt must come from production configuration file");
//			globalSalt = "01b6665c-1eca-433c-9add-58268cdfb0ea";
//		} 
//		
//		BaseUser.setGlobalSalt(globalSalt);*/
//		Logger.info("Global salt set");
//	}
//	
//	public AuthenticatedUser getUser(String sessionID)  throws AuthenticationException {
//		if (sessionID==null || sessionID.isEmpty())
//			throw new AuthenticationException("Session ID invalid");
//		AuthenticatedUser authUser =  AuthenticatedUser.getUserByID(sessionID);
//	    if(authUser==null)
//	    	throw new AuthenticationException("User not found");
//	    return authUser;
//	}
//	
//	public AuthenticatedUser getUserFromToken (String token) throws AuthenticationException {
//	    DecodedJWT jwt = JWT.decode(token);
//	    if(jwt==null)
//	    	throw new AuthenticationException("JWT decode failed");
//	    String email = jwt.getClaim("email").asString();
//	    if(email==null)
//	    	throw new AuthenticationException("Email claim not found in JWT token");
//	    
///*	    String jtiToken = jwt.getClaim("jti").asString();
//	    if(jtiToken==null)
//	    	throw new AuthenticationException("JTI claim not found in JWT token");	*/   
//	    
//	    AuthenticatedUser authUser = AuthenticatedUser.findUserByEmail(email);
//	    if(authUser==null)
//	    	throw new AuthenticationException("User not found");
//	    
///*	    if(!authUser.getJtiToken().equals(jtiToken))
//	    	throw new AuthenticationException("JTI token mismatch");*/
//	    
//	    return authUser;
//	}
//	
//	public String generateJWTToken(AuthenticatedUser user) {
//		String token = "";
//		try {
//		    Algorithm algorithm = Algorithm.HMAC256("changeinprod123");
//		    token = JWT.create()
//		        .withIssuer("overridelabs")
//		        .withClaim("email", user.getEmail())
//		        .withClaim("jti", user.getJtiToken())
//		        .sign(algorithm);
//		} catch (UnsupportedEncodingException exception){
//		    //UTF-8 encoding not supported
//		} catch (JWTCreationException exception){
//		    //Invalid Signing configuration / Couldn't convert Claims.
//		}
//		
//		return token;
//	}
//	
//	public AuthenticatedUser authenticateUser(Form<LoginForm> form) throws AuthenticationException {
//		Logger.info("User: " + form.get().getEmail() + " " + form.get().getPassword());
//		return authenticateUser(form.get().getEmail(),form.get().getPassword());
//		
//	}
//	
//	public AuthenticatedUser authenticateUser(String email, String enteredPassword) throws AuthenticationException {
//		email = email.toLowerCase();
//		AuthenticatedUser user = AuthenticatedUser.findUserByEmail(email);
//		if(user==null || user.isDeleted()) {
//			AuthenticationException e = new AuthenticationException("User does not exist");
//			LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
//			Ebean.save(lh);
//			throw e;
//		}
//				
//		if(user.isDisabledByAdmin() || user.isDisabledByUser() ) {
//			AuthenticationException e = new AuthenticationException("User is disabled");
//			LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
//			Ebean.save(lh);
//			throw e;
//		}
//		
//		if(!user.authenticate(enteredPassword)) {
//			AuthenticationException e = new AuthenticationException("User or password mismatch");
//			LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
//			Ebean.save(lh);
//			throw e;
//		}
//				
//		LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.SUCCESS,"");
//		Ebean.save(lh);
//		user.setLastLogin(new Date());
//		Ebean.update(user);
//		return user;
//	}
//	
//	public void logoutUser(AuthenticatedUser user) {
//		user.setLastLogout(new Date());
//		Ebean.update(user);
//	}
	

	

}
