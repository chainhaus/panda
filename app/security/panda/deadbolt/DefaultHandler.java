package security.panda.deadbolt;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Subject;
import be.objectify.deadbolt.java.ConfigKeys;
import play.mvc.Result;
import play.mvc.Http.Context;
import models.panda.AuthenticatedUser;

public class DefaultHandler extends AbstractDeadboltHandler {

	public DefaultHandler(ExecutionContextProvider ecProvider) {
		super(ecProvider);
	}

/*	@Override
	public CompletionStage<Optional<Result>> beforeAuthCheck(Context context) {
		// TODO Auto-generated method stub
		return super.beforeAuthCheck(context);
	}
	
	@Override
	public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Context context) {
		// TODO Auto-generated method stub
		return super.getDynamicResourceHandler(context);
	}
*/
	@Override
	public CompletionStage<Optional<? extends Subject>> getSubject(Context context) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(AuthenticatedUser.findUserByEmail(context.session().get("email"))),
                (Executor) executionContextProvider.get());
	}
	// controllers.raven.routes.RavenController.error()

	@Override
	public CompletionStage<Result> onAuthFailure(Context context, Optional<String> content) {
		return CompletableFuture.completedFuture(ok("Unauthorized access"));
	}
	
	public String handlerName() {

		return ConfigKeys.DEFAULT_HANDLER_KEY;
	}	

}
