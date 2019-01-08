package security.panda.deadbolt;

import javax.inject.Singleton;

import be.objectify.deadbolt.java.cache.HandlerCache;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class DeadboltHookModule extends Module {

	public DeadboltHookModule() {
		// TODO Auto-generated constructor stub
	}

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final Configuration configuration) {
        return seq( /*bind(TemplateFailureListener.class).to(MyCustomTemplateFailureListener.class).in(Singleton.class),*/
                   bind(HandlerCache.class).to(PandaHandlerCache.class).in(Singleton.class));
    }

}