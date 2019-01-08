package panda.services;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.typesafe.config.Config;

import io.ebean.Ebean;
import models.panda.AuthenticatedUser;
import panda.forms.LoginForm;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;

@Singleton
public class AuthService extends BaseService {
	
	
	@Inject
	public AuthService (Environment env, Config conf, ApplicationLifecycle al) { //(Environment env, Config conf, ApplicationLifecycle al, Application app) {
		super(env, conf, al); // TODO: Fix
		//this.environment = env;
/*
		String globalSalt = conf.getString("ion.userService.globalSalt");
		if(globalSalt==null || globalSalt.isEmpty()) {
			Logger.info("Loading default salt. For production, salt must come from production configuration file");
			globalSalt = "01b6665c-1eca-433c-9add-58268cdfb0ea";
		} 
		
		BaseUser.setGlobalSalt(globalSalt);*/
		Logger.info("Global salt set");
	}
	
//	public AuthenticatedUser authenticate(LoginForm loginAttempt) {
//		authenticateUser(loginAttempt.getUsername());
//		return null;
//	}
	
//  return JWT.create()
//  .withIssuer("ThePlayApp")
//  .withClaim("user_id", userId)
//  .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(10).toInstant()))
//  .sign(algorithm);
	
	public AuthenticatedUser authenticateLoginAttempt(LoginForm loginAttempt) {
		
		return authenticateUser(loginAttempt.getUsername(),loginAttempt.getPassword());
	}
	
	public AuthenticatedUser authenticateUser(String email, String enteredPassword) {// { throws AuthenticationException {
		email = email.toLowerCase();
		AuthenticatedUser user = AuthenticatedUser.findUserByEmail(email);
		if(user==null) { // || user.isDeleted()) {
	//		AuthenticationException e = new AuthenticationException("User does not exist");
	//		LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
	//		Ebean.save(lh);
			//throw e;
			return null;
		}
				
		if(user.isDisabledByAdmin() || user.isDisabledByUser() ) {
	//		AuthenticationException e = new AuthenticationException("User is disabled");
	//		LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
	//		Ebean.save(lh);
			return null;
			//throw e;
		}
		
		if(!user.authenticate(enteredPassword)) {
	//		AuthenticationException e = new AuthenticationException("User or password mismatch");
	//		LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.FAIL,e.getMessage());
	//		Ebean.save(lh);
	//		throw e;
			return null;
	}
			
	//LoginHistory lh = new LoginHistory(email, enteredPassword, LoginHistory.SUCCESS,"");
	//Ebean.save(lh);
	user.setLastLogin(new Date());
	Ebean.update(user);
	return user;
}
	
	
	public String generateJWTToken(AuthenticatedUser user) {
		String token = "";
		String secret = conf.getString("play.http.secret.key");
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    token = JWT.create()
		        .withIssuer("airrefer.com")
		        .withClaim("email", user.getEmail())
		        .withClaim("jti", user.getJtiToken())
		        .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(180).toInstant()))
		        .sign(algorithm);
		} catch (UnsupportedEncodingException exception){
		    //UTF-8 encoding not supported
		} catch (JWTCreationException exception){
		    //Invalid Signing configuration / Couldn't convert Claims.
		}
		
		return token;
	}
}
