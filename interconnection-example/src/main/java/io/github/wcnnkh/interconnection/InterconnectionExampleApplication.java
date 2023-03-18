package io.github.wcnnkh.interconnection;

import javax.servlet.annotation.MultipartConfig;

import io.basc.framework.boot.annotation.BootApplication;
import io.basc.framework.boot.support.MainApplication;
import io.basc.framework.context.annotation.Bean;
import io.basc.framework.db.DB;
import io.basc.framework.sqlite.SQLiteDB;
import io.basc.framework.web.cors.Cors;
import io.basc.framework.web.cors.CorsRegistry;

@MultipartConfig
@BootApplication
public class InterconnectionExampleApplication {
	public static void main(String[] args) {
		MainApplication.run(InterconnectionExampleApplication.class, args);
	}

	@Bean
	public CorsRegistry getCorsRegistry() {
		CorsRegistry registry = new CorsRegistry();
		registry.add("/**", Cors.DEFAULT);
		return registry;
	}

	@Bean
	public DB getDB() {
		return SQLiteDB.create("transaction");
	}
}
