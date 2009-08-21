package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeUtils

class DateTimeZoneTagLib {

	static namespace = "joda"

	static final ZONE_FORMATTER = DateTimeFormat.forPattern("ZZZ ZZ")

	/**
	 * A helper tag for creating DateTimeZone selects
	 * e.g. <joda:zoneSelect name="myTimeZone" value="${tz}" />
	 */
	def zoneSelect = {attrs ->
		attrs.from = DateTimeZone.getAvailableIDs();
		attrs.value = attrs.value?.ID ?: DateTimeZone.default.ID
		def time = DateTimeUtils.currentTimeMillis()

		// set the option value as a closure that formats the DateTimeZone for display
		attrs.optionValue = {
			DateTimeZone tz = DateTimeZone.forID(it)
			return ZONE_FORMATTER.withZone(tz).print(time)
		}

		// use generic select
		out << select(attrs)
	}

}
