package security.panda.deadbolt;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;

@Singleton
public class PandaHandlerCache implements HandlerCache {

    protected DeadboltHandler defaultHandler;
    protected final Map<String, DeadboltHandler> handlers = new HashMap<>();
    
    @Inject
    public PandaHandlerCache(final ExecutionContextProvider ecProvider) {
    	defaultHandler = new DefaultHandler(ecProvider);
    	handlers.put(defaultHandler.handlerName(), defaultHandler); // TODO change to handlerkeys
    }
    
	@Override
	public DeadboltHandler apply(String key) {
		// TODO Auto-generated method stub
		return handlers.get(key);
	}

	@Override
	public DeadboltHandler get() {
		// TODO Auto-generated method stub
		return defaultHandler;
	}

}