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
package example.app.temp;

import java.util.Scanner;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import example.app.temp.event.TemperatureEventHandler;
import example.app.temp.event.support.LoggingTemperatureEventHandler;
import example.app.temp.function.exe.AverageTemperatureFunctionExecution;
import example.app.temp.model.TemperatureReading;
import example.app.temp.repo.TemperatureReadingRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The TemperatureMonitorApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@ClientCacheApplication(subscriptionEnabled = true)
@EnableCachingDefinedRegions
@EnableClusterConfiguration
@EnableContinuousQueries
@EnableEntityDefinedRegions(basePackageClasses = TemperatureReading.class)
@EnableGemfireCaching
@EnableGemfireFunctionExecutions(basePackageClasses = AverageTemperatureFunctionExecution.class)
@EnableGemfireRepositories(basePackageClasses = TemperatureReadingRepository.class)
@EnablePdx
@EnableScheduling
@Slf4j
@SuppressWarnings("unused")
public class TemperatureMonitorApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(TemperatureMonitorApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);

		new Scanner(System.in).next();
	}

	@Bean
	TemperatureEventHandler loggingTemperatureEventHandler() {
		return new LoggingTemperatureEventHandler(log);
	}
}
