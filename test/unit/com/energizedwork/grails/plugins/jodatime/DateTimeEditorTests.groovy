package com.energizedwork.grails.plugins.jodatime

import grails.test.GrailsUnitTestCase
import org.junit.Test
import org.springframework.context.i18n.LocaleContextHolder
import static java.util.Locale.UK
import static java.util.Locale.US
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.nullValue
import org.joda.time.*
import static org.junit.Assert.assertThat

class DateTimeEditorTests extends GrailsUnitTestCase {

	@Test
	void getAsTextHandlesEmptyStringAsNull() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = null
		assertThat editor.asText, equalTo("")
	}

	@Test
	void setAsTextHandlesEmptyStringAsNull() {
		def editor = new DateTimeEditor(LocalDate)
		editor.asText = ""
		assertThat editor.value, nullValue()
	}

	@Test
	void getAsTextForLocalDateUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = UK
		assertThat editor.asText, equalTo("29/11/71")
	}

	@Test
	void getAsTextForLocalDateUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = US
		assertThat editor.asText, equalTo("11/29/71")
	}

	@Test
	void getAsTextForLocalDateUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDate="dd/MM/yyyy"
		'''
		def editor = new DateTimeEditor(LocalDate)
		editor.value = new LocalDate(1971, 11, 29)
		assertThat editor.asText, equalTo("29/11/1971")
	}

	@Test
	void setAsTextForLocalDateUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = UK
		editor.asText = "29/11/71"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void setAsTextForLocalDateUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		LocaleContextHolder.locale = US
		editor.asText = "11/29/71"
		assertThat editor.value, equalTo(expected)

		shouldFail(IllegalArgumentException) {
			editor.asText = "29/11/71"
		}
	}

	@Test
	void setAsTextForLocalDateUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDate="dd/MM/yyyy"
		'''
		def editor = new DateTimeEditor(LocalDate)
		def expected = new LocalDate(1971, 11, 29)
		editor.asText = "29/11/1971"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void getAsTextForLocalDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = UK
		assertThat editor.asText, equalTo("06/03/09 17:00")
	}

	@Test
	void getAsTextForLocalDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = US
		assertThat editor.asText, equalTo("3/6/09 5:00 PM")
	}

	@Test
	void getAsTextForLocalDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDateTime="dd/MM/yyyy h:mm a"
		'''
		def editor = new DateTimeEditor(LocalDateTime)
		editor.value = new LocalDateTime(1971, 11, 29, 17, 0)
		assertThat editor.asText, equalTo("29/11/1971 5:00 PM")
	}

	@Test
	void setAsTextForLocalDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = UK
		editor.asText = "06/03/09 17:00"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void setAsTextForLocalDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(2009, 3, 6, 17, 0)
		LocaleContextHolder.locale = US
		editor.asText = "3/6/09 5:00 PM"
		assertThat editor.value, equalTo(expected)

		shouldFail(IllegalArgumentException) {
			editor.asText = "06/30/09 17:00"
		}
	}

	@Test
	void setAsTextForLocalDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalDateTime="dd/MM/yyyy h:mm a"
		'''
		def editor = new DateTimeEditor(LocalDateTime)
		def expected = new LocalDateTime(1971, 11, 29, 17, 0)
		editor.asText = "29/11/1971 5:00 PM"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void getAsTextForDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = UK
		assertThat editor.asText, equalTo("06/03/09 17:00")
	}

	@Test
	void getAsTextForDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = US
		assertThat editor.asText, equalTo("3/6/09 5:00 PM")
	}

	@Test
	void getAsTextForDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="dd/MM/yyyy h:mm a Z"
		'''
		def editor = new DateTimeEditor(DateTime)
		editor.value = new DateTime(2009, 3, 6, 17, 0, 0, 0).withZone(DateTimeZone.forID("GMT"))
		assertThat editor.asText, equalTo("06/03/2009 5:00 PM +0000")
	}

	@Test
	void setAsTextForDateTimeUsingUKLocale() {
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = UK
		editor.asText = "06/03/09 17:00"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void setAsTextForDateTimeUsingUSLocale() {
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0)
		LocaleContextHolder.locale = US
		editor.asText = "3/6/09 5:00 PM"
		assertThat editor.value, equalTo(expected)

		shouldFail(IllegalArgumentException) {
			editor.asText = "06/30/09 17:00"
		}
	}

	@Test
	void setAsTextForDateTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="dd/MM/yyyy h:mm a Z"
		'''
		def editor = new DateTimeEditor(DateTime)
		def expected = new DateTime(2009, 3, 6, 17, 0, 0, 0).withZone(DateTimeZone.forID("GMT"))
		editor.asText = "06/03/2009 5:00 PM +0000"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void getAsTextForLocalTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		LocaleContextHolder.locale = UK
		assertThat editor.asText, equalTo("23:59")
	}

	@Test
	void getAsTextForLocalTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		LocaleContextHolder.locale = US
		assertThat editor.asText, equalTo("11:59 PM")
	}

	@Test
	void getAsTextForLocalTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalTime="h:mm a"
		'''
		def editor = new DateTimeEditor(LocalTime)
		editor.value = new LocalTime(23, 59)
		assertThat editor.asText, equalTo("11:59 PM")
	}

	@Test
	void setAsTextForLocalTimeUsingUKLocale() {
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		LocaleContextHolder.locale = UK
		editor.asText = "23:59"
		assertThat editor.value, equalTo(expected)
	}

	@Test
	void setAsTextForLocalTimeUsingUSLocale() {
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		LocaleContextHolder.locale = US
		editor.asText = "11:59 PM"
		assertThat editor.value, equalTo(expected)

		shouldFail(IllegalArgumentException) {
			editor.asText = "23:59"
		}
	}

	@Test
	void setAsTextForLocalTimeUsingConfiguredPattern() {
		mockConfig '''
			jodatime.format.org.joda.time.LocalTime="h:mm a"
		'''
		def editor = new DateTimeEditor(LocalTime)
		def expected = new LocalTime(23, 59)
		editor.asText = "11:59 PM"
		assertThat editor.value, equalTo(expected)
	}

}
