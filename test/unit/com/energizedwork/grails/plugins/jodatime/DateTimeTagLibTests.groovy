package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.LocalDateTime
import static com.energizedwork.grails.commons.test.Assertions.assertMatch
import static com.energizedwork.grails.commons.test.Assertions.assertNoMatch

class DateTimeTagLibTests extends TagLibUnitTestCase {

	void setUp() {
		super.setUp()

		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def mockGrailsApplication = [config: new ConfigObject()]
		tagLib.metaClass.getGrailsApplication = {-> mockGrailsApplication }
		tagLib.metaClass.getOutput = {-> delegate.out.toString() }

		mockRequest.addPreferredLocale Locale.UK

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	protected void tearDown() {
		super.tearDown()

		DateTimeUtils.setCurrentMillisSystem()
	}

	void testDatePickerOutputsOnlyDateFields() {
		tagLib.datePicker(name: 'foo')

		assertMatch(/<select name="foo_day"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertNoMatch(/<select name="foo_hour"/, tagLib.output)
		assertNoMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)
	}

	void testTimePickerOutputsOnlyTimeFields() {
		tagLib.timePicker(name: 'foo', precision: 'second')

		assertNoMatch(/<select name="foo_day"/, tagLib.output)
		assertNoMatch(/<select name="foo_month"/, tagLib.output)
		assertNoMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_hour"/, tagLib.output)
		assertMatch(/<select name="foo_minute"/, tagLib.output)
		assertMatch(/<select name="foo_second"/, tagLib.output)
	}

	void testPickerTagsUseCurrentDateTimeAsDefault() {
		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		assertMatch(/<option value="2" selected="selected">2<\/option>/, tagLib.output)
		assertMatch(/<option value="10" selected="selected">October<\/option>/, tagLib.output)
		assertMatch(/<option value="2008" selected="selected">2008<\/option>/, tagLib.output)
		assertMatch(/<option value="2" selected="selected">2<\/option>/, tagLib.output)
		assertMatch(/<option value="50" selected="selected">50<\/option>/, tagLib.output)
		assertMatch(/<option value="33" selected="selected">33<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptStringDefault() {
		tagLib.dateTimePicker(name: 'foo', 'default': '1971-11-29T16:22')

		assertMatch(/<option value="29" selected="selected">29<\/option>/, tagLib.output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, tagLib.output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, tagLib.output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, tagLib.output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, tagLib.output)
	}

	void testStringDefaultFormatIsAppropriateForPrecision() {
		tagLib.datePicker(name: 'foo', 'default': '1971-11-29')

		assertMatch(/<option value="29" selected="selected">29<\/option>/, tagLib.output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, tagLib.output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, tagLib.output)
	}

	void testTimePickerAcceptsStringDefault() {
		tagLib.timePicker(name: 'foo', precision: 'second', 'default': '23:59:59')

		assertMatch(/<option value="23" selected="selected">23<\/option>/, tagLib.output)
		assertMatch(/<option value="59" selected="selected">59<\/option>/, tagLib.output)
		assertMatch(/<option value="59" selected="selected">59<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptReadableInstantDefault() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		assertMatch(/<option value="29" selected="selected">29<\/option>/, tagLib.output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, tagLib.output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, tagLib.output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, tagLib.output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptReadablePartialDefault() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		assertMatch(/<option value="29" selected="selected">29<\/option>/, tagLib.output)
		assertMatch(/<option value="11" selected="selected">November<\/option>/, tagLib.output)
		assertMatch(/<option value="1971" selected="selected">1971<\/option>/, tagLib.output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, tagLib.output)
		assertMatch(/<option value="22" selected="selected">22<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptReadableInstantValue() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new DateTime(1977, 2, 8, 9, 30, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		assertMatch(/<option value="8" selected="selected">8<\/option>/, tagLib.output)
		assertMatch(/<option value="2" selected="selected">February<\/option>/, tagLib.output)
		assertMatch(/<option value="1977" selected="selected">1977<\/option>/, tagLib.output)
		assertMatch(/<option value="09" selected="selected">09<\/option>/, tagLib.output)
		assertMatch(/<option value="30" selected="selected">30<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptReadablePartialValue() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new LocalDateTime(1942, 8, 15, 4, 16, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		assertMatch(/<option value="15" selected="selected">15<\/option>/, tagLib.output)
		assertMatch(/<option value="8" selected="selected">August<\/option>/, tagLib.output)
		assertMatch(/<option value="1942" selected="selected">1942<\/option>/, tagLib.output)
		assertMatch(/<option value="04" selected="selected">04<\/option>/, tagLib.output)
		assertMatch(/<option value="16" selected="selected">16<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptNoSelectionArg() {
		tagLib.dateTimePicker(name: 'foo', noSelection: ['': 'Choose sumfink innit'])

		assertMatch(/<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/, tagLib.output)
		assertMatch(/<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/, tagLib.output)
		assertMatch(/<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/, tagLib.output)
		assertMatch(/<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/, tagLib.output)
		assertMatch(/<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/, tagLib.output)
	}

	void testPickerTagsAcceptYearsArg() {
		tagLib.dateTimePicker(name: 'foo', years: 1979..1983, 'default': '1979-12-04T00:00')

		assertNoMatch(/<option value="1978">1978<\/option>/, tagLib.output)
		assertMatch(/<option value="1979" selected="selected">1979<\/option>/, tagLib.output)
		assertMatch(/<option value="1980">1980<\/option>/, tagLib.output)
		assertMatch(/<option value="1981">1981<\/option>/, tagLib.output)
		assertMatch(/<option value="1982">1982<\/option>/, tagLib.output)
		assertMatch(/<option value="1983">1983<\/option>/, tagLib.output)
		assertNoMatch(/<option value="1984">1984<\/option>/, tagLib.output)
	}

	void testDateTimePickerUsesMinuteAsDefaultPrecision() {
		tagLib.dateTimePicker(name: 'foo')

		assertMatch(/<select name="foo_day"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_hour"/, tagLib.output)
		assertMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)
	}

	void testPickerTagsAcceptPrecisionArg() {
		tagLib.dateTimePicker(name: 'foo', precision: 'year')

		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertNoMatch(/<select name="foo_month"/, tagLib.output)
		assertNoMatch(/<select name="foo_day"/, tagLib.output)
		assertNoMatch(/<select name="foo_hour"/, tagLib.output)
		assertNoMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)

		tagLib.dateTimePicker(name: 'foo', precision: 'month')

		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertNoMatch(/<select name="foo_day"/, tagLib.output)
		assertNoMatch(/<select name="foo_hour"/, tagLib.output)
		assertNoMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)

		tagLib.dateTimePicker(name: 'foo', precision: 'day')

		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertMatch(/<select name="foo_day"/, tagLib.output)
		assertNoMatch(/<select name="foo_hour"/, tagLib.output)
		assertNoMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)

		tagLib.dateTimePicker(name: 'foo', precision: 'hour')

		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertMatch(/<select name="foo_day"/, tagLib.output)
		assertMatch(/<select name="foo_hour"/, tagLib.output)
		assertNoMatch(/<select name="foo_minute"/, tagLib.output)
		assertNoMatch(/<select name="foo_second"/, tagLib.output)

		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		assertMatch(/<select name="foo_year"/, tagLib.output)
		assertMatch(/<select name="foo_month"/, tagLib.output)
		assertMatch(/<select name="foo_day"/, tagLib.output)
		assertMatch(/<select name="foo_hour"/, tagLib.output)
		assertMatch(/<select name="foo_minute"/, tagLib.output)
		assertMatch(/<select name="foo_second"/, tagLib.output)
	}

}
