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

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.GemFireCache;
import org.cp.elements.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
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
import example.app.temp.repo.TemperatureSensorRepository;
import example.app.temp.util.TemperatureReadingComparator;

/**
 * Integration tests for inline caching using Apache Geode / Pivotal GemFire with an RDBMS
 * along with Apache Cassandra.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.GemFireCache
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see org.springframework.data.gemfire.config.annotation.PeerCacheApplication
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @see org.springframework.test.context.junit4.SpringRunner
 * @see example.app.temp.geode.cache.RepositoryCacheLoader
 * @see example.app.temp.geode.cache.RepositoryCacheWriter
 * @see example.app.temp.model.Temperature
 * @see example.app.temp.model.TemperatureReading
 * @see example.app.temp.model.TemperatureSensor
 * @see example.app.temp.repo.TemperatureSensorRepository
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("database")
@SpringBootTest
@SuppressWarnings("unused")
public class InlineCachingWithDatabaseIntegrationTests {

  private static final String GEMFIRE_LOG_LEVEL = "warn";

  @Autowired
  private GemfireTemplate temperatureSensorsTemplate;

  private volatile TemperatureSensor sensorA;

  @Autowired
  private TemperatureSensorRepository repository;

  protected void assertEquals(TemperatureSensor actualSensor, TemperatureSensor expectedSensor) {

    assertThat(actualSensor).isNotNull();
    assertThat(actualSensor.getId()).isEqualTo(expectedSensor.getId());
    assertThat(actualSensor.getLocation()).isEqualTo(expectedSensor.getLocation());
    assertThat(actualSensor.getName()).isEqualTo(expectedSensor.getName());

    List<TemperatureReading> actualTemperatureReadings =
      CollectionUtils.nullSafeCollection(actualSensor.getTemperatureReadings()).stream()
        .sorted(TemperatureReadingComparator.ORDER_BY_TIME_TEMP_ASC)
        .collect(Collectors.toList());

    List<TemperatureReading> expectedTemperatureReadings =
      CollectionUtils.nullSafeCollection(expectedSensor.getTemperatureReadings()).stream()
        .sorted(TemperatureReadingComparator.ORDER_BY_TIME_TEMP_ASC)
        .collect(Collectors.toList());

    assertThat(actualTemperatureReadings).hasSize(expectedTemperatureReadings.size());

    for (int index = expectedTemperatureReadings.size(); --index >= 0; ) {
      assertThat(actualTemperatureReadings.get(index)).isEqualTo(expectedTemperatureReadings.get(index));
    }
  }

  @Before
  public void setup() {

    if (this.sensorA == null) {

      this.sensorA = this.repository.findByName("A");

      assertThat(this.sensorA).isNotNull();
      assertThat(this.sensorA.getId()).isNotNull();
      assertThat(this.sensorA.getLocation()).isNull();
      assertThat(this.sensorA.getName()).isEqualTo("A");

      List<Temperature> sensorTemperatureReadings =
        CollectionUtils.nullSafeCollection(this.sensorA.getTemperatureReadings()).stream()
          .sorted(TemperatureReadingComparator.ORDER_BY_TIME_TEMP_ASC)
          .map(TemperatureReading::getTemperature)
          .collect(Collectors.toList());

      assertThat(sensorTemperatureReadings).hasSize(2);
      assertThat(sensorTemperatureReadings).contains(
        Temperatures.ROOM_TEMPERATURE_FAHRENHEIT.getTemperature(),
        Temperature.of(0.0d).celsius()
      );
    }
  }

  @Test
  public void cacheLoadsFromDatabase() {

    assertThat(this.temperatureSensorsTemplate.containsKey(sensorA.getId())).isFalse();
    assertEquals(this.temperatureSensorsTemplate.get(sensorA.getId()), sensorA);
  }

  @Test
  public void cacheWritesToDatabase() {

    TemperatureReading nineElevenDegreesFahrenheit = TemperatureReading.of(Temperature.of(911).fahrenheit());

    TemperatureSensor localSensor = TemperatureSensor.create("B")
      .add(nineElevenDegreesFahrenheit)
      .identifiedBy(this.sensorA.getId() + 1L);

    assertThat(this.temperatureSensorsTemplate.containsKey(localSensor.getId())).isFalse();
    assertThat(this.repository.existsById(localSensor.getId())).isFalse();

    this.temperatureSensorsTemplate.put(localSensor.getId(), localSensor);

    assertThat(this.temperatureSensorsTemplate.<Long, TemperatureSensor>get(localSensor.getId())).isEqualTo(localSensor);
    assertEquals(this.repository.findById(localSensor.getId()).orElse(null), localSensor);
  }

  @PeerCacheApplication(logLevel = GEMFIRE_LOG_LEVEL)
  static class GeodeConfiguration {

    @Bean("TemperatureSensors")
    public LocalRegionFactoryBean<Long, TemperatureSensor> temperatureSensorsRegion(GemFireCache gemfireCache,
        TemperatureSensorRepository repository) {

      LocalRegionFactoryBean<Long, TemperatureSensor> temperatureSensorsRegion = new LocalRegionFactoryBean<>();

      temperatureSensorsRegion.setCache(gemfireCache);
      temperatureSensorsRegion.setCacheLoader(new RepositoryCacheLoader<>(repository));
      temperatureSensorsRegion.setCacheWriter(new RepositoryCacheWriter<>(repository));
      temperatureSensorsRegion.setClose(false);
      temperatureSensorsRegion.setPersistent(false);

      return temperatureSensorsRegion;
    }

    @Bean
    public GemfireTemplate temperatureSensorsTemplate(GemFireCache gemfireCache) {
      return new GemfireTemplate(gemfireCache.getRegion("/TemperatureSensors"));
    }
  }

  @Profile("database")
  @SpringBootApplication(exclude = {
    CassandraAutoConfiguration.class,
    CassandraDataAutoConfiguration.class
  })
  @EntityScan(basePackageClasses = TemperatureSensor.class)
  @EnableJpaRepositories(basePackageClasses = TemperatureSensorRepository.class)
  static class DatabaseConfiguration { }

  @Profile("cassandra")
  @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
  @EnableCassandraRepositories(basePackageClasses = TemperatureSensorRepository.class)
  static class CassandraConfiguration { }

}
