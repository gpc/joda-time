package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.JodaTimeUtils
import grails.test.TagLibUnitTestCase
import java.util.regex.Matcher
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.chrono.IslamicChronology
import org.springframework.mock.web.MockHttpServletRequest
import static java.util.Locale.FRANCE
import static java.util.Locale.UK

class JodaTimeTagLibTests extends TagLibUnitTestCase {

	MockHttpServletRequest request

	void setUp() {
		super.setUp()

		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def mockGrailsApplication = [config: new ConfigObject()]
		tagLib.metaClass.getGrailsApplication = {-> mockGrailsApplication }

		request = new MockHttpServletRequest()
		request.addPreferredLocale UK

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	protected void tearDown() {
		super.tearDown()

		DateTimeUtils.setCurrentMillisSystem()
	}

	void testDatePickerOutputsOnlyDateFields() {
		tagLib.datePicker(name: 'foo')

		def output = tagLib.out.toString()

		assertMatch(/<select name="foo_day"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertMatch(/<select name="foo_year"/, output)
		assertNoMatch(/<select name="foo_hour"/, output)
		assertNoMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)
	}

	void testTimePickerOutputsOnlyTimeFields() {
		tagLib.timePicker(name: 'foo', precision: 'second')

		def output = tagLib.out.toString()

		assertNoMatch(/<select name="foo_day"/, output)
		assertNoMatch(/<select name="foo_month"/, output)
		assertNoMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_hour"/, output)
		assertMatch(/<select name="foo_minute"/, output)
		assertMatch(/<select name="foo_second"/, output)
	}

	void testPickerTagsUseCurrentDateTimeAsDefault() {
		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		def output = tagLib.out.toString()

		assertMatch(/<option value="2" selected="selected">2<\/option>/, output)
		assertMatch(/<option value="10" selected="selected">October<\/option>/, output)
		assertMatch(/<option value="2008" selected="selected">2008<\/option>/, output)
		assertMatch(/<option value="2" selected="selected">2<\/option>/, output)
		assertMatch(/<option value="50" selected="selected">50<\/option>/, output)
		assertMatch(/<option value="33" selected="selected">33<\/option>/, output)
	}

	void testPickerTagsAcceptStringDefault() {
		tagLib.dateTimePicker(name: 'foo', 'default': '1971-11-29T16:22')

		def output = tagLib.out.toString()

		assertMatch(/<option value="29" selected="selected">29<\/option>/, output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, output)
	}

	void testStringDefaultFormatIsAppropriateForPrecision() {
		tagLib.datePicker(name: 'foo', 'default': '1971-11-29')

		def output = tagLib.out.toString()

		assertMatch(/<option value="29" selected="selected">29<\/option>/, output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, output)
	}

	void testTimePickerAcceptsStringDefault() {
		tagLib.timePicker(name: 'foo', precision: 'second', 'default': '23:59:59')

		def output = tagLib.out.toString()

		assertMatch(/<option value="23" selected="selected">23<\/option>/, output)
		assertMatch(/<option value="59" selected="selected">59<\/option>/, output)
		assertMatch(/<option value="59" selected="selected">59<\/option>/, output)
	}

	void testPickerTagsAcceptReadableInstantDefault() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		def output = tagLib.out.toString()

		assertMatch(/<option value="29" selected="selected">29<\/option>/, output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, output)
	}

	void testPickerTagsAcceptReadablePartialDefault() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		def output = tagLib.out.toString()

