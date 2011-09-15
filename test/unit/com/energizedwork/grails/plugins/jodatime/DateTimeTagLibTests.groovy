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
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.junit.*
import grails.test.mixin.*

@TestFor(DateTimeTagLib)
class DateTimeTagLibTests {

	@Before void setUp() {
		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	@After void tearDown() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	@Test void datePickerOutputsOnlyDateFields() {
		def output = applyTemplate('<joda:datePicker name="foo"/>')

		assert output =~ /<select name="foo_day"/
		assert output =~ /<select name="foo_month"/
		assert output =~ /<select name="foo_year"/
		assert !output.contains(/<select name="foo_hour"/)
		assert !output.contains(/<select name="foo_minute"/)
		assert !output.contains(/<select name="foo_second"/)
	}

	@Test void timePickerOutputsOnlyTimeFields() {
		def output = applyTemplate('<joda:timePicker name="foo" precision="second"/>')

		assert !output.contains(/<select name="foo_day"/)
		assert !output.contains(/<select name="foo_month"/)
		assert !output.contains(/<select name="foo_year"/)
		assert output =~ /<select name="foo_hour"/
		assert output =~ /<select name="foo_minute"/
		assert output =~ /<select name="foo_second"/
	}

	@Test void pickerTagsUseCurrentDateTimeAsDefault() {
		def output = applyTemplate('<joda:dateTimePicker name="foo" precision="second"/>')

		assert output =~ /<option value="2" selected="selected">2<\/option>/
		assert output =~ /<option value="10" selected="selected">October<\/option>/
		assert output =~ /<option value="2008" selected="selected">2008<\/option>/
		assert output =~ /<option value="2" selected="selected">2<\/option>/
		assert output =~ /<option value="50" selected="selected">50<\/option>/
		assert output =~ /<option value="33" selected="selected">33<\/option>/
	}

	@Test void pickerTagsAcceptStringDefault() {
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="1971-11-29T16:22"/>')

		assert output =~ /<option value="29" selected="selected">29<\/option>/
		assert output =~ /<option value="11" selected="selected">November<\/option>/
		assert output =~ /<option value="1971" selected="selected">1971<\/option>/
		assert output =~ /<option value="16" selected="selected">16<\/option>/
		assert output =~ /<option value="22" selected="selected">22<\/option>/
	}

	@Test void stringDefaultFormatIsAppropriateForPrecision() {
		def output = applyTemplate('<joda:datePicker name="foo" default="1971-11-29"/>')

		assert output =~ /<option value="29" selected="selected">29<\/option>/
		assert output =~ /<option value="11" selected="selected">November<\/option>/
		assert output =~ /<option value="1971" selected="selected">1971<\/option>/
	}

	@Test void timePickerAcceptsStringDefault() {
		def output = applyTemplate('<joda:timePicker name="foo" precision="second" default="23:59:59"/>')

		assert output =~ /<option value="23" selected="selected">23<\/option>/
		assert output =~ /<option value="59" selected="selected">59<\/option>/
		assert output =~ /<option value="59" selected="selected">59<\/option>/
	}

	@Test void pickerTagsAcceptReadableInstantDefault() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}"/>', [defaultValue: defaultValue])

