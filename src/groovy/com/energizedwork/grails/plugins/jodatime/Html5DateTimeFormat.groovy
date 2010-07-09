package com.energizedwork.grails.plugins.jodatime

import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.format.DateTimePrinter

/**
 * Provides a set of DateTimeFormatters that parse and format correctly for the various HTML5 date & time input types.
 */
class Html5DateTimeFormat {

	static DateTimeFormatter month() {
		DateTimeFormat.forPattern("yyyy-MM")
	}

	static DateTimeFormatter week() {
		DateTimeFormat.forPattern("xxxx-'W'ww")
	}

	static DateTimeFormatter date() {
		ISODateTimeFormat.date()
	}

	static DateTimeFormatter time() {
		new DateTimeFormatterBuilder()
				.appendPattern("HH:mm")
				.appendOptional(DateTimeFormat.forPattern(":ss").getParser())
				.appendOptional(DateTimeFormat.forPattern(".SSS").getParser())
				.append(DateTimeFormat.forPattern(":ss.SSS").getPrinter() as DateTimePrinter) // the cast is required or dispatch of append method gets confused
				.toFormatter()
	}

	static DateTimeFormatter datetimeLocal() {
		new DateTimeFormatterBuilder()
				.append(date())
				.appendLiteral("T")
				.append(time())
				.toFormatter()
	}

	static DateTimeFormatter datetime() {
		new DateTimeFormatterBuilder()
				.append(datetimeLocal())
				.appendTimeZoneOffset("Z", true, 2, 2)
				.toFormatter()
				.withOffsetParsed()
	}

	private Html5DateTimeFormat() {}

}
