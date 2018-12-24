package security.panda;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Provider;
import javax.inject.Singleton;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

@Singleton
public class PandaErrorHandler extends DefaultHttpErrorHandler {
	
	@Inject
	public PandaErrorHandler(Config configuration, Environment environment, OptionalSourceMapper sourceMapper,
			Provider<Router> routes) {
		super(configuration, environment, sourceMapper, routes);
		// TODO Auto-generated constructor stub
	}
	
    protected CompletionStage<Result> onForbidden(RequestHeader request, String message) {
        return CompletableFuture.completedFuture(
                Results.forbidden("Access to this resource is forbidden"));
    }
    
    protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(
                Results.internalServerError("Server error: " + exception.getMessage()));
    }    

}
