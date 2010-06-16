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
import org.junit.Test
import static org.hamcrest.Matchers.equalTo
import org.joda.time.*
import static org.joda.time.DateTimeZone.UTC
import static org.junit.Assert.assertThat

class ConversionTests {

	@Test
	void dateTimeAsJSON() {
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def json = marshalAsJSON(o)
		assertThat "DateTime as JSON", json.dateTime, equalTo("1970-01-01T00:00:00Z")
	}

	@Test
	void dateTimeAsXML() {
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def xml = marshalAsXML(o)
		assertThat "DateTime as XML", xml.entry.find {it.'@key' == 'dateTime'}?.text(), equalTo("1970-01-01T00:00:00Z")
	}

	@Test
	void dateTimeAsJSONHandlesZones() {
		def o = [dateTime: new DateTime(0).withZone(DateTimeZone.forOffsetHours(-5))]
		def json = marshalAsJSON(o)
		assertThat "DateTime with zone as JSON", json.dateTime, equalTo("1969-12-31T19:00:00-05:00")
	}

	@Test
	void dateTimeAsXMLHandlesZones() {
		def o = [dateTime: new DateTime(0).withZone(DateTimeZone.forOffsetHours(3))]
		def xml = marshalAsXML(o)
		assertThat "DateTime with zone as XML", xml.entry.find {it.'@key' == 'dateTime'}?.text(), equalTo("1970-01-01T03:00:00+03:00")
	}

	@Test
	void localDateAsJSON() {
		def o = [localDate: new LocalDate(2009, 8, 2)]
		def json = marshalAsJSON(o)
		assertThat "LocalDate as JSON", json.localDate, equalTo("2009-08-02")
	}

	@Test
	void localTimeAsJSON() {
		def o = [localTime: new LocalTime(6, 29)]
		def json = marshalAsJSON(o)
		assertThat "LocalTime as JSON", json.localTime, equalTo("06:29:00")
	}

	@Test
	void localDateTimeAsJSON() {
		def o = [localDateTime: new LocalDateTime(2009, 7, 13, 6, 29)]
		def json = marshalAsJSON(o)
		assertThat "LocalDateTime as JSON", json.localDateTime, equalTo("2009-07-13T06:29:00")
	}

	@Test
	void dateTimeZoneAsJSON() {
		def o = [dateTimeZone: DateTimeZone.forID("America/Vancouver")]
		def json = marshalAsJSON(o)
		assertThat "DateTimeZone as JSON", json.dateTimeZone, equalTo("America/Vancouver")
	}

	@Test
	void localDateAsXML() {
		def o = [localDate: new LocalDate(2009, 8, 2)]
		def xml = marshalAsXML(o)
		assertThat "LocalDate as XML", xml.entry.find {it.'@key' == 'localDate'}?.text(), equalTo("2009-08-02")
	}

	@Test
	void localTimeAsXML() {
		def o = [localTime: new LocalTime(6, 29)]
		def xml = marshalAsXML(o)
		assertThat "LocalTime as XML", xml.entry.find {it.'@key' == 'localTime'}?.text(), equalTo("06:29:00")
	}

	@Test
	void localDateTimeAsXML() {
		def o = [localDateTime: new LocalDateTime(2009, 7, 13, 6, 29)]
		def xml = marshalAsXML(o)
		assertThat "LocalDateTime as XML", xml.entry.find {it.'@key' == 'localDateTime'}?.text(), equalTo("2009-07-13T06:29:00")
	}

	@Test
	void dateTimeZoneAsXML() {
		def o = [dateTimeZone: DateTimeZone.forID("America/Vancouver")]
		def xml = marshalAsXML(o)
		assertThat "DateTimeZone as XML", xml.entry.find {it.'@key' == 'dateTimeZone'}?.text(), equalTo("America/Vancouver")
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