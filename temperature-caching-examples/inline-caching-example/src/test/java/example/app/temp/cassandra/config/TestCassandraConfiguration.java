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
package example.app.temp.cassandra.config;

import static org.cp.elements.lang.RuntimeExceptionsFactory.newRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.cp.elements.io.IOUtils;
import org.cp.elements.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The TestCassandraConfiguration class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public abstract class TestCassandraConfiguration extends AbstractCassandraConfiguration {

  private static final boolean CASSANDRA_METRICS_ENABLED = false;

  protected static final int CASSANDRA_DEFAULT_PORT = CassandraClusterFactoryBean.DEFAULT_PORT;

  private static final String CASSANDRA_DATA_CQL = "cassandra-data.cql";
  private static final String CASSANDRA_SCHEMA_CQL = "cassandra-schema.cql";
  private static final String CLUSTER_NAME = "TemperatureServiceCluster";
  private static final String KEYSPACE_NAME = "TemperatureService";

  @Nullable @Override
  protected String getClusterName() {
    return CLUSTER_NAME;
  }

  @NonNull @Override @SuppressWarnings("all")
  protected String getKeyspaceName() {
    return KEYSPACE_NAME;
  }

  @Override
  protected boolean getMetricsEnabled() {
    return CASSANDRA_METRICS_ENABLED;
  }

  @Override @SuppressWarnings("all")
  protected List<String> getStartupScripts() {

    List<String> startupScripts = new ArrayList<>(super.getStartupScripts());

    startupScripts.addAll(readLines(new ClassPathResource(CASSANDRA_SCHEMA_CQL)));
    startupScripts.addAll(readLines(new ClassPathResource(CASSANDRA_DATA_CQL)));

    return startupScripts;
  }

  private List<String> readLines(Resource resource) {

    BufferedReader resourceReader = null;

    try {

      resourceReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

      return resourceReader.lines()
        .filter(StringUtils::hasText)
        .collect(Collectors.toList());
    }
    catch (IOException cause) {
      throw newRuntimeException(cause, "Failed to read from Resource [%s]", resource);
    }
    finally {
      IOUtils.close(resourceReader);
    }
  }
}
