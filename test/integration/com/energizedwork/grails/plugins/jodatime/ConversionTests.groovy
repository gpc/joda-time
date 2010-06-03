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

import grails.converters.JSON
import grails.converters.XML
import org.codehaus.groovy.grails.web.json.JSONElement
import org.joda.time.*
import static org.joda.time.DateTimeZone.UTC

class ConversionTests extends GroovyTestCase {

	void testDateTimeAsJSON() {
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def json = marshalAsJSON(o)
		assertEquals "1970-01-01T00:00:00Z", json.dateTime
	}

	void testDateTimeAsXML() {
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def xml = marshalAsXML(o)
		assertEquals "1970-01-01T00:00:00Z", xml.entry.find {it.'@key' == 'dateTime'}?.text()
	}

	void testDateTimeAsJSONHandlesZones() {
		def o = [dateTime: new DateTime(0).withZone(DateTimeZone.forOffsetHours(-5))]
		def json = marshalAsJSON(o)
		assertEquals "1969-12-31T19:00:00-05:00", json.dateTime
	}

	void testDateTimeAsXMLHandlesZones() {
		def o = [dateTime: new DateTime(0).withZone(DateTimeZone.forOffsetHours(3))]
		def xml = marshalAsXML(o)
		assertEquals "1970-01-01T03:00:00+03:00", xml.entry.find {it.'@key' == 'dateTime'}?.text()
	}

	void testLocalDateAsJSON() {
		def o = [localDate: new LocalDate(2009, 8, 2)]
		def json = marshalAsJSON(o)
		assertEquals "2009-08-02", json.localDate
	}

	void testLocalTimeAsJSON() {
		def o = [localTime: new LocalTime(6, 29)]
		def json = marshalAsJSON(o)
		assertEquals "06:29:00", json.localTime
	}

	void testLocalDateTimeAsJSON() {
		def o = [localDateTime: new LocalDateTime(2009, 7, 13, 6, 29)]
		def json = marshalAsJSON(o)
		assertEquals "2009-07-13T06:29:00", json.localDateTime
	}

	void testDateTimeZoneAsJSON() {
		def o = [dateTimeZone: DateTimeZone.forID("America/Vancouver")]
		def json = marshalAsJSON(o)
		assertEquals "America/Vancouver", json.dateTimeZone
	}

	void testLocalDateAsXML() {
		def o = [localDate: new LocalDate(2009, 8, 2)]
		def xml = marshalAsXML(o)
		assertEquals "2009-08-02", xml.entry.find {it.'@key' == 'localDate'}?.text()
	}

	void testLocalTimeAsXML() {
		def o = [localTime: new LocalTime(6, 29)]
		def xml = marshalAsXML(o)
		assertEquals "06:29:00", xml.entry.find {it.'@key' == 'localTime'}?.text()
	}

	void testLocalDateTimeAsXML() {
		def o = [localDateTime: new LocalDateTime(2009, 7, 13, 6, 29)]
		def xml = marshalAsXML(o)
		assertEquals "2009-07-13T06:29:00", xml.entry.find {it.'@key' == 'localDateTime'}?.text()
	}

	void testDateTimeZoneAsXML() {
		def o = [dateTimeZone: DateTimeZone.forID("America/Vancouver")]
		def xml = marshalAsXML(o)
		assertEquals "America/Vancouver", xml.entry.find {it.'@key' == 'dateTimeZone'}?.text()
	}

	private JSONElement marshalAsJSON(object) {
		def sw = new StringWriter()
		(object as JSON).render(sw)
		def json = JSON.parse(sw.toString())
		println "marshalled $object to $sw"
		return json
	}

	private marshalAsXML(object) {
		def sw = new StringWriter()
		(object as XML).render(sw)
		def xml = XML.parse(sw.toString())
		println "marshalled $object to $sw"
		return xml
	}

}