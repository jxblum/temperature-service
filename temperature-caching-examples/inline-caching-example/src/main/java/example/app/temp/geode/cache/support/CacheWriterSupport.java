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
package example.app.temp.geode.cache.support;

import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

/**
 * Class supporting the implementation of Apache Geode / Pivotal GemFire {@link CacheWriter CacheWriters}.
 *
 * @author John Blum
 * @see org.apache.geode.cache.CacheWriter
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class CacheWriterSupport<K, V> implements CacheWriter<K, V> {

  @Override
  public void beforeCreate(EntryEvent<K, V> event) throws CacheWriterException { }

  @Override
  public void beforeUpdate(EntryEvent<K, V> event) throws CacheWriterException { }

  @Override
  public void beforeDestroy(EntryEvent<K, V> event) throws CacheWriterException { }

  @Override
  public void beforeRegionClear(RegionEvent<K, V> event) throws CacheWriterException { }

  @Override
  public void beforeRegionDestroy(RegionEvent<K, V> event) throws CacheWriterException { }

}
