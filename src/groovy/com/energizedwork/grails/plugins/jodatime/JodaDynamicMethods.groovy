package com.energizedwork.grails.plugins.jodatime

import org.joda.time.*

class JodaDynamicMethods {

	static void registerDynamicMethods() {
		// consistency with format(String) method Groovy adds to java.util.Date
		ReadableInstant.metaClass.format = {String pattern ->
			delegate.toString(pattern)
		}
		ReadablePartial.metaClass.format = {String pattern ->
			delegate.toString(pattern)
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