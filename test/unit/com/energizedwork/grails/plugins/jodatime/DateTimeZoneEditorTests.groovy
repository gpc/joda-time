package com.energizedwork.grails.plugins.jodatime

import grails.test.GrailsUnitTestCase
import org.joda.time.DateTimeZone

class DateTimeZoneEditorTests extends GrailsUnitTestCase {

	void testGetAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.value = null
		assertEquals "", editor.asText
	}
	
	void testGetAsTextReturnsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.value = DateTimeZone.forID("Europe/London")
		assertEquals "Europe/London", editor.asText
	}

	void testSetAsTextAcceptsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.asText = "Europe/London"
		assertEquals DateTimeZone.forID("Europe/London"), editor.value
	}

	void testSetAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.asText = ""
		assertNull editor.value
	}

}