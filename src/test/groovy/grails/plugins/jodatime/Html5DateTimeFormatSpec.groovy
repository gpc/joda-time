package grails.plugins.jodatime

import grails.plugin.jodatime.Html5DateTimeFormat
import org.hamcrest.Matcher
import org.joda.time.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.hamcrest.Matchers.*
import static org.joda.time.DateTimeZone.UTC

@Unroll
class Html5DateTimeFormatSpec extends Specification {

	@Shared DateTime dateTime = new DateTime(2008, 10, 2, 2, 50, 43, 123, UTC)

	def "HTML5 '#format' format is printed correctly"() {
		expect: Html5DateTimeFormat."$format"().print(dateTime) == expected
		where:
		format          | expected
		"month"         | "2008-10"
		"week"          | "2008-W40"
		"date"          | "2008-10-02"
		"datetimeLocal" | "2008-10-02T02:50:43.123"
		"datetime"      | "2008-10-02T02:50:43.123Z"
		"time"          | "02:50:43.123"
	}
	
	def "HTML5 '#format' format parses '#value' correctly"() {
		when: def dt = Html5DateTimeFormat."$format"().parseDateTime(value)
		then: matcher.matches(dt) // TODO: proper hamcrest support coming to spock soon - currently this doesn't generate good output if it fails
		where:
		format          | value                           | matcher   
		"month"         | "2008-10"                       | allOf(year(2008), monthOfYear(10))
		"week"          | "2008-W40"                      | allOf(year(2008), weekOfWeekyear(40))
		"date"          | "2008-10-02"                    | allOf(year(2008), monthOfYear(10), dayOfMonth(2))
		"datetimeLocal" | "2008-10-02T02:50"              | allOf(year(2008), monthOfYear(10), dayOfMonth(2), hourOfDay(2), minuteOfHour(50))
		"datetimeLocal" | "2008-10-02T02:50:43"           | secondOfMinute(43)
		"datetimeLocal" | "2008-10-02T02:50:43.1"         | millisOfSecond(100)
	    "datetimeLocal" | "2008-10-02T02:50:43.12"        | millisOfSecond(120)
        "datetimeLocal" | "2008-10-02T02:50:43.123"       | millisOfSecond(123)
        "datetime"      | "2008-10-02T02:50Z"             | allOf(year(2008), monthOfYear(10), dayOfMonth(2), hourOfDay(2), minuteOfHour(50), zone(UTC))
        "datetime"      | "2008-10-02T02:50:43Z"          | secondOfMinute(43)
        "datetime"      | "2008-10-02T02:50:43.1Z"        | millisOfSecond(100)
        "datetime"      | "2008-10-02T02:50:43.12Z"       | millisOfSecond(120)
        "datetime"      | "2008-10-02T02:50:43.123Z"      | zone(UTC)
        "datetime"      | "2008-10-02T02:50:43.123-08:00" | zone(DateTimeZone.forOffsetHours(-8))
        "datetime"      | "2008-10-02T02:50:43.123+05:30" | zone(DateTimeZone.forOffsetHoursMinutes(5, 30))
		"time"          | "02:50"                         | allOf(hourOfDay(2), minuteOfHour(50))
		"time"          | "02:50:43"                      | secondOfMinute(43)
		"time"          | "02:50:43.123"                  | millisOfSecond(123)
		"time"          | "02:50:43.12"                   | millisOfSecond(120)
		"time"          | "02:50:43.1"                    | millisOfSecond(100)
	}
	
	private static Matcher<DateTime> year(int expected) {
		hasProperty "year", equalTo(expected)
	}

	private static Matcher<DateTime> monthOfYear(int expected) {
		hasProperty "monthOfYear", equalTo(expected)
	}

	private static Matcher<DateTime> dayOfMonth(int expected) {
		hasProperty "dayOfMonth", equalTo(expected)
	}

	private static Matcher<DateTime> weekOfWeekyear(int expected) {
		hasProperty "weekOfWeekyear", equalTo(expected)
	}

	private static Matcher<DateTime> hourOfDay(int expected) {
		hasProperty "hourOfDay", equalTo(expected)
	}

	private static Matcher<DateTime> minuteOfHour(int expected) {
		hasProperty "minuteOfHour", equalTo(expected)
	}

	private static Matcher<DateTime> secondOfMinute(int expected) {
		hasProperty "secondOfMinute", equalTo(expected)
	}

	private static Matcher<DateTime> millisOfSecond(int expected) {
		hasProperty "millisOfSecond", equalTo(expected)
	}

	private static Matcher<DateTime> zone(DateTimeZone expected) {
		hasProperty "zone", equalTo(expected)
	}

}
