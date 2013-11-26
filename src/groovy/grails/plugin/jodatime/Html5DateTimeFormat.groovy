package grails.plugin.jodatime

import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.DateTimeParser
import org.joda.time.format.ISODateTimeFormat

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
		DateTimeParser[] parsers = [
				DateTimeFormat.forPattern( "HH:mm" ).getParser(),
				DateTimeFormat.forPattern( "HH:mm:ss" ).getParser(),
				DateTimeFormat.forPattern( "HH:mm:ss.SSS" ).getParser()
		]
		new DateTimeFormatterBuilder()
				.append(DateTimeFormat.forPattern("HH:mm:ss.SSS").getPrinter(), parsers)
				.toFormatter()
	}

	static DateTimeFormatter timeShort() {
		new DateTimeFormatterBuilder()
				.appendPattern("HH:mm")
				.toFormatter()
	}

	static DateTimeFormatter datetimeLocal() {
		new DateTimeFormatterBuilder()
				.append(date())
				.appendLiteral("T")
				.append(time())
				.toFormatter()
	}

	static DateTimeFormatter datetimeLocalShort() {
		new DateTimeFormatterBuilder()
				.append(date())
				.appendLiteral("T")
				.append(timeShort())
				.toFormatter()
	}

	static DateTimeFormatter datetime() {
		new DateTimeFormatterBuilder()
				.append(datetimeLocal())
				.appendTimeZoneOffset("Z", true, 2, 2)
				.toFormatter()
				.withOffsetParsed()
	}

	static DateTimeFormatter datetimeShort() {
		new DateTimeFormatterBuilder()
				.append(datetimeLocalShort())
				.appendTimeZoneOffset("Z", true, 2, 2)
				.toFormatter()
				.withOffsetParsed()
	}

	private Html5DateTimeFormat() {}

}