		assert output =~ /<option value="29" selected="selected">29<\/option>/
		assert output =~ /<option value="11" selected="selected">November<\/option>/
		assert output =~ /<option value="1971" selected="selected">1971<\/option>/
		assert output =~ /<option value="16" selected="selected">16<\/option>/
		assert output =~ /<option value="22" selected="selected">22<\/option>/
	}

	@Test void pickerTagsAcceptReadablePartialDefault() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}"/>', [defaultValue: defaultValue])

		assert output =~ /<option value="29" selected="selected">29<\/option>/
		assert output =~ /<option value="11" selected="selected">November<\/option>/
		assert output =~ /<option value="1971" selected="selected">1971<\/option>/
		assert output =~ /<option value="16" selected="selected">16<\/option>/
		assert output =~ /<option value="22" selected="selected">22<\/option>/
	}

	@Test void pickerTagsAcceptReadableInstantValue() {
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new DateTime(1977, 2, 8, 9, 30, 0, 0)
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}" value="${value}"/>', [value: value, defaultValue: defaultValue])

		assert output =~ /<option value="8" selected="selected">8<\/option>/
		assert output =~ /<option value="2" selected="selected">February<\/option>/
		assert output =~ /<option value="1977" selected="selected">1977<\/option>/
		assert output =~ /<option value="09" selected="selected">09<\/option>/
		assert output =~ /<option value="30" selected="selected">30<\/option>/
	}

	@Test void pickerTagsAcceptReadablePartialValue() {
		def defaultValue = new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)
		def value = new LocalDateTime(1942, 8, 15, 4, 16, 0, 0)
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}" value="${value}"/>', [value: value, defaultValue: defaultValue])

		assert output =~ /<option value="15" selected="selected">15<\/option>/
		assert output =~ /<option value="8" selected="selected">August<\/option>/
		assert output =~ /<option value="1942" selected="selected">1942<\/option>/
		assert output =~ /<option value="04" selected="selected">04<\/option>/
		assert output =~ /<option value="16" selected="selected">16<\/option>/
	}

	@Test void pickerTagsAcceptNoSelectionArg() {
		def output = applyTemplate('''<joda:dateTimePicker name="foo" noSelection="['': 'Choose sumfink innit']"/>''')

		assert output =~ /<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/
		assert output =~ /<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/
		assert output =~ /<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/
		assert output =~ /<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/
		assert output =~ /<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/
	}

    @Test void pickerTagsUsesOneHundredYearsAsDefault() {
        def year = new LocalDate().year
        def output = applyTemplate('<joda:dateTimePicker name="foo"/>')

        assert !output.contains(/<option value="${year - 101}">${year - 101}<\/option>/)
        assert output =~ /<option value="${year - 100}">${year - 100}<\/option>/
        assert output =~ /<option value="${year + 100}">${year + 100}<\/option>/
        assert !output.contains(/<option value="${year + 101}">${year + 101}<\/option>/)
    }

    @Test void dateTimePickerUsesConfigForYears() {
        tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsBelow = 1
        tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsAbove = 2
        def year = new LocalDate().year
        def output = applyTemplate('<joda:dateTimePicker name="foo"/>')

        assert !output.contains(/<option value="${year - 2}">${year - 2}<\/option>/)
        assert output =~ /<option value="${year - 1}">${year - 1}<\/option>/
        assert output =~ /<option value="${year}" selected="selected">${year}<\/option>/
        assert output =~ /<option value="${year + 1}">${year + 1}<\/option>/
        assert output =~ /<option value="${year + 2}">${year + 2}<\/option>/
        assert !output.contains(/<option value="${year + 3}">${year + 3}<\/option>/)
    }

    @Test void pickerTagsAcceptYearsArg() {
		def output = applyTemplate('<joda:dateTimePicker name="foo" years="${1979..1983}" default="1979-12-04T00:00"/>')

		assert !output.contains(/<option value="1978">1978<\/option>/)
		assert output =~ /<option value="1979" selected="selected">1979<\/option>/
		assert output =~ /<option value="1980">1980<\/option>/
		assert output =~ /<option value="1981">1981<\/option>/
		assert output =~ /<option value="1982">1982<\/option>/
		assert output =~ /<option value="1983">1983<\/option>/
		assert !output.contains(/<option value="1984">1984<\/option>/)
	}

	@Test void dateTimePickerUsesMinuteAsDefaultPrecision() {
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')

		assert output =~ /<select name="foo_day"/
		assert output =~ /<select name="foo_month"/
		assert output =~ /<select name="foo_year"/
		assert output =~ /<select name="foo_hour"/
		assert output =~ /<select name="foo_minute"/
		assert !output.contains(/<select name="foo_second"/)
	}

    @Test void dateTimePickerUsesConfigForPrecision() {
        tagLib.grailsApplication.config.grails.tags.datePicker.default.precision = "second"
        def output = applyTemplate('<joda:dateTimePicker name="foo"/>')

        assert output =~ /<select name="foo_day"/
        assert output =~ /<select name="foo_month"/
        assert output =~ /<select name="foo_year"/
        assert output =~ /<select name="foo_hour"/
        assert output =~ /<select name="foo_minute"/
        assert output =~ /<select name="foo_second"/
    }

    @Test void pickerTagsAcceptPrecisionArg() {
		def output = applyTemplate('<joda:dateTimePicker name="foo" precision="year"/>')

		assert output =~ /<select name="foo_year"/
		assert !output.contains(/<select name="foo_month"/)
		assert !output.contains(/<select name="foo_day"/)
		assert !output.contains(/<select name="foo_hour"/)
		assert !output.contains(/<select name="foo_minute"/)
		assert !output.contains(/<select name="foo_second"/)

		output = applyTemplate('<joda:dateTimePicker name="foo" precision="month"/>')

		assert output =~ /<select name="foo_year"/
		assert output =~ /<select name="foo_month"/
		assert !output.contains(/<select name="foo_day"/)
		assert !output.contains(/<select name="foo_hour"/)
		assert !output.contains(/<select name="foo_minute"/)
		assert !output.contains(/<select name="foo_second"/)

		output = applyTemplate('<joda:dateTimePicker name="foo" precision="day"/>')

		assert output =~ /<select name="foo_year"/
		assert output =~ /<select name="foo_month"/
		assert output =~ /<select name="foo_day"/
		assert !output.contains(/<select name="foo_hour"/)
		assert !output.contains(/<select name="foo_minute"/)
		assert !output.contains(/<select name="foo_second"/)

		output = applyTemplate('<joda:dateTimePicker name="foo" precision="hour"/>')

		assert output =~ /<select name="foo_year"/
		assert output =~ /<select name="foo_month"/
		assert output =~ /<select name="foo_day"/
		assert output =~ /<select name="foo_hour"/
		assert !output.contains(/<select name="foo_minute"/)
		assert !output.contains(/<select name="foo_second"/)

		output = applyTemplate('<joda:dateTimePicker name="foo" precision="second"/>')

		assert output =~ /<select name="foo_year"/
		assert output =~ /<select name="foo_month"/
		assert output =~ /<select name="foo_day"/
		assert output =~ /<select name="foo_hour"/
		assert output =~ /<select name="foo_minute"/
		assert output =~ /<select name="foo_second"/
	}

	@Test void dateTimePickerAcceptsUseZoneArg() {
		mockTagLib DateTimeZoneTagLib

		def output = applyTemplate('<joda:dateTimePicker name="foo" useZone="true"/>')

		assert output =~ /<select name="foo_zone"/
	}

}
