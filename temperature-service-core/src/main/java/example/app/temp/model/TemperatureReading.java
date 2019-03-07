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
package example.app.temp.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

/**
 * The {@link TemperatureReading} class is an Abstract Data Type (ADT) modeling a temperature reading,
 * which may have originated from a sensor or thermometer.
 *
 * @author John Blum
 * @see java.util.UUID
 * @see lombok
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @since 1.0.0
 */
@Data
@Entity
@Region("TemperatureReadings")
@Table(name = "temperature_readings")
@EqualsAndHashCode(of = { "timestamp", "temperature" })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@ToString
@SuppressWarnings("unused")
public class TemperatureReading {

	@Id
	@javax.persistence.Id
	private String id = UUID.randomUUID().toString();

	@Column(name = "date_time")
	private Long timestamp = System.currentTimeMillis();

	@Embedded @NonNull
	private Temperature temperature;

}
