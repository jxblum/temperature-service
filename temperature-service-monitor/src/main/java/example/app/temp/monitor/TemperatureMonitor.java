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

import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

import example.app.temp.event.AbstractTemperatureEventPublisher;
import example.app.temp.event.support.BoilingTemperatureEvent;
import example.app.temp.event.support.FreezingTemperatureEvent;

/**
 * The TemperatureMonitor class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Component
@SuppressWarnings("unused")
public class TemperatureMonitor extends AbstractTemperatureEventPublisher {

	@ContinuousQuery(name = "BoilingTemperatureMonitor",
		query = "SELECT * FROM /TemperatureReadings WHERE temperature.measurement >= 212.0")
	public void boilingTemperatureReadings(CqEvent event) {
		publish(event, temperatureReading -> new BoilingTemperatureEvent(this, temperatureReading));
	}

	@ContinuousQuery(name = "FreezingTemperatureMonitor",
		query = "SELECT * FROM /TemperatureReadings WHERE temperature.measurement <= 32.0")
	public void freezingTemperatureReadings(CqEvent event) {
		publish(event, temperatureReading -> new FreezingTemperatureEvent(this, temperatureReading));
	}
}
