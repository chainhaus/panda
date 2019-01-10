package controllers.panda;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.panda.AuthenticatedUser;
import panda.forms.LoginForm;
import panda.helpers.ResponseUtil;
import panda.services.AuthService;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller{

    @Inject private AuthService auth;
    
    /*
     * Signin() extracts checks for JSON content in the body, binds it to a LoginForm and
     * generates a JTW token if authentication is valid
     * 
     */
    public Result signin() throws UnsupportedEncodingException {
        JsonNode json = request().body().asJson();
        
        if (json == null) 
            return forbidden();
   
        LoginForm loginAttempt = Json.fromJson(json,LoginForm.class);
        AuthenticatedUser user = auth.authenticateLoginAttempt(loginAttempt);
        if(user !=null) {

			ObjectNode result = Json.newObject();
			result.put("access_token", auth.generateJWTToken(user));
			return ok(result);
        } else {
	        Logger.error("Authentication failed, attempt JSON: ", json.toString());
	        return forbidden(ResponseUtil.createResponse("Wrong user credentials", true));
        }

    }


    public Result signup(){
        Logger.info("********* New User Sign Up ******** You can use any database store to register the User"+request().body().asJson().toString());
        return ok(ResponseUtil.createResponse(request().body().asJson().toString(),true));
    }

}
