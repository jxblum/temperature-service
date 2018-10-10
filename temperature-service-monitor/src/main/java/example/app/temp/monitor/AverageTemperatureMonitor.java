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
package example.app.temp.monitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import example.app.temp.event.AbstractTemperatureEventPublisher;
import example.app.temp.event.support.AverageTemperatureEvent;
import example.app.temp.model.TemperatureReading;
import example.app.temp.service.TemperatureService;

/**
 * The AverageTemperatureMonitor class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Component
@SuppressWarnings("unused")
public class AverageTemperatureMonitor extends AbstractTemperatureEventPublisher {

	private final TemperatureService temperatureService;

	public AverageTemperatureMonitor(TemperatureService temperatureService) {

		Assert.notNull(temperatureService, "TemperatureService is required");

		this.temperatureService = temperatureService;
	}

	@Scheduled(fixedRateString = "${example.app.temp.monitor.reading.schedule.rate:3000}")
	public void computeAverageTemperature() {

		TemperatureReading averageTemperatureReading =
			this.temperatureService.computeAverageTemperature("avgTemp");

		publish(new AverageTemperatureEvent(this, averageTemperatureReading,
			this.temperatureService.isCacheMiss()));
	}
}
