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
package example.app.temp.function.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;
import org.cp.elements.lang.MathUtils;
import org.springframework.stereotype.Component;

/**
 * The AverageTemperatureResultCollector class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Component("AverageTemperatureResultCollector")
public class AverageTemperatureResultCollector implements ResultCollector<Double, Iterable<Optional<Double>>> {

	private volatile Double result;

	private List<Double> allAverageTemperatures = Collections.synchronizedList(new ArrayList<Double>());

	@Override
	public Iterable<Optional<Double>> getResult() throws FunctionException {
		return Collections.singletonList(Optional.ofNullable(this.result));
	}

	@Override
	public Iterable<Optional<Double>> getResult(long timeout, TimeUnit unit) throws FunctionException, InterruptedException {

		final long waitTimeout = System.currentTimeMillis() + unit.toMillis(timeout);
		final long waitDuration = waitTimeout / 4;

		while (this.result == null && System.currentTimeMillis() < waitTimeout) {
			TimeUnit.MILLISECONDS.timedWait(this, waitDuration);
		}

		return Collections.singletonList(Optional.ofNullable(this.result));
	}

	@Override
	public void addResult(DistributedMember memberID, Double resultOfSingleExecution) {
		Optional.ofNullable(resultOfSingleExecution).ifPresent(this.allAverageTemperatures::add);
	}

	@Override
	public void endResults() {
		this.result = MathUtils.roundToNearestTenth(this.allAverageTemperatures.stream()
			.collect(Collectors.averagingDouble(value -> value)));
	}

	@Override
	public void clearResults() {
		this.allAverageTemperatures.clear();
	}
}
