/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.LocalDateTime
import static com.energizedwork.grails.commons.test.RegexMatcher.isMatch
import static org.hamcrest.Matchers.not
import static org.junit.Assert.assertThat

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

		assertThat tagLib.output, isMatch(/<select name="foo_day"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_hour"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_minute"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))
	}

	void testTimePickerOutputsOnlyTimeFields() {
		tagLib.timePicker(name: 'foo', precision: 'second')

		assertThat tagLib.output, not(isMatch(/<select name="foo_day"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_month"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_year"/))
		assertThat tagLib.output, isMatch(/<select name="foo_hour"/)
		assertThat tagLib.output, isMatch(/<select name="foo_minute"/)
		assertThat tagLib.output, isMatch(/<select name="foo_second"/)
	}

	void testPickerTagsUseCurrentDateTimeAsDefault() {
		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		assertThat tagLib.output, isMatch(/<option value="2" selected="selected">2<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="10" selected="selected">October<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="2008" selected="selected">2008<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="2" selected="selected">2<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="50" selected="selected">50<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="33" selected="selected">33<\/option>/)
	}

	void testPickerTagsAcceptStringDefault() {
		tagLib.dateTimePicker(name: 'foo', 'default': '1971-11-29T16:22')

		assertThat tagLib.output, isMatch(/<option value="29" selected="selected">29<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="11" selected="selected">November<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1971" selected="selected">1971<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="16" selected="selected">16<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="22" selected="selected">22<\/option>/)
	}

	void testStringDefaultFormatIsAppropriateForPrecision() {
		tagLib.datePicker(name: 'foo', 'default': '1971-11-29')

		assertThat tagLib.output, isMatch(/<option value="29" selected="selected">29<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="11" selected="selected">November<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1971" selected="selected">1971<\/option>/)
	}

	void testTimePickerAcceptsStringDefault() {
		tagLib.timePicker(name: 'foo', precision: 'second', 'default': '23:59:59')

		assertThat tagLib.output, isMatch(/<option value="23" selected="selected">23<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="59" selected="selected">59<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="59" selected="selected">59<\/option>/)
	}

	void testPickerTagsAcceptReadableInstantDefault() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		assertThat tagLib.output, isMatch(/<option value="29" selected="selected">29<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="11" selected="selected">November<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1971" selected="selected">1971<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="16" selected="selected">16<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="22" selected="selected">22<\/option>/)
	}

	void testPickerTagsAcceptReadablePartialDefault() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue)

		assertThat tagLib.output, isMatch(/<option value="29" selected="selected">29<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="11" selected="selected">November<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1971" selected="selected">1971<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="16" selected="selected">16<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="22" selected="selected">22<\/option>/)
	}

	void testPickerTagsAcceptReadableInstantValue() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new DateTime(1977, 2, 8, 9, 30, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		assertThat tagLib.output, isMatch(/<option value="8" selected="selected">8<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="2" selected="selected">February<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1977" selected="selected">1977<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="09" selected="selected">09<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="30" selected="selected">30<\/option>/)
	}

	void testPickerTagsAcceptReadablePartialValue() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new LocalDateTime(1942, 8, 15, 4, 16, 0, 0)
		tagLib.dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		assertThat tagLib.output, isMatch(/<option value="15" selected="selected">15<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="8" selected="selected">August<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1942" selected="selected">1942<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="04" selected="selected">04<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="16" selected="selected">16<\/option>/)
	}

	void testPickerTagsAcceptNoSelectionArg() {
		tagLib.dateTimePicker(name: 'foo', noSelection: ['': 'Choose sumfink innit'])

		assertThat tagLib.output, isMatch(/<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/)
		assertThat tagLib.output, isMatch(/<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/)
		assertThat tagLib.output, isMatch(/<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/)
		assertThat tagLib.output, isMatch(/<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/)
		assertThat tagLib.output, isMatch(/<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/)
	}

	void testPickerTagsAcceptYearsArg() {
		tagLib.dateTimePicker(name: 'foo', years: 1979..1983, 'default': '1979-12-04T00:00')

		assertThat tagLib.output, not(isMatch(/<option value="1978">1978<\/option>/))
		assertThat tagLib.output, isMatch(/<option value="1979" selected="selected">1979<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1980">1980<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1981">1981<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1982">1982<\/option>/)
		assertThat tagLib.output, isMatch(/<option value="1983">1983<\/option>/)
		assertThat tagLib.output, not(isMatch(/<option value="1984">1984<\/option>/))
	}

	void testDateTimePickerUsesMinuteAsDefaultPrecision() {
		tagLib.dateTimePicker(name: 'foo')

		assertThat tagLib.output, isMatch(/<select name="foo_day"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, isMatch(/<select name="foo_hour"/)
		assertThat tagLib.output, isMatch(/<select name="foo_minute"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))
	}

	void testPickerTagsAcceptPrecisionArg() {
		tagLib.dateTimePicker(name: 'foo', precision: 'year')

		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_month"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_day"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_hour"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_minute"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))

		tagLib.dateTimePicker(name: 'foo', precision: 'month')

		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_day"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_hour"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_minute"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))

		tagLib.dateTimePicker(name: 'foo', precision: 'day')

		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, isMatch(/<select name="foo_day"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_hour"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_minute"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))

		tagLib.dateTimePicker(name: 'foo', precision: 'hour')

		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, isMatch(/<select name="foo_day"/)
		assertThat tagLib.output, isMatch(/<select name="foo_hour"/)
		assertThat tagLib.output, not(isMatch(/<select name="foo_minute"/))
		assertThat tagLib.output, not(isMatch(/<select name="foo_second"/))

		tagLib.dateTimePicker(name: 'foo', precision: 'second')

		assertThat tagLib.output, isMatch(/<select name="foo_year"/)
		assertThat tagLib.output, isMatch(/<select name="foo_month"/)
		assertThat tagLib.output, isMatch(/<select name="foo_day"/)
		assertThat tagLib.output, isMatch(/<select name="foo_hour"/)
		assertThat tagLib.output, isMatch(/<select name="foo_minute"/)
		assertThat tagLib.output, isMatch(/<select name="foo_second"/)
	}

	void testDateTimePickerAcceptsUseZoneArg() {
		def dateTimeZoneSelectArgs
		tagLib.metaClass.dateTimeZoneSelect = {attrs ->
			dateTimeZoneSelectArgs = attrs
		}
		tagLib.dateTimePicker(name: "foo", useZone: "true")
		assertEquals "foo_zone", dateTimeZoneSelectArgs.name
	}

}
