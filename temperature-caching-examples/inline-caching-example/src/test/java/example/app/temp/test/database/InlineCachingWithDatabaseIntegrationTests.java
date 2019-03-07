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
package example.app.temp.test.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import example.app.temp.geode.cache.RepositoryCacheLoader;
import example.app.temp.geode.cache.RepositoryCacheWriter;
import example.app.temp.model.Temperature;
import example.app.temp.model.TemperatureReading;
import example.app.temp.model.TemperatureSensor;
import example.app.temp.model.Temperatures;
import example.app.temp.repo.TemperatureReadingRepository;

/**
 * The InlineCachingWithDatabaseIntegrationTests class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("database")
@SpringBootTest
@SuppressWarnings("unused")
public class InlineCachingWithDatabaseIntegrationTests {

  private static final String GEMFIRE_LOG_LEVEL = "warn";

  private static final TemperatureReading ROOM_TEMPERATURE_READING =
    TemperatureReading.of(Temperatures.ROOM_TEMPERATURE_FAHRENHEIT.getTemperature());

  @Autowired
  private GemfireTemplate temperatureReadingsTemplate;

  @Autowired
  private TemperatureReadingRepository repository;

  @Before
  public void setup() {

    assertThat(this.repository.count()).isEqualTo(0);
    assertThat(this.temperatureReadingsTemplate.getRegion()).hasSize(0);

    this.repository.save(ROOM_TEMPERATURE_READING);

    assertThat(this.repository.count()).isEqualTo(1);
    assertThat(this.repository.existsById(ROOM_TEMPERATURE_READING.getId())).isTrue();
    assertThat(this.repository.findById(ROOM_TEMPERATURE_READING.getId()).orElse(null)).isEqualTo(
      ROOM_TEMPERATURE_READING);
    assertThat(this.temperatureReadingsTemplate.<String, TemperatureReading>get(ROOM_TEMPERATURE_READING.getId())).isNull();
    assertThat(this.temperatureReadingsTemplate.containsKey(ROOM_TEMPERATURE_READING.getId())).isFalse();
    assertThat(this.temperatureReadingsTemplate.getRegion()).hasSize(0);
  }

  @Test
  public void cacheLoadsFromDatabase() {

    assertThat(this.temperatureReadingsTemplate.containsKey(ROOM_TEMPERATURE_READING.getId())).isFalse();
    assertThat(this.temperatureReadingsTemplate.<String, TemperatureReading>get(ROOM_TEMPERATURE_READING.getId()))
      .isEqualTo(ROOM_TEMPERATURE_READING);
  }

  @Test
  public void cacheWritesToDatabase() {

    TemperatureReading nineElevenDegreesFahrenheit = TemperatureReading.of(Temperature.of(911).fahrenheit());

    assertThat(this.temperatureReadingsTemplate.containsKey(nineElevenDegreesFahrenheit.getId())).isFalse();
    assertThat(this.repository.existsById(nineElevenDegreesFahrenheit.getId())).isFalse();

    this.temperatureReadingsTemplate.put(nineElevenDegreesFahrenheit.getId(), nineElevenDegreesFahrenheit);

    assertThat(this.temperatureReadingsTemplate.<String, TemperatureReading>get(nineElevenDegreesFahrenheit.getId()))
      .isEqualTo(nineElevenDegreesFahrenheit);
    assertThat(this.repository.findById(nineElevenDegreesFahrenheit.getId()).orElse(null))
      .isEqualTo(nineElevenDegreesFahrenheit);
  }

  @SpringBootApplication
  @PeerCacheApplication(logLevel = GEMFIRE_LOG_LEVEL)
  @EntityScan(basePackageClasses = TemperatureSensor.class)
  @EnableJpaRepositories(basePackageClasses = TemperatureReadingRepository.class)
  static class GeodeConfiguration {

    @Bean("TemperatureReadings")
    public LocalRegionFactoryBean<String, TemperatureReading> temperatureReadingsRegion(GemFireCache gemfireCache,
        TemperatureReadingRepository repository) {

      LocalRegionFactoryBean<String, TemperatureReading> localRegion = new LocalRegionFactoryBean<>();

      localRegion.setCache(gemfireCache);
      localRegion.setCacheLoader(new RepositoryCacheLoader<>(repository));
      localRegion.setCacheWriter(new RepositoryCacheWriter<>(repository));
      localRegion.setClose(false);
      localRegion.setPersistent(false);

      return localRegion;
    }

    @Bean
    public GemfireTemplate temperatureReadingsTemplate(GemFireCache gemfireCache) {
      return new GemfireTemplate(gemfireCache.getRegion("/TemperatureReadings"));
    }
  }
}
