package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.StructuredDateTimeEditor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.DateTimeZone

class StructuredDateTimeEditorTests extends GroovyTestCase {

	void testAssembleHandlesNoFieldsAsNull() {
		def editor = new StructuredDateTimeEditor(LocalDate)
		editor.assemble(LocalDate, [:])
		assertNull "Property editor should have set value to null", editor.value
	}

	void testAssembleHandlesEmptyFieldsAsNull() {
		def editor = new StructuredDateTimeEditor(LocalDate)
		editor.assemble(LocalDate, [year: "", month: "", day: ""])
		assertNull "Property editor should have set value to null", editor.value
	}

	void testAssembleRequiresYearForDateTypes() {
		def editor = new StructuredDateTimeEditor(LocalDate)
		shouldFail(IllegalArgumentException) {
			editor.assemble(LocalDate, [month: 11, day: 29])
		}
	}

	void testAssembleLocalDateUsingOnlyYear() {
		def editor = new StructuredDateTimeEditor(LocalDate)
		def expected = new LocalDate(1977, 1, 1)
		def actual = editor.assemble(LocalDate, [year: '1977'])
		assertEquals(expected, actual)
	}

	void testAssembleLocalDateUsingYearMonthDay() {
		def editor = new StructuredDateTimeEditor(LocalDate)
		def expected = new LocalDate(2009, 2, 20)
		def actual = editor.assemble(LocalDate, [year: '2009', month: '02', day: '20'])
		assertEquals(expected, actual)
	}

	void testAssembleLocalDateTimeUsingOnlyYear() {
		def editor = new StructuredDateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 1, 1, 0, 0)
		def actual = editor.assemble(LocalDateTime, [year: '2009'])
		assertEquals(expected, actual)
	}

	void testAssembleLocalDateUsingYearMonthDayHourMinute() {
		def editor = new StructuredDateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 3, 6, 17, 21, 33)
		def actual = editor.assemble(LocalDateTime, [year: '2009', month: '03', day: '06', hour: '17', minute: '21', second: '33'])
		assertEquals(expected, actual)
	}

	void testAssembleDateTimeUsingOnlyYear() {
		def editor = new StructuredDateTimeEditor(DateTime)
		def expected = new DateTime(2009, 1, 1, 0, 0, 0, 0)
		def actual = editor.assemble(DateTime, [year: '2009'])
		assertEquals(expected, actual)
	}

	void testAssembleDateUsingYearMonthDayHourMinute() {
		def editor = new StructuredDateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 21, 33, 0)
		def actual = editor.assemble(DateTime, [year: '2009', month: '03', day: '06', hour: '17', minute: '21', second: '33'])
		assertEquals(expected, actual)
	}

	void testAssembleRequiresHourForTimeTypes() {
		def editor = new StructuredDateTimeEditor(LocalTime)
		shouldFail(IllegalArgumentException) {
			editor.assemble(LocalTime, [minute: 29])
		}
	}

	void testAssembleLocalTimeUsingOnlyHour() {
		def editor = new StructuredDateTimeEditor(LocalTime)
		def expected = new LocalTime(17, 0)
		def actual = editor.assemble(LocalTime, [hour: '17'])
		assertEquals(expected, actual)
	}

	void testAssembleLocalTimeUsingHourMinute() {
		def editor = new StructuredDateTimeEditor(LocalTime)
		def expected = new LocalTime(17, 55, 33)
		def actual = editor.assemble(LocalTime, [hour: '17', minute: '55', second: '33'])
		assertEquals(expected, actual)
	}

	void testAssembleDateTimeWithDefaultZone() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			def editor = new StructuredDateTimeEditor(DateTime)
			def expected = new DateTime(2009, 8, 24, 13, 6, 0, 0).withZoneRetainFields(DateTimeZone.forID("Europe/London"))
			def actual = editor.assemble(DateTime, [year: "2009", month: "08", day: "24", hour: "13", minute: "06"])
			assertEquals(expected, actual)
		}
	}

	void testAssembleDateTimeWithSpecifiedZone() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			def editor = new StructuredDateTimeEditor(DateTime)
			def expected = new DateTime(2009, 8, 24, 13, 6, 0, 0).withZoneRetainFields(DateTimeZone.forID("America/Vancouver"))
			def actual = editor.assemble(DateTime, [year: "2009", month: "08", day: "24", hour: "13", minute: "06", zone: "America/Vancouver"])
			assertEquals(expected, actual)
		}
	}

}