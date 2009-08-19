package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.joda.time.Period
import static com.energizedwork.grails.commons.test.Assertions.assertMatch
import static com.energizedwork.grails.commons.test.Assertions.assertNoMatch

class PeriodTagLibTests extends TagLibUnitTestCase {

	void setUp() {
		super.setUp()
		tagLib.metaClass.getOutput = {-> delegate.out.toString() }
		tagLib.metaClass.message = { attrs -> attrs.default }
	}

	void testPeriodPickerDefaultsIdFromName() {
		tagLib.periodPicker(name: "foo")
		["hours", "minutes", "seconds"].each {
			assertMatch(/<input type="text" name="foo_$it" id="foo_$it"/, tagLib.output)
		}
	}

	void testPeriodPickerUsesHoursMinutesAndSecondsByDefault() {
		tagLib.periodPicker(name: "foo")
		assertNoMatch(/<input type="text" name="foo_years"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_months"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_weeks"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_days"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_hours"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_minutes"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_seconds"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_millis"/, tagLib.output)
	}

	void testPeriodPickerUsesFieldsFromConfigIfPresent() {
		mockConfig '''
			jodatime.periodpicker.default.fields="years,months , days"
		'''
		tagLib.periodPicker(name: "foo")
		assertMatch(/<input type="text" name="foo_years"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_months"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_weeks"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_days"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_hours"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_minutes"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_seconds"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_millis"/, tagLib.output)
	}

	void testPeriodPickerAcceptsFieldsAttribute() {
		tagLib.periodPicker(name: "foo", fields: "years,months,days")
		assertMatch(/<input type="text" name="foo_years"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_months"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_weeks"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_days"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_hours"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_minutes"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_seconds"/, tagLib.output)
		assertNoMatch(/<input type="text" name="foo_millis"/, tagLib.output)
	}

	void testPeriodPickerAcceptsValueAttribute() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35)
		tagLib.periodPicker(name: "foo", value: value)
		assertMatch(/<input type="text" name="foo_hours" id="foo_hours" value="8"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/, tagLib.output)
	}

	void testPeriodPickerAcceptsDurationValue() {
		def value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()
		tagLib.periodPicker(name: "foo", value: value)
		assertMatch(/<input type="text" name="foo_hours" id="foo_hours" value="8"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_minutes" id="foo_minutes" value="12"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_seconds" id="foo_seconds" value="35"/, tagLib.output)
	}

	void testPeriodPickerUsesHourAsHighestFieldWhenValueIsDuration() {
		def value = new Period().withDays(1).withHours(1).toStandardDuration()
		tagLib.periodPicker(name: "foo", fields: "days,hours,minutes,seconds", value: value)
		assertMatch(/<input type="text" name="foo_days" id="foo_days" value="0"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_hours" id="foo_hours" value="25"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_minutes" id="foo_minutes" value="0"/, tagLib.output)
		assertMatch(/<input type="text" name="foo_seconds" id="foo_seconds" value="0"/, tagLib.output)
	}

	void testPeriodPickerHandlesNullValueAttribute() {
		tagLib.periodPicker(name: "foo", value: null)
		["hours", "minutes", "seconds"].each {
			assertMatch(/<input type="text" name="foo_$it" id="foo_$it" value="0"/, tagLib.output)
		}
	}

	void testPeriodPickerOutputsLabels() {
		tagLib.periodPicker(name: "foo", value: null)
		["hours", "minutes", "seconds"].each {
			assertMatch(/<label for="foo_$it"><input type="text" name="foo_$it"[^\/]*\/>&nbsp;$it <\/label>/, tagLib.output)
		}
	}

	void testPeriodPickerLabelsCanBeChangedWithMessageProperties() {
		def messages = [
				"org.joda.time.DurationFieldType.hours": "h",
				"org.joda.time.DurationFieldType.minutes": "m",
				"org.joda.time.DurationFieldType.seconds": "s"
		]
		tagLib.metaClass.message = { attrs -> messages[attrs.code] }
		tagLib.periodPicker(name: "foo")
		assertMatch(/<label for="foo_hours"><input type="text" name="foo_hours"[^\/]*\/>&nbsp;h <\/label>/, tagLib.output)
		assertMatch(/<label for="foo_minutes"><input type="text" name="foo_minutes"[^\/]*\/>&nbsp;m <\/label>/, tagLib.output)
		assertMatch(/<label for="foo_seconds"><input type="text" name="foo_seconds"[^\/]*\/>&nbsp;s <\/label>/, tagLib.output)
	}

}