package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

class DateTimeZoneTagLib {

	static namespace = "joda"

	static final ZONE_FORMATTER = DateTimeFormat.forPattern("ZZ")

	/**
	 * A helper tag for creating DateTimeZone selects
	 * e.g. <joda:dateTimeZoneSelect name="myTimeZone" value="${tz}" />
	 */
	def dateTimeZoneSelect = {attrs ->
		attrs.from = DateTimeZone.getAvailableIDs();
		attrs.value = attrs.value?.ID ?: DateTimeZone.default.ID
		def time = DateTimeUtils.currentTimeMillis()

		// set the option value as a closure that formats the DateTimeZone for display
		attrs.optionValue = {
			DateTimeZone tz = DateTimeZone.forID(it)
			def offset = ZONE_FORMATTER.withZone(tz).print(time)
			return "$it $offset"
		}

		// use generic select
		out << select(attrs)
	}

}
