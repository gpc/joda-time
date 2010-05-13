package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTimeZone
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.nullValue
import static org.junit.Assert.assertThat

class DateTimeZoneEditorTests {

	@Test
	void getAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.value = null
		assertThat editor.asText, equalTo("")
	}

	@Test
	void getAsTextReturnsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.value = DateTimeZone.forID("Europe/London")
		assertThat editor.asText, equalTo("Europe/London")
	}

	@Test
	void setAsTextAcceptsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.asText = "Europe/London"
		assertThat editor.value, equalTo(DateTimeZone.forID("Europe/London"))
	}

	@Test
	void setAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.asText = ""
		assertThat editor.value, nullValue()
	}

}