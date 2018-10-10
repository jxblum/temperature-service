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
package example.app.temp.event;

import java.util.Optional;
import java.util.function.Function;

import org.apache.geode.cache.query.CqEvent;
import org.cp.elements.lang.Assert;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import example.app.temp.model.TemperatureReading;

/**
 * The AbstractTemperatureEventPublisher class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public abstract class AbstractTemperatureEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	@Override @SuppressWarnings("all")
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	protected ApplicationEventPublisher getApplicationEventPublisher() {

		Assert.state(this.applicationEventPublisher != null,
			"No ApplicationEventPublisher was configured");

		return this.applicationEventPublisher;
	}

	protected void publish(TemperatureEvent event) {
		getApplicationEventPublisher().publishEvent(event);
	}

	protected void publish(CqEvent event, Function<TemperatureReading, TemperatureEvent> newTemperatureEventFunction) {

		Optional.ofNullable(event)
			.map(CqEvent::getNewValue)
			.filter(TemperatureReading.class::isInstance)
			.map(TemperatureReading.class::cast)
			.map(newTemperatureEventFunction)
			.ifPresent(getApplicationEventPublisher()::publishEvent);
	}
}
