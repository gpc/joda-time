package com.energizedwork.grails.plugins.jodatime

import org.joda.time.Period
import org.joda.time.Duration

class PeriodEditorTests extends GroovyTestCase {

	void testGetAsTextHandlesNull() {
		def editor = new PeriodEditor(Period)
		editor.value = null
		assertEquals "", editor.asText
	}

	void testSetAsTextHandlesNull() {
		def editor = new PeriodEditor(Period)
		editor.asText = null
		assertEquals null, editor.value
	}

	void testSetAsTextHandlesEmptyString() {
		def editor = new PeriodEditor(Period)
		editor.asText = ""
		assertEquals null, editor.value
	}

	void testGetAsTextFormatsValueCorrectly() {
		def editor = new PeriodEditor(Period)
		editor.value = new Period(1, 2, 0, 4, 8, 12, 35, 0)
		assertEquals "1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds", editor.asText
	}

	void testGetAsTextWithValueOverStandardRange() {
		def editor = new PeriodEditor(Period)
		editor.value = new Period(0, 120, 12, 0)
		assertEquals "120 minutes and 12 seconds", editor.asText
	}

	void testSetAsTextParsesValueCorrectly() {
		def editor = new PeriodEditor(Period)
		editor.asText = "1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds"
		assertEquals new Period(1, 2, 0, 4, 8, 12, 35, 0), editor.value
	}

	void testSetAsTextSupportsDuration() {
		def editor = new PeriodEditor(Duration)
		editor.asText = "1 hour, 35 minutes and 16 seconds"
		assertEquals new Period(1, 35, 16, 0).toStandardDuration(), editor.value
	}

	void testGetAsTextSupportsDuration() {
		def editor = new PeriodEditor(Duration)
		editor.value = new Period(1, 35, 16, 0).toStandardDuration()
		assertEquals "1 hour, 35 minutes and 16 seconds", editor.asText
	}

}