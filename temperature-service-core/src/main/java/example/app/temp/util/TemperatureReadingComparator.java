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

import java.util.Comparator;

import org.cp.elements.util.ComparatorResultBuilder;

import example.app.temp.model.TemperatureReading;

/**
 * Enumeration of {@link Comparator} implementations to compare and order {@link TemperatureReading TemperatureReadings}
 * by different criteria.
 *
 * @author John Blum
 * @see java.util.Comparator
 * @see org.cp.elements.util.ComparatorResultBuilder
 * @since 1.0.0
 */
public enum TemperatureReadingComparator implements Comparator<TemperatureReading> {

  ORDER_BY_TIME_TEMP_ASC {

    @Override @SuppressWarnings("unchecked")
    public int compare(TemperatureReading readingOne, TemperatureReading readingTwo) {

      return ComparatorResultBuilder.<Comparable>create()
        .doCompare(readingOne.getTimestamp(), readingTwo.getTimestamp())
        .doCompare(readingOne.getTemperature().getMeasurement(), readingTwo.getTemperature().getMeasurement())
        .build();
    }
  };
}
