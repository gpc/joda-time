package com.energizedwork.grails.plugins.jodatime

import org.hamcrest.Matcher
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test
import static com.energizedwork.grails.plugins.jodatime.Html5DateTimeFormat.*
import static org.hamcrest.Matchers.*
import static org.joda.time.DateTimeZone.UTC
import static org.junit.Assert.assertThat

class Html5DateTimeFormatTests {

	DateTime dateTime = new DateTime(2008, 10, 2, 2, 50, 43, 123, UTC)

	@Test
	void monthPrintsCorrectly() {
		assertThat "date formatted as HTML5 month", month().print(dateTime), equalTo("2008-10")
	}

	@Test
	void monthParsesCorrectly() {
		assertThat "date parsed as HTML5 month", month().parseDateTime("2008-10"), allOf(year(2008), monthOfYear(10))
	}

	@Test
	void weekPrintsCorrectly() {
		assertThat "date formatted as HTML5 week", week().print(dateTime), equalTo("2008-W40")
	}

	@Test
	void weekParsesCorrectly() {
		assertThat "date parsed as HTML5 week", week().parseDateTime("2008-W40"), allOf(year(2008), weekOfWeekyear(40))
	}

	@Test
	void datePrintsCorrectly() {
		assertThat "date formatted as HTML5 date", date().print(dateTime), equalTo("2008-10-02")
	}

	@Test
	void dateParsesCorrectly() {
		assertThat "date parsed as HTML5 date", date().parseDateTime("2008-10-02"), allOf(year(2008), monthOfYear(10), dayOfMonth(2))
	}

	@Test
	void datetimeLocalPrintsCorrectly() {
		assertThat "date formatted as HTML5 datetime-local", datetimeLocal().print(dateTime), equalTo("2008-10-02T02:50:43.123")
	}

	@Test
	void datetimeLocalParsesCorrectly() {
		assertThat "date parsed as HTML5 datetime-local", datetimeLocal().parseDateTime("2008-10-02T02:50"), allOf(year(2008), monthOfYear(10), dayOfMonth(2), hourOfDay(2), minuteOfHour(50))
		assertThat "date parsed as HTML5 datetime-local", datetimeLocal().parseDateTime("2008-10-02T02:50:43"), secondOfMinute(43)
		assertThat "date parsed as HTML5 datetime-local", datetimeLocal().parseDateTime("2008-10-02T02:50:43.1"), millisOfSecond(100)
		assertThat "date parsed as HTML5 datetime-local", datetimeLocal().parseDateTime("2008-10-02T02:50:43.12"), millisOfSecond(120)
		assertThat "date parsed as HTML5 datetime-local", datetimeLocal().parseDateTime("2008-10-02T02:50:43.123"), millisOfSecond(123)
	}

	@Test
	void datetimePrintsCorrectly() {
		assertThat "date formatted as HTML5 datetime", datetime().print(dateTime), equalTo("2008-10-02T02:50:43.123Z")
	}

	@Test
	void datetimeParsesCorrectly() {
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50Z"), allOf(year(2008), monthOfYear(10), dayOfMonth(2), hourOfDay(2), minuteOfHour(50), zone(UTC))
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43Z"), secondOfMinute(43)
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43.1Z"), millisOfSecond(100)
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43.12Z"), millisOfSecond(120)
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43.123Z"), zone(UTC)
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43.123-08:00"), zone(DateTimeZone.forOffsetHours(-8))
		assertThat "date parsed as HTML5 datetime", datetime().parseDateTime("2008-10-02T02:50:43.123+05:30"), zone(DateTimeZone.forOffsetHoursMinutes(5, 30))
	}

	@Test
	void timePrintsCorrectly() {
		assertThat "date formatted as HTML5 time", time().print(dateTime), equalTo("02:50:43.123")
	}

	@Test
	void timeParsesCorrectly() {
		assertThat "date parsed as HTML5 time", time().parseDateTime("02:50"), allOf(hourOfDay(2), minuteOfHour(50))
		assertThat "date parsed as HTML5 time", time().parseDateTime("02:50:43"), secondOfMinute(43)
		assertThat "date parsed as HTML5 time", time().parseDateTime("02:50:43.123"), millisOfSecond(123)
		assertThat "date parsed as HTML5 time", time().parseDateTime("02:50:43.12"), millisOfSecond(120)
		assertThat "date parsed as HTML5 time", time().parseDateTime("02:50:43.1"), millisOfSecond(100)
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
