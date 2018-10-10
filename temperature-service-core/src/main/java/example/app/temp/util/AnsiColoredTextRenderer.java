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

import org.cp.elements.lang.Renderer;

import example.app.temp.event.TemperatureEvent;
import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;

/**
 * The AnsiColoredTextRenderer class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class AnsiColoredTextRenderer implements Renderer<String> {

	private final AnsiColor ansiColor;

	private AnsiColoredTextRenderer(AnsiColor ansiColor) {
		this.ansiColor = AnsiColor.nullSafeAnsiColor(ansiColor);
	}

	@Override
	public String render(String text) {
		return this.ansiColor.format(text);
	}

	public static final class Builder {

		private AnsiColor ansiColor = AnsiColor.useDefault();

		public Builder from(Temperature temperature) {

			this.ansiColor = temperature.isBoiling() ? AnsiColor.RED
				: temperature.isFreezing() ? AnsiColor.BLUE
				: AnsiColor.BLACK;

			return this;
		}

		public Builder from(TemperatureEvent temperatureEvent) {
			return from(temperatureEvent.getTemperatureReading());
		}

		public Builder from(TemperatureReading temperatureReading) {
			return from(temperatureReading.getTemperature());
		}

		public Builder using(AnsiColor ansiColor) {
			this.ansiColor = ansiColor;
			return this;
		}

		public Builder usingPlainText() {
			this.ansiColor = AnsiColor.PLAIN;
			return this;
		}

		public Renderer<String> build() {
			return new AnsiColoredTextRenderer(this.ansiColor);
		}
	}

	public enum AnsiColor {

		BLACK("\u001b[30m%s\u001b[0m"),
		BLUE("\u001b[34m%s\u001b[0m"),
		CYAN("\u001b[36m%s\u001b[0m"),
		GREEN("\u001b[32m%s\u001b[0m"),
		MAGENTA("\u001b[35m%s\u001b[0m"),
		RED("\u001b[31m%s\u001b[0m"),
		WHITE("\u001b[37m%s\u001b[0m"),
		YELLOW("\u001b[33m%s\u001b[0m"),
		PLAIN("%s");

		private final String coloredTextFormat;

		static AnsiColor nullSafeAnsiColor(AnsiColor ansiColor) {
			return ansiColor != null ? ansiColor : useDefault();
		}

		static AnsiColor useDefault() {
			return PLAIN;
		}

		AnsiColor(String coloredTextFormat) {
			this.coloredTextFormat = coloredTextFormat;
		}

		public String getColoredTextFormat() {
			return this.coloredTextFormat;
		}

		public String format(String text) {
			return String.format(getColoredTextFormat(), text);
		}
	}
}
