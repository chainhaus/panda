package panda.services;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.objectify.deadbolt.java.models.Role;
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
    public AuthService(Environment env, Config conf, ApplicationLifecycle al) {
        super(env, conf, al);
        Logger.info("Global salt set");
    }

    public AuthenticatedUser authenticateLoginAttempt(LoginForm loginAttempt) {
        return authenticateUser(loginAttempt.getUsername(), loginAttempt.getPassword());
    }

    public AuthenticatedUser authenticateUser(String email, String enteredPassword) {
        email = email.toLowerCase();
        AuthenticatedUser user = AuthenticatedUser.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (user.isDisabledByAdmin() || user.isDisabledByUser()) {
            return null;
        }
        if (!user.authenticate(enteredPassword)) {
            return null;
        }
        user.setLastLogin(new Date());
        Ebean.update(user);
        return user;
    }


    public String generateJWTToken(AuthenticatedUser user) {
        String token = "";
        String secret = conf.getString("play.http.secret.key");
        //Reading user roles as string array to pass with JWT token.
        List<String> userRoleNames = user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(Role::getName)
                .collect(Collectors.toList());
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create().withKeyId("airrefer.com")
                    .withIssuer("airrefer.com")
                    .withClaim("email", user.getEmail())
                    .withClaim("jti", user.getJtiToken())
                    //Binding user fname lname for show in dashboard.
                    .withClaim("fname", user.getFname())
                    .withClaim("lname", user.getLname())
                    //Binding profile picture
                    .withClaim("profile_image", user.getPicURL())
                    //Binding user roles
                    .withArrayClaim("roles", userRoleNames.stream().toArray(String[]::new))
                    .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusHours(4).toInstant()))
                    //.withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusSeconds(10).toInstant()))
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            Logger.error("Exception in JWT Generation : "+exception);
        } catch (JWTCreationException exception) {
            Logger.error("Exception in JWT Generation : "+exception);
        }
        return token;
    }
}
