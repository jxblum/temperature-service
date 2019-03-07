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
package example.app.temp.geode.cache;

import java.util.function.Supplier;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.CacheRuntimeException;
import org.apache.geode.cache.LoaderHelper;
import org.springframework.data.repository.CrudRepository;

import example.app.temp.geode.cache.support.RepositoryCacheLoaderWriterSupport;

/**
 * A {@link CacheLoader} implementation backed by a Spring Data {@link CrudRepository} used to load an entity
 * from an external data source.
 *
 * @author John Blum
 * @see org.apache.geode.cache.CacheLoader
 * @see org.springframework.data.repository.CrudRepository
 * @see org.springframework.geode.cache.support.CacheLoaderSupport
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class RepositoryCacheLoader<T, ID> extends RepositoryCacheLoaderWriterSupport<T, ID> {

  public RepositoryCacheLoader(CrudRepository<T, ID> repository) {
    super(repository);
  }

  @Override
  public T load(LoaderHelper<ID, T> helper) throws CacheLoaderException {
    return getRepository().findById(helper.getKey()).orElse(null);
  }

  @Override
  protected CacheRuntimeException newCacheRuntimeException(Supplier<String> messageSupplier, Throwable cause) {
    return new CacheLoaderException(messageSupplier.get(), cause);
  }
}
