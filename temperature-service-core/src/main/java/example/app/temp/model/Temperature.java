package example.app.temp.model;

import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The {@link Temperature} class is an Abstract Data Type (ADT) modeling a physical temperature containing
 * a {@link Double measurement} in a given {@link Scale}.
 *
 * @author John Blum
 * @since 1.0.0
 */
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@SuppressWarnings("unused")
public class Temperature {

	@Getter
	private final double measurement;

	@Getter
	private Scale scale = Scale.getDefault();

	@Transient
	public boolean isBoiling() {
		return getScale().isBoiling(getMeasurement());
	}

	@Transient
	public boolean isFreezing() {
		return getScale().isFreezing(getMeasurement());
	}

	@Transient
	public boolean isNormal() {
		return getScale().isNormal(getMeasurement());
	}

	public Temperature using(Scale scale) {
		this.scale = Scale.nullSafeScale(scale).assertValid(getMeasurement());
		return this;
	}

	@Override
	public String toString() {
		return String.format("%1$s %2$s", StringUtils.leftPad(String.valueOf(getMeasurement()), 6), getScale());
	}

	public enum Scale {

		CELSIUS(0.0d, 100.d, "°C"),
		FAHRENHEIT(32.0d, 212.0d, "°F"),
		KELVIN(273.15d, 373.15d, "K");

		private final double boilingTemperature;
		private final double freezingTemperature;

		private final String symbol;

		Scale(double freezingTemperature, double boilingTemperature, String symbol) {
			this.boilingTemperature = boilingTemperature;
			this.freezingTemperature = freezingTemperature;
			this.symbol = symbol;
		}

		public static Scale getDefault() {

			return Optional.of(Locale.getDefault())
				.filter(Locale.US::equals)
				.map(it -> Scale.FAHRENHEIT)
				.orElse(Scale.CELSIUS);
		}

		public static Scale nullSafeScale(Scale scale) {
			return scale != null ? scale : getDefault();
		}

		public Scale assertValid(double temperature) {

			Assert.isTrue(!KELVIN.equals(this) || temperature >= 0.0d,
				String.format("Temperature [%s] must be greater than equal to absolute 0 on the Kelvin Scale",
					temperature));

			return this;
		}

		public boolean isBoiling(double temperature) {
			return temperature >= this.boilingTemperature;
		}

		public boolean isFreezing(double temperature) {
			return temperature <= this.freezingTemperature;
		}

		public boolean isNormal(double temperature) {
			return !(isBoiling(temperature) || isFreezing(temperature));
		}

		public double getBoilingTemperature() {
			return this.boilingTemperature;
		}

		public double getFreezingTemperature() {
			return this.freezingTemperature;
		}

		public String getSymbol() {
			return this.symbol;
		}

		@Override
		public String toString() {
			return getSymbol();
		}
	}
}
