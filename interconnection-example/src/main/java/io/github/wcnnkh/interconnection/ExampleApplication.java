package io.github.wcnnkh.interconnection;

import io.basc.framework.beans.annotation.Bean;
import io.basc.framework.boot.support.MainApplication;
import io.basc.framework.web.cors.Cors;
import io.basc.framework.web.cors.CorsRegistry;

public class ExampleApplication {
	public static void main(String[] args) {
		MainApplication.run(ExampleApplication.class, args);
	}
	
	@Bean
	public CorsRegistry getCorsRegistry() {
		CorsRegistry registry = new CorsRegistry();
		registry.add("/**", Cors.DEFAULT);
		return registry;
	}
}
