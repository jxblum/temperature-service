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
package example.app.temp.repo;

import org.springframework.data.repository.CrudRepository;

import example.app.temp.model.TemperatureReading;

/**
 * The {@link TemperatureReadingRepository} interface is a Data Access Object (DAO) & Spring Data {@link CrudRepository}
 * providing basic CRUD and simply query data access operations for {@link TemperatureReading} objects.
 *
 * @author John Blum
 * @see java.lang.String
 * @see org.springframework.data.repository.CrudRepository
 * @see example.app.temp.model.TemperatureReading
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface TemperatureReadingRepository extends CrudRepository<TemperatureReading, String> {

}