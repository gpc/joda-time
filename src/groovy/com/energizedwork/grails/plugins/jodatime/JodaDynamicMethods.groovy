package com.energizedwork.grails.plugins.jodatime

import org.joda.time.format.DateTimeFormat
import org.joda.time.*

class JodaDynamicMethods {

	static void registerDynamicMethods() {
		ReadableInstant.metaClass.format = {String pattern ->
			JodaDynamicMethods.format(delegate, pattern)
		}
		ReadablePartial.metaClass.format = {String pattern ->
			JodaDynamicMethods.format(delegate, pattern)
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

		DateTimeUtils.metaClass.static.withCurrentMillisFixed = { long fixed, Closure yield ->
			JodaDynamicMethods.withCurrentMillisFixed(fixed, yield)
		}
		DateTimeUtils.metaClass.static.withCurrentMillisOffset = { long offset, Closure yield ->
			JodaDynamicMethods.withCurrentMillisOffset(offset, yield)
		}
	}

	/**
	 * Formats a {@link ReadableInstant} or {@link ReadablePartial} using the specified pattern.
	 * @param self a {@link ReadableInstant} or {@link ReadablePartial}
	 * @param pattern a valid date/time format pattern {@see DateTimeFormat}
	 * @return the formatted date/time value
	 */
	static String format(self, String pattern) {
		DateTimeFormat.forPattern(pattern).print(self)
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

}