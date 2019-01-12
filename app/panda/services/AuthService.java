package panda.services;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.objectify.deadbolt.java.models.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.typesafe.config.Config;

import io.ebean.Ebean;
import models.panda.AuthenticatedUser;
import panda.forms.LoginForm;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import security.panda.jwt.JwtControllerHelper;
import security.panda.jwt.JwtValidator;
import security.panda.jwt.VerifiedJwt;
import security.panda.jwt.VerifiedJwtImpl;

@Singleton
public class AuthService extends BaseService implements JwtValidator, JwtControllerHelper {

    private String secret;
    private JWTVerifier verifier;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Inject
    public AuthService(Environment env, Config conf, ApplicationLifecycle al) throws UnsupportedEncodingException {
        super(env, conf, al);
        Logger.info("Global salt set");

        this.secret = conf.getString("play.http.secret.key");

        Algorithm algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm)
                .withIssuer("airrefer.com")
                .build();
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

    @Override
    public F.Either<Error, VerifiedJwt> verify(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            VerifiedJwtImpl verifiedJwt = new VerifiedJwtImpl(jwt);
            return F.Either.Right(verifiedJwt);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            Logger.error("f=AuthService, event=verify, exception=JWTVerificationException, msg={}",
                    exception.getMessage());
            if(exception.getMessage().contains("expired")){
                return F.Either.Left(Error.TOKEN_EXPIRED);
            }
            return F.Either.Left(Error.ERR_INVALID_SIGNATURE_OR_CLAIM);
        }
    }

    @Override
    public Result verify(Http.Request request, Function<F.Either<Error, VerifiedJwt>, Result> f) {
        Optional<String> authHeader =  request.getHeaders().get(HEADER_AUTHORIZATION);

        if (!authHeader.filter(ah -> ah.contains(BEARER)).isPresent()) {
            Logger.error("f=AuthService, event=verify, error=authHeaderNotPresent");
        }

        String token = authHeader.map(ah -> ah.replace(BEARER, "")).orElse("");
        return f.apply(this.verify(token));
    }
}
