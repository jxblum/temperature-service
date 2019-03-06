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
package example.app.temp.util.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility methods for using Java {@link Function Functions}.
 *
 * @author John Blum
 * @see java.util.function.Function
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class FunctionUtils {

  public static <T, R> Function<T, R> toNullReturningFunction(Consumer<T> consumer) {

    return object -> {
      consumer.accept(object);
      return null;
    };
  }
}
