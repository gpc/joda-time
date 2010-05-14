package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.Period
import org.junit.Before
import org.junit.Test
import static com.energizedwork.grails.commons.test.RegexMatcher.isMatch
import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

class PeriodTagLibTests extends TagLibUnitTestCase {

	@Before
	void setUp() {
		super.setUp()

		tagLib.metaClass.getOutput = {-> delegate.out.toString() }
		tagLib.metaClass.message = { attrs -> attrs.default }
	}

	@Test
	void periodPickerDefaultsIdFromName() {
		tagLib.periodPicker(name: "foo")

		["hours", "minutes", "seconds"].each {
			assertThat tagLib.output, containsString(/<input type="text" name="foo_$it" id="foo_$it"/)
		}
	}

	@Test
	void periodPickerUsesHoursMinutesAndSecondsByDefault() {
		tagLib.periodPicker(name: "foo")

		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_years"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_months"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_weeks"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_days"/))
		assertThat tagLib.output, containsString(/<input type="text" name="foo_hours"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_minutes"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_seconds"/)
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_millis"/))
	}

	@Test
	void periodPickerUsesFieldsFromConfigIfPresent() {
		mockConfig '''
			jodatime.periodpicker.default.fields="years,months , days"
		'''

		tagLib.periodPicker(name: "foo")

		assertThat tagLib.output, containsString(/<input type="text" name="foo_years"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_months"/)
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_weeks"/))
		assertThat tagLib.output, containsString(/<input type="text" name="foo_days"/)
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_hours"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_minutes"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_seconds"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_millis"/))
	}

	@Test
	void periodPickerAcceptsFieldsAttribute() {
		tagLib.periodPicker(name: "foo", fields: "years,months,days")

		assertThat tagLib.output, containsString(/<input type="text" name="foo_years"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_months"/)
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_weeks"/))
		assertThat tagLib.output, containsString(/<input type="text" name="foo_days"/)
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_hours"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_minutes"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_seconds"/))
		assertThat tagLib.output, not(containsString(/<input type="text" name="foo_millis"/))
	}

	@Test
	void periodPickerAcceptsValueAttribute() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35)

		tagLib.periodPicker(name: "foo", value: value)

		assertThat tagLib.output, containsString(/<input type="text" name="foo_hours" id="foo_hours" value="8"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/)
	}

	@Test
	void periodPickerAcceptsDurationValue() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()

		tagLib.periodPicker(name: "foo", value: value)

		assertThat tagLib.output, containsString(/<input type="text" name="foo_hours" id="foo_hours" value="8"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/)
	}

	@Test
	void periodPickerUsesHourAsHighestFieldWhenValueIsDuration() {
		def value = new Period().withDays(1).withHours(1).toStandardDuration()

		tagLib.periodPicker(name: "foo", fields: "days,hours,minutes,seconds", value: value)

		assertThat tagLib.output, containsString(/<input type="text" name="foo_days" id="foo_days" value="0"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_hours" id="foo_hours" value="25"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_minutes" id="foo_minutes" value="0"/)
		assertThat tagLib.output, containsString(/<input type="text" name="foo_seconds" id="foo_seconds" value="0"/)
	}

	@Test
	void periodPickerHandlesNullValueAttribute() {
		tagLib.periodPicker(name: "foo", value: null)

		["hours", "minutes", "seconds"].each {
			assertThat tagLib.output, containsString(/<input type="text" name="foo_$it" id="foo_$it" value="0"/)
		}
	}

	@Test
	void periodPickerOutputsLabels() {
		tagLib.periodPicker(name: "foo", value: null)

		["hours", "minutes", "seconds"].each {
			assertThat tagLib.output, isMatch(/<label for="foo_$it"><input type="text" name="foo_$it"[^\/]*\/>&nbsp;$it <\/label>/)
		}
	}

	@Test
	void periodPickerLabelsCanBeChangedWithMessageProperties() {
		def messages = [
				"org.joda.time.DurationFieldType.hours": "h",
				"org.joda.time.DurationFieldType.minutes": "m",
				"org.joda.time.DurationFieldType.seconds": "s"
		]
		tagLib.metaClass.message = { attrs -> messages[attrs.code] }

		tagLib.periodPicker(name: "foo")

		assertThat tagLib.output, isMatch(/<label for="foo_hours"><input type="text" name="foo_hours"[^\/]*\/>&nbsp;h <\/label>/)
		assertThat tagLib.output, isMatch(/<label for="foo_minutes"><input type="text" name="foo_minutes"[^\/]*\/>&nbsp;m <\/label>/)
		assertThat tagLib.output, isMatch(/<label for="foo_seconds"><input type="text" name="foo_seconds"[^\/]*\/>&nbsp;s <\/label>/)
	}

	@Test(expected = GrailsTagException)
	void formatPeriodRequiresValueAttribute() {
		tagLib.formatPeriod([:])
	}

	@Test
	void formatPeriodUsesDefaultFields() {
		def value = new Period().withYears(2).withMonths(2).withWeeks(2).withDays(2).withHours(2).withMinutes(2).withSeconds(2)

		tagLib.formatPeriod(value: value)

		assertThat tagLib.output, equalTo("2 years, 2 months, 2 weeks, 2 days, 2 hours, 2 minutes and 2 seconds")
	}

	@Test
	void formatPeriodNormalizesValue() {
		def value = new Period().withYears(1).withMonths(12).withDays(7).withHours(48)

		tagLib.formatPeriod(value: value)

		assertThat tagLib.output, equalTo("2 years, 1 week and 2 days")
	}

	@Test
	void formatPeriodAcceptsFieldAttribute() {
		def value = new Period().withWeeks(2).withHours(50).withMinutes(2).withSeconds(2)

		tagLib.formatPeriod(value: value, fields: "days,hours,minutes")

		assertThat tagLib.output, equalTo("16 days, 2 hours and 2 minutes")
	}

	@Test
	void formatPeriodUsesFieldsFromConfig() {
		mockConfig '''
			jodatime.periodpicker.default.fields="years,months , days"
		'''
		def value = new Period().withYears(2).withMonths(14).withDays(2).withHours(12)

		tagLib.formatPeriod(value: value)

		assertThat tagLib.output, equalTo("3 years, 2 months and 2 days")
	}

	@Test
	void formatPeriodOmitsZeroValuedFields() {
		def value = new Period().withMonths(2).withDays(2).withMinutes(2)

		tagLib.formatPeriod(value: value)

		assertThat tagLib.output, equalTo("2 months, 2 days and 2 minutes")
	}

	@Test
	void formatPeriodHandlesErrorIfValueHasYearsOrMonthsAndFieldsDoesNot() {
		def value = new Period().withYears(1).withMonths(1).withDays(2).withMinutes(2)

		tagLib.formatPeriod(value: value, fields: "days,minutes")

		assertThat tagLib.output, equalTo("2 days and 2 minutes")
	}

	@Test
	void formatPeriodAcceptsDurationValue() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()

		tagLib.formatPeriod(value: value)

		assertThat tagLib.output, equalTo("8 hours, 12 minutes and 35 seconds")
	}

}