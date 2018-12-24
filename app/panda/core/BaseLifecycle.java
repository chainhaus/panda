package panda.core;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import io.ebean.Ebean;

import models.panda.AppRegistry;
import play.Application;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;


public class BaseLifecycle {

	private Environment env;
	private Config conf;
	private Application app;
	private ApplicationLifecycle al;
	private String appName;
	private String appVersion;
	private String appTitle;
	
	public BaseLifecycle(Environment env, Config conf, Application app, ApplicationLifecycle al) {
		this.env = env;
		this.conf = conf;
		this.al = al;
		this.app = app;
		appName = conf.getString("panda.app.name");
		appVersion = conf.getString("panda.app.majorVersion");
		appTitle = conf.getString("panda.app.pageTitle");
		Logger.info("");
		Logger.info("");
		Logger.info("########### Starting app " + appName + " version " + appVersion + " ###########");
		Logger.info("");
		Logger.info("");
		Logger.info("Mode: " + env.mode().name());
		String executorThreads = conf.getString("panda.executorThreadService.threadPoolSize");
		if (executorThreads != null) {
			int t = Integer.valueOf(executorThreads);
			if (t > 0) {
				Executor.setThreads(t);
			}
		}
		
		if(new File("conf/startup.yml").exists())
			if(AppRegistry.find.query().findCount() < 1)
				loadAndSave("startup.yml");
			else
				Logger.info("Skipping startup YML load");
		else
			Logger.info("No startup YML found");
		
		al.addStopHook(() -> {
			Logger.info("");
			Logger.info("");
			Logger.info("########### Stopping app " + appName + " version " + appVersion + " ###########");
			Logger.info("");
			Logger.info("");
			if (env.isProd()) {
				// ss.publishToActivityChannel("Production Padwow instance
				// shutting down");
			}
			Executor.shutdownExecutorService();
			return CompletableFuture.completedFuture(null);
		});
		

	}

	// Snakeyaml methods
	protected Object load(String resourceName) {
		return load(env.resourceAsStream(resourceName), env.classLoader());
	}
	
	protected void loadAndSave(String resourceName) {
		Logger.info("Loading YML " + resourceName );
		int count = Ebean.saveAll((List<?>)load(resourceName));
		Logger.info(".....with " + count + " records");
	}

	private static Object load(InputStream is, ClassLoader classloader) {
		org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml(new CustomClassLoaderConstructor(classloader));
		return yaml.load(is);
	}

}
