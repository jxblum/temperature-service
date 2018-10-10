/*
 *  Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package example.app.temp.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;

import example.app.temp.function.impl.AverageTemperatureFunction;
import example.app.temp.server.config.GeodeConfiguration;

/**
 * The TemperatureServerApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@CacheServerApplication(name = "TemperatureServiceServer", locators = "localhost[10334]", port = 0)
@ComponentScan(basePackageClasses = AverageTemperatureFunction.class)
@EnableGemfireFunctions
@EnablePdx
@Import(GeodeConfiguration.class)
@SuppressWarnings("unused")
public class TemperatureServerApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(TemperatureServerApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Configuration
	@EnableLocator
	@EnableManager(start = true)
	@Profile("locator-manager")
	static class LocatorManagerConfiguration { }

}
