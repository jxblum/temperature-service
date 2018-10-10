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
package example.app.temp.util;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.function.Function;

import org.cp.elements.lang.Renderer;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.util.Assert;

import example.app.temp.event.TemperatureEvent;
import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;

/**
 * The JansiConsoleOutputRenderer class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class JansiConsoleOutputRenderer implements Renderer<String> {

	static {
		AnsiConsole.systemInstall();
	}

	private final Function<String, String> ansiFunction;

	private JansiConsoleOutputRenderer(Function<String, String> ansiFunction) {

		Assert.notNull(ansiFunction, "The ANSI Function used to transform text messages is required");

		this.ansiFunction = ansiFunction;

		Runtime.getRuntime().addShutdownHook(new Thread(AnsiConsole::systemUninstall));
	}

	@Override
	public String render(String text) {
		return this.ansiFunction.apply(text);
	}

	public static final class Builder {

		private Function<String, String> ansiFunction = newNormalTemperatureAnsiFunction();

		public Builder from(Temperature temperature) {

			this.ansiFunction = temperature.isBoiling() ? newBoilingTemperatureAnsiFunction()
				: temperature.isFreezing() ? newFreezingTemperatureAnsiFunction()
				: newNormalTemperatureAnsiFunction();

			return this;
		}

		public Builder from(TemperatureEvent temperatureEvent) {
			return from(temperatureEvent.getTemperatureReading());
		}

		public Builder from(TemperatureReading temperatureReading) {
			return from(temperatureReading.getTemperature());
		}

		private Function<String, String> newBoilingTemperatureAnsiFunction() {
			return text -> ansi().fgRed().a(text).toString();
		}

		private Function<String, String> newFreezingTemperatureAnsiFunction() {
			return text -> ansi().fgBrightBlue().a(text).toString();
		}

		private Function<String, String> newNormalTemperatureAnsiFunction() {
			return text -> text;
		}

		public JansiConsoleOutputRenderer build() {
			return new JansiConsoleOutputRenderer(this.ansiFunction);
		}
	}
}
