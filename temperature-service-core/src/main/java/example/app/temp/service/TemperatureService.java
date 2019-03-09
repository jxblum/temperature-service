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
package example.app.temp.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import example.app.temp.function.exe.AverageTemperatureFunctionExecution;
import example.app.temp.function.support.AverageTemperatureFunctionUtils;
import example.app.temp.model.TemperatureReading;
import example.app.temp.repo.TemperatureReadingRepository;

/**
 * The {@link TemperatureService} class is a Spring {@link Service} class used to manage
 * {@link TemperatureReading TemperatureReadings} and events associated with the sensors
 * and monitoring of temperatures.
 *
 * @author John Blum
 * @see example.app.temp.function.exe.AverageTemperatureFunctionExecution
 * @see example.app.temp.model.TemperatureReading
 * @see example.app.temp.repo.TemperatureReadingRepository
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.stereotype.Service
 * @since 1.0.0
 */
@Service("TemperatureService")
@SuppressWarnings("unused")
public class TemperatureService {

	private static final long TIME_TO_LIVE_EXPIRATION_MILLISECONDS = 15000L;

	private volatile long timestamp = System.currentTimeMillis();

	private final AtomicBoolean cacheMiss = new AtomicBoolean(false);

	@Autowired(required = false)
	private AverageTemperatureFunctionExecution averageTemperatureFunction;

	private final TemperatureReadingRepository temperatureReadingRepository;

	/**
	 * Constructs a new instance of {@link TemperatureService} initialized with the {@link TemperatureReadingRepository}
	 * used to perform data access operations on {@link TemperatureReading TemperatureReadings}.
	 *
	 * @param temperatureReadingRepository {@link TemperatureReadingRepository} used to perform data access operations
	 * on {@link TemperatureReading TemperatureReadings}.
	 * @throws IllegalArgumentException if {@link TemperatureReadingRepository} is {@literal null}.
	 * @see example.app.temp.repo.TemperatureReadingRepository
	 */
	public TemperatureService(TemperatureReadingRepository temperatureReadingRepository) {

		Assert.notNull(temperatureReadingRepository, "TemperatureReadingRepository is required");

		this.temperatureReadingRepository = temperatureReadingRepository;
	}

	protected AverageTemperatureFunctionExecution getAverageTemperatureFunction() {

		Assert.state(this.averageTemperatureFunction != null,
			"No AverageTemperatureFunctionExecution was configured");

		return this.averageTemperatureFunction;
	}

	@Cacheable(cacheNames = "AverageTemperature")
	public TemperatureReading computeAverageTemperature(String surrogateKey) {

		this.cacheMiss.set(true);

		Object averageTemperature = getAverageTemperatureFunction().averageTemperature();

		return AverageTemperatureFunctionUtils.asTemperatureReading(averageTemperature);
	}

	public Optional<TemperatureReading> load(UUID id) {
		return this.temperatureReadingRepository.findById(id);
	}

	@CacheEvict(cacheNames = "AverageTemperature", allEntries = true, beforeInvocation = true,
		condition = "T(System).currentTimeMillis() - @TemperatureService.getTimestamp() > "
			+ TIME_TO_LIVE_EXPIRATION_MILLISECONDS)
	public TemperatureReading record(TemperatureReading temperatureReading) {

		resetTimestamp();

		return this.temperatureReadingRepository.save(temperatureReading);
	}

	public boolean isCacheMiss() {
		return this.cacheMiss.compareAndSet(true, false);
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	private void resetTimestamp() {

		long currentTimestamp = System.currentTimeMillis();

		if (currentTimestamp - getTimestamp() > TIME_TO_LIVE_EXPIRATION_MILLISECONDS) {
			this.timestamp = currentTimestamp;
		}
	}
}
