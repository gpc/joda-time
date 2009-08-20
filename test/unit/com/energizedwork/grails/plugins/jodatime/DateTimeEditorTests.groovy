package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.springframework.context.i18n.LocaleContextHolder
import static java.util.Locale.UK
import static java.util.Locale.US
import grails.test.GrailsUnitTestCase
import org.joda.time.DateTimeZone

class DateTimeEditorTests extends GrailsUnitTestCase {

	void testGetAsTextHandlesEmptyStringAsNull() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = null
		assertEquals "", editor.asText
	}

	void testSetAsTextHandlesEmptyStringAsNull() {
		def editor = new DateTimeEditor(LocalDate)
		editor.asText = ""
		assertNull editor.value
	}

	void testGetAsTextForLocalDateUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = UK
		assertEquals "29/11/71", editor.asText
	}

	void testGetAsTextForLocalDateUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = US
		assertEquals "11/29/71", editor.asText
	}

	void testGetAsTextForLocalDateUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDate="dd/MM/yyyy"
		'''
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		assertEquals "29/11/1971", editor.asText
	}

	void testSetAsTextForLocalDateUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = UK
		editor.asText = "29/11/71"
		assertEquals expected, editor.value
	}

	void testSetAsTextForLocalDateUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = US
		editor.asText = "11/29/71"
		assertEquals expected, editor.value

		shouldFail(IllegalArgumentException) {
			editor.asText = "29/11/71"
		}
	}

	void testSetAsTextForLocalDateUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDate="dd/MM/yyyy"
		'''
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		editor.asText = "29/11/1971"
		assertEquals expected, editor.value
	}

	void testGetAsTextForLocalDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = UK
		assertEquals "06/03/09 17:00", editor.asText
	}

	void testGetAsTextForLocalDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = US
		assertEquals "3/6/09 5:00 PM", editor.asText
	}

	void testGetAsTextForLocalDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDateTime="dd/MM/yyyy h:mm a"
		'''
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(1971, 11, 29, 17, 0)
		assertEquals "29/11/1971 5:00 PM", editor.asText
	}

	void testSetAsTextForLocalDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = UK
		editor.asText = "06/03/09 17:00"
		assertEquals expected, editor.value
	}

	void testSetAsTextForLocalDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = US
		editor.asText = "3/6/09 5:00 PM"
		assertEquals expected, editor.value

		shouldFail(IllegalArgumentException) {
			editor.asText = "06/30/09 17:00"
		}
	}

	void testSetAsTextForLocalDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDateTime="dd/MM/yyyy h:mm a"
		'''
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(1971, 11, 29, 17, 0)
		editor.asText = "29/11/1971 5:00 PM"
		assertEquals expected, editor.value
	}

	void testGetAsTextForDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = UK
		assertEquals "06/03/09 17:00", editor.asText
	}

	void testGetAsTextForDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = US
		assertEquals "3/6/09 5:00 PM", editor.asText
	}

	void testGetAsTextForDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="dd/MM/yyyy h:mm a Z"
		'''
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0).withZone(DateTimeZone.forID("GMT"))
		assertEquals "06/03/2009 5:00 PM +0000", editor.asText
	}

	void testSetAsTextForDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = UK
		editor.asText = "06/03/09 17:00"
		assertEquals expected, editor.value
	}

	void testSetAsTextForDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = US
		editor.asText = "3/6/09 5:00 PM"
		assertEquals expected, editor.value

		shouldFail(IllegalArgumentException) {
			editor.asText = "06/30/09 17:00"
		}
	}

	void testSetAsTextForDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="dd/MM/yyyy h:mm a Z"
		'''
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0).withZone(DateTimeZone.forID("GMT"))
		editor.asText = "06/03/2009 5:00 PM +0000"
		assertEquals expected, editor.value
	}

	void testGetAsTextForLocalTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		LocaleContextHolder.locale = UK
		assertEquals "23:59", editor.asText
	}

	void testGetAsTextForLocalTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		LocaleContextHolder.locale = US
		assertEquals "11:59 PM", editor.asText
	}

	void testGetAsTextForLocalTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalTime="h:mm a"
		'''
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		assertEquals "11:59 PM", editor.asText
	}

	void testSetAsTextForLocalTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		LocaleContextHolder.locale = UK
		editor.asText = "23:59"
		assertEquals expected, editor.value
	}

	void testSetAsTextForLocalTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		LocaleContextHolder.locale = US
		editor.asText = "11:59 PM"
		assertEquals expected, editor.value

		shouldFail(IllegalArgumentException) {
			editor.asText = "23:59"
		}
	}

	void testSetAsTextForLocalTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalTime="h:mm a"
		'''
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		editor.asText = "11:59 PM"
		assertEquals expected, editor.value
	}

}
