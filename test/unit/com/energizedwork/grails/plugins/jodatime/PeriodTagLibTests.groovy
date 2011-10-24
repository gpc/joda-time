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

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.Period
import org.junit.*

@TestFor(PeriodTagLib)
class PeriodTagLibTests {
	
	@After void resetConfig() {
		grailsApplication.config.jodatime.clear()
	}

	@Test
	void periodPickerDefaultsIdFromName() {
		def output = applyTemplate('<joda:periodPicker name="foo"/>')

		["hours", "minutes", "seconds"].each {
			assert output.contains(/<input type="text" name="foo_$it" id="foo_$it"/)
		}
	}

	@Test
	void periodPickerUsesHoursMinutesAndSecondsByDefault() {
		def output = applyTemplate('<joda:periodPicker name="foo"/>')

		assert !output.contains(/<input type="text" name="foo_years"/)
		assert !output.contains(/<input type="text" name="foo_months"/)
		assert !output.contains(/<input type="text" name="foo_weeks"/)
		assert !output.contains(/<input type="text" name="foo_days"/)
		assert output.contains(/<input type="text" name="foo_hours"/)
		assert output.contains(/<input type="text" name="foo_minutes"/)
		assert output.contains(/<input type="text" name="foo_seconds"/)
		assert !output.contains(/<input type="text" name="foo_millis"/)
	}

	@Test
	void periodPickerUsesFieldsFromConfigIfPresent() {
		grailsApplication.config.jodatime.periodpicker.default.fields = "years,months , days"

		def output = applyTemplate('<joda:periodPicker name="foo"/>')

		assert output.contains(/<input type="text" name="foo_years"/)
		assert output.contains(/<input type="text" name="foo_months"/)
		assert !output.contains(/<input type="text" name="foo_weeks"/)
		assert output.contains(/<input type="text" name="foo_days"/)
		assert !output.contains(/<input type="text" name="foo_hours"/)
		assert !output.contains(/<input type="text" name="foo_minutes"/)
		assert !output.contains(/<input type="text" name="foo_seconds"/)
		assert !output.contains(/<input type="text" name="foo_millis"/)
	}

	@Test
	void periodPickerAcceptsFieldsAttribute() {
		def output = applyTemplate('<joda:periodPicker name="foo" fields="years,months,days"/>')

		assert output.contains(/<input type="text" name="foo_years"/)
		assert output.contains(/<input type="text" name="foo_months"/)
		assert !output.contains(/<input type="text" name="foo_weeks"/)
		assert output.contains(/<input type="text" name="foo_days"/)
		assert !output.contains(/<input type="text" name="foo_hours"/)
		assert !output.contains(/<input type="text" name="foo_minutes"/)
		assert !output.contains(/<input type="text" name="foo_seconds"/)
		assert !output.contains(/<input type="text" name="foo_millis"/)
	}

	@Test
	void periodPickerAcceptsValueAttribute() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35)

		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: value])

		assert output.contains(/<input type="text" name="foo_hours" id="foo_hours" value="8"/)
		assert output.contains(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/)
		assert output.contains(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/)
	}

	@Test
	void periodPickerAcceptsDurationValue() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()

		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: value])

		assert output.contains(/<input type="text" name="foo_hours" id="foo_hours" value="8"/)
		assert output.contains(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/)
		assert output.contains(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/)
	}

	@Test
	void periodPickerUsesHourAsHighestFieldWhenValueIsDuration() {
		def value = new Period().withDays(1).withHours(1).toStandardDuration()

		def output = applyTemplate('<joda:periodPicker name="foo" fields="days,hours,minutes,seconds" value="${value}"/>', [value: value])

		assert output.contains(/<input type="text" name="foo_days" id="foo_days" value="0"/)
		assert output.contains(/<input type="text" name="foo_hours" id="foo_hours" value="25"/)
		assert output.contains(/<input type="text" name="foo_minutes" id="foo_minutes" value="0"/)
		assert output.contains(/<input type="text" name="foo_seconds" id="foo_seconds" value="0"/)
	}

	@Test
	void periodPickerHandlesNullValueAttribute() {
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: null])

		["hours", "minutes", "seconds"].each {
			assert output.contains(/<input type="text" name="foo_$it" id="foo_$it" value="0"/)
		}
	}

	@Test
	void periodPickerOutputsLabels() {
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: null])

		["hours", "minutes", "seconds"].each {
			assert output =~ /<label for="foo_$it"><input type="text" name="foo_$it"[^\/]*\/>&nbsp;$it <\/label>/
		}
	}

	@Test
	void periodPickerLabelsCanBeChangedWithMessageProperties() {
		def messages = [
				"org.joda.time.DurationFieldType.hours": "h",
				"org.joda.time.DurationFieldType.minutes": "m",
				"org.joda.time.DurationFieldType.seconds": "s"
		]
		messageSource.addMessages messages, Locale.ENGLISH
		
		def output = applyTemplate('<joda:periodPicker name="foo"/>')

		assert output =~ /<label for="foo_hours"><input type="text" name="foo_hours"[^\/]*\/>&nbsp;h <\/label>/
		assert output =~ /<label for="foo_minutes"><input type="text" name="foo_minutes"[^\/]*\/>&nbsp;m <\/label>/
		assert output =~ /<label for="foo_seconds"><input type="text" name="foo_seconds"[^\/]*\/>&nbsp;s <\/label>/
	}

	@Test(expected = GrailsTagException)
	void formatPeriodRequiresValueAttribute() {
		applyTemplate('<joda:formatPeriod/>')
	}

	@Test
	void formatPeriodUsesDefaultFields() {
		def value = new Period().withYears(2).withMonths(2).withWeeks(2).withDays(2).withHours(2).withMinutes(2).withSeconds(2)

		assert applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) ==  "2 years, 2 months, 2 weeks, 2 days, 2 hours, 2 minutes and 2 seconds"
	}

	@Test
	void formatPeriodNormalizesValue() {
		def value = new Period().withYears(1).withMonths(12).withDays(7).withHours(48)

		assert applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "2 years, 1 week and 2 days"
	}

	@Test
	void formatPeriodAcceptsFieldAttribute() {
		def value = new Period().withWeeks(2).withHours(50).withMinutes(2).withSeconds(2)

		assert applyTemplate('<joda:formatPeriod fields="days,hours,minutes" value="${value}"/>', [value: value]) == "16 days, 2 hours and 2 minutes"
	}

	@Test
	void formatPeriodUsesFieldsFromConfig() {
		grailsApplication.config.jodatime.periodpicker.default.fields = "years,months , days"
		
		def value = new Period().withYears(2).withMonths(14).withDays(2).withHours(12)

		assert applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "3 years, 2 months and 2 days"
	}

	@Test
	void formatPeriodOmitsZeroValuedFields() {
		def value = new Period().withMonths(2).withDays(2).withMinutes(2)

		assert applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "2 months, 2 days and 2 minutes"
	}

	@Test
	void formatPeriodHandlesErrorIfValueHasYearsOrMonthsAndFieldsDoesNot() {
		def value = new Period().withYears(1).withMonths(1).withDays(2).withMinutes(2)

		assert applyTemplate('<joda:formatPeriod fields="days,minutes" value="${value}"/>', [value: value]) == "2 days and 2 minutes"
	}

	@Test
	void formatPeriodAcceptsDurationValue() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()

		assert applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "8 hours, 12 minutes and 35 seconds"
	}

}