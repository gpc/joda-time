package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTimeZone

class JodaTimeUtils {

	static withDateTimeZone(DateTimeZone zone, Closure yield) {
		final realZone = DateTimeZone.'default'
		try {
			DateTimeZone.'default' = zone
			yield()
		} finally {
			DateTimeZone.'default' = realZone
		}
	}

}