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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import example.app.temp.event.TemperatureEventHandler;
import example.app.temp.event.support.LoggingTemperatureEventHandler;
import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;
import example.app.temp.repo.TemperatureReadingRepository;
import example.app.temp.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;

/**
 * The TemperatureSensorApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@ClientCacheApplication(name = "TemperatureSensorApplication")
@EnableClusterConfiguration
@EnableClusterDefinedRegions
@EnableEntityDefinedRegions(basePackageClasses = TemperatureReading.class)
@EnableGemfireCaching
@EnableGemfireRepositories(basePackageClasses = TemperatureReadingRepository.class)
@EnablePdx
@EnableScheduling
@Slf4j
@SuppressWarnings("unused")
public class TemperatureSensorApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(TemperatureSensorApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(TemperatureService temperatureService) {

		return args -> {

			TemperatureReading temperatureReading = TemperatureReading.of(Temperature.of(72.0d));

			assertThat(temperatureReading).isNotNull();
			assertThat(temperatureReading.getTemperature()).isNotNull();
			assertThat(temperatureReading.getTemperature().getMeasurement()).isEqualTo(72.0d);
			assertThat(temperatureReading.getTemperature().getScale()).isEqualTo(Temperature.Scale.getDefault());
			assertThat(temperatureReading.getTimestamp()).isLessThanOrEqualTo(System.currentTimeMillis());

			TemperatureReading expected = temperatureService.record(temperatureReading);

			assertThat(expected).isNotNull();

			Optional<TemperatureReading> actual = temperatureService.load(expected.getId());

			assertThat(actual.orElse(null)).isEqualTo(expected);
			assertThat(actual.orElse(null)).isNotSameAs(expected);

			System.err.println("*** RUNNING ***");
		};
	}

	@Bean
	TemperatureEventHandler loggingTemperatureEventHandler() {
		return new LoggingTemperatureEventHandler(log);
	}
}