		assertMatch(/<option value="29" selected="selected">29<\/option>/, output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, output)
	}

	void testPickerTagsAcceptReadableInstantValue() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new DateTime(1977, 2, 8, 9, 30, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		def output = tagLib.out.toString()

		assertMatch(/<option value="8" selected="selected">8<\/option>/, output)
		assertMatch(/<option value="2" selected="selected">February<\/option>/, output)
		assertMatch(/<option value="1977" selected="selected">1977<\/option>/, output)
		assertMatch(/<option value="09" selected="selected">09<\/option>/, output)
		assertMatch(/<option value="30" selected="selected">30<\/option>/, output)
	}

	void testPickerTagsAcceptReadablePartialValue() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new LocalDateTime(1942, 8, 15, 4, 16, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		def output = tagLib.out.toString()

		assertMatch(/<option value="15" selected="selected">15<\/option>/, output)
		assertMatch(/<option value="8" selected="selected">August<\/option>/, output)
		assertMatch(/<option value="1942" selected="selected">1942<\/option>/, output)
		assertMatch(/<option value="04" selected="selected">04<\/option>/, output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, output)
	}

	void testPickerTagsAcceptNoSelectionArg() {
		tagLib.dateTimePicker(name: 'foo', noSelection: ['': 'Choose sumfink innit'])

		def output = tagLib.out.toString()

		assertMatch(/<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/, output)
		assertMatch(/<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/, output)
		assertMatch(/<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/, output)
		assertMatch(/<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/, output)
		assertMatch(/<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/, output)
	}

	void testPickerTagsAcceptYearsArg() {
		tagLib.dateTimePicker(name: 'foo', years: 1979..1983, 'default': '1979-12-04T00:00')

		def output = tagLib.out.toString()

		assertNoMatch(/<option value="1978">1978<\/option>/, output)
		assertMatch(/<option value="1979" selected="selected">1979<\/option>/, output)
		assertMatch(/<option value="1980">1980<\/option>/, output)
		assertMatch(/<option value="1981">1981<\/option>/, output)
		assertMatch(/<option value="1982">1982<\/option>/, output)
		assertMatch(/<option value="1983">1983<\/option>/, output)
		assertNoMatch(/<option value="1984">1984<\/option>/, output)
	}

	void testDateTimePickerUsesMinuteAsDefaultPrecision() {
		tagLib.dateTimePicker(name: 'foo')

		def output = tagLib.out.toString()

		assertMatch(/<select name="foo_day"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_hour"/, output)
		assertMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)
	}

	void testPickerTagsAcceptPrecisionArg() {
		tagLib.dateTimePicker(name: 'foo', precision: 'year')

		def output = tagLib.out.toString()

		assertMatch(/<select name="foo_year"/, output)
		assertNoMatch(/<select name="foo_month"/, output)
		assertNoMatch(/<select name="foo_day"/, output)
		assertNoMatch(/<select name="foo_hour"/, output)
		assertNoMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)

		tagLib.dateTimePicker(name: 'foo', precision: 'month')

		output = tagLib.out.toString()

		assertMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertNoMatch(/<select name="foo_day"/, output)
		assertNoMatch(/<select name="foo_hour"/, output)
		assertNoMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)

		tagLib.dateTimePicker(name: 'foo', precision: 'day')

		output = tagLib.out.toString()

		assertMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertMatch(/<select name="foo_day"/, output)
		assertNoMatch(/<select name="foo_hour"/, output)
		assertNoMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)

		tagLib.dateTimePicker(name: 'foo', precision: 'hour')

		output = tagLib.out.toString()

		assertMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertMatch(/<select name="foo_day"/, output)
		assertMatch(/<select name="foo_hour"/, output)
		assertNoMatch(/<select name="foo_minute"/, output)
		assertNoMatch(/<select name="foo_second"/, output)

		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		output = tagLib.out.toString()

		assertMatch(/<select name="foo_year"/, output)
		assertMatch(/<select name="foo_month"/, output)
		assertMatch(/<select name="foo_day"/, output)
		assertMatch(/<select name="foo_hour"/, output)
		assertMatch(/<select name="foo_minute"/, output)
		assertMatch(/<select name="foo_second"/, output)
	}

	void testFormatDefaultsToCurrentDateTimeInRequestLocale() {
		tagLib.format([:])
		def output = tagLib.out.toString()
		assertEquals('Oct 2, 2008 2:50:33 AM', output)
	}

	void testFormatUsesValueIfSpecified() {
		tagLib.format(value: new DateTime(1971, 11, 29, 16, 22, 0, 0))
		def output = tagLib.out.toString()
		assertEquals('Nov 29, 1971 4:22:00 PM', output)
	}

	void testFormatUsesLocaleIfSpecified() {
		tagLib.format(locale: FRANCE)
		def output = tagLib.out.toString()
		assertEquals('2 oct. 2008 02:50:33', output)
	}

	void testFormatUsesZoneIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(zone: DateTimeZone.forID('America/Vancouver'))
		}
		def output = tagLib.out.toString()
		assertEquals('Oct 1, 2008 6:50:33 PM', output)
	}

	void testFormatUsesChronologyIfSpecified() {
		tagLib.format(chronology: IslamicChronology.instance)
		def output = tagLib.out.toString()
		assertEquals('10 1, 1429 2:50:33 AM', output)
	}

	void testFormatUsesStyleIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(style: 'LL')
		}
		def output = tagLib.out.toString()
		assertEquals('October 2, 2008 2:50:33 AM BST', output)
	}

	void testFormatAcceptsLocalDateValue() {
		tagLib.format(value: new LocalDate(1985, 10, 13))
		def output = tagLib.out.toString()
		assertEquals('Oct 13, 1985', output)
	}

	void testFormatAcceptsLocalTimeValue() {
		tagLib.format(value: new LocalTime(16, 23, 42))
		def output = tagLib.out.toString()
		assertEquals('4:23:42 PM', output)
	}

	void testFormatDoesNotAcceptBothStyleAndPattern() {
		shouldFail(GrailsTagException) {
			tagLib.format(style: 'SS', pattern: 'yyyy-MM-dd HH:mm:ss')
		}
	}

	void testFormatUsesPatternIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(pattern: '[z] dd-MM-yyyy@HH:mm:ss')
		}
		def output = tagLib.out.toString()
		assertEquals('[BST] 02-10-2008@02:50:33', output)
	}

	void assertMatch(String expectedRegex, actual) {
		Matcher matcher = actual =~ expectedRegex
		if (!matcher) {
			fail("Expected match of $expectedRegex but found $actual")
		}
	}

	void assertNoMatch(String expectedRegex, actual) {
		Matcher matcher = actual =~ expectedRegex
		if (matcher) {
			fail("Expected no match of $expectedRegex but found $actual")
		}
	}
}
