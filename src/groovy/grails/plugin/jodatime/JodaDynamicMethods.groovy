/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.jodatime

import org.joda.time.*
import static org.joda.time.DateTimeFieldType.*
import grails.plugin.jodatime.util.DateTimeRange

class JodaDynamicMethods {

	static void registerDynamicMethods() {
		// consistency with format(String) method Groovy adds to java.util.Date
		ReadableInstant.metaClass.format = {String pattern ->
			delegate.toString(pattern)
		}
		ReadablePartial.metaClass.format = {String pattern ->
			delegate.toString(pattern)
		}

		// next and previous
		ReadableInstant.metaClass.next = {->
			JodaDynamicMethods.next(delegate)
		}
		ReadablePartial.metaClass.next = {->
			JodaDynamicMethods.next(delegate)
		}
		ReadableInstant.metaClass.previous = {->
			JodaDynamicMethods.previous(delegate)
		}
		ReadablePartial.metaClass.previous = {->
			JodaDynamicMethods.previous(delegate)
		}

		// compatibility with Groovy operators where JodaTime method name conventions differ
		[Days, Hours, Minutes, Months, Seconds, Weeks, Years].each { clazz ->
			clazz.metaClass.negative = {
				delegate.negated()
			}
			clazz.metaClass.multiply = { int scalar ->
				delegate.multipliedBy(scalar)
			}
			clazz.metaClass.div = { int divisor ->
				delegate.dividedBy(divisor)
			}
		}

		// scoped current time overriding
		DateTimeUtils.metaClass.static.withCurrentMillisFixed = { long fixed, Closure yield ->
			JodaDynamicMethods.withCurrentMillisFixed(fixed, yield)
		}
		DateTimeUtils.metaClass.static.withCurrentMillisOffset = { long offset, Closure yield ->
			JodaDynamicMethods.withCurrentMillisOffset(offset, yield)
		}

		// range extensions
		Range.metaClass.step = { DurationFieldType increment ->
			JodaDynamicMethods.step(delegate, 1, increment)
		}
		Range.metaClass.step = { int step, DurationFieldType increment ->
			JodaDynamicMethods.step(delegate, step, increment)
		}
		Range.metaClass.step = { DurationFieldType increment, Closure closure ->
			JodaDynamicMethods.step(delegate, 1, increment, closure)
		}
		Range.metaClass.step = { int step, DurationFieldType increment, Closure closure ->
			JodaDynamicMethods.step(delegate, step, increment, closure)
		}
	}

	static ReadablePartial next(ReadablePartial delegate) {
		if (delegate.isSupported(dayOfMonth())) {
			delegate.plusDays(1)
		} else if (delegate.isSupported(hourOfDay())) {
			delegate.plusHours(1)
		} else if (delegate.isSupported(monthOfYear())) {
			delegate.plusMonths(1)
		}
	}

	static ReadableInstant next(ReadableInstant delegate) {
		if (delegate.isSupported(dayOfMonth())) {
			delegate.plusDays(1)
		} else if (delegate.isSupported(hourOfDay())) {
			delegate.plusHours(1)
		} else if (delegate.isSupported(monthOfYear())) {
			delegate.plusMonths(1)
		}
	}

	static ReadablePartial previous(ReadablePartial delegate) {
		if (delegate.isSupported(dayOfMonth())) {
			delegate.minusDays(1)
		} else if (delegate.isSupported(hourOfDay())) {
			delegate.minusHours(1)
		} else if (delegate.isSupported(monthOfYear())) {
			delegate.minusMonths(1)
		}
	}

	static ReadableInstant previous(ReadableInstant delegate) {
		if (delegate.isSupported(dayOfMonth())) {
			delegate.minusDays(1)
		} else if (delegate.isSupported(hourOfDay())) {
			delegate.minusHours(1)
		} else if (delegate.isSupported(monthOfYear())) {
			delegate.minusMonths(1)
		}
	}

	static withCurrentMillisFixed(long fixed, Closure yield) {
		try {
			DateTimeUtils.setCurrentMillisFixed fixed
			return yield()
		} finally {
			DateTimeUtils.setCurrentMillisSystem()
		}
	}

	static withCurrentMillisOffset(long offset, Closure yield) {
		try {
			DateTimeUtils.setCurrentMillisOffset offset
			return yield()
		} finally {
			DateTimeUtils.setCurrentMillisSystem()
		}
	}

	static List step(Range self, int step, DurationFieldType increment) {
		if (self.from instanceof ReadablePartial || self.from instanceof ReadableInstant) {
			new DateTimeRange(increment, self.from, self.to).step(step)
		} else {
			throw new MissingMethodException("step", self.getClass(), [increment] as Object[])
		}
	}

	static void step(Range self, int step, DurationFieldType increment, Closure closure) {
		if (self.from instanceof ReadablePartial || self.from instanceof ReadableInstant) {
			new DateTimeRange(increment, self.from, self.to).step(step).each(closure)
		} else {
			throw new MissingMethodException("step", self.getClass(), [increment] as Object[])
		}
	}

}