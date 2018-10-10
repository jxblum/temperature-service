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
package example.app.temp.event.support;

import org.cp.elements.lang.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

import example.app.temp.event.TemperatureEvent;
import example.app.temp.event.TemperatureEventHandler;
import example.app.temp.util.AnsiColoredTextRenderer;

/**
 * The LoggingTemperatureEventHandler class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class LoggingTemperatureEventHandler implements TemperatureEventHandler {

	private final Logger logger;

	public LoggingTemperatureEventHandler() {
		this(LoggerFactory.getLogger(LoggingTemperatureEventHandler.class));
	}

	public LoggingTemperatureEventHandler(Logger logger) {

		Assert.notNull(logger, "Logger is required");

		this.logger = logger;
	}

	@EventListener(classes = TemperatureEvent.class)
	public void handle(TemperatureEvent temperatureEvent) {

		String temperatureEventString = temperatureEvent.toString();

		Renderer<String> ansiTextRenderer = new AnsiColoredTextRenderer.Builder()
			.from(temperatureEvent)
			//.usingPlainText()
			.build();

		this.logger.info(ansiTextRenderer.render(temperatureEventString));
	}
}
