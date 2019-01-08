package panda.services;

import javax.inject.Inject;

import play.Application;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;


public abstract class BaseService { // implements IService {
	
	protected Environment env;
	protected Config conf;
	protected ApplicationLifecycle al;

	public BaseService (Environment env, Config conf, ApplicationLifecycle al) {
		this.env = env;
		this.conf = conf;
		this.al = al;
		Logger.info(this.getClass().getName() + " started");
	}
	
	public BaseService (Config conf) {
		this.conf = conf;
	}
}
