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

import org.codehaus.groovy.grails.web.json.JSONElement
import grails.converters.*
import org.joda.time.*
import static org.joda.time.DateTimeZone.UTC
import spock.lang.*

class ConversionSpec extends Specification {

	@Unroll({"can marshal a ${value.getClass().simpleName} object to XML"})
	def "XML marshalling"() {
		given:
		def o = [value: value]

		when:
		def xml = marshalAsXML(o)

		then:
		xml.entry.find { it."@key" == "value" }?.text() == xmlForm

		where:
		value                                                    | xmlForm
		new DateTime(0).withZone(UTC)                            | "1970-01-01T00:00:00Z"
		new DateTime(0).withZone(DateTimeZone.forOffsetHours(3)) | "1970-01-01T03:00:00+03:00"
		new LocalDate(2009, 8, 2)                                | "2009-08-02"
		new LocalTime(6, 29)                                     | "06:29:00"
		new LocalDateTime(2009, 7, 13, 6, 29)                    | "2009-07-13T06:29:00"
		DateTimeZone.forID("America/Vancouver")                  | "America/Vancouver"
	}

	@Unroll({"can marshal a ${value.getClass().simpleName} object to JSON"})
	def "JSON marshalling"() {
		given:
		def o = [value: value]

		when:
		def json = marshalAsJSON(o)

		then:
		json.value == jsonForm

		where:
		value                                                     | jsonForm
		new DateTime(0).withZone(UTC)                             | "1970-01-01T00:00:00Z"
		new DateTime(0).withZone(DateTimeZone.forOffsetHours(-5)) | "1969-12-31T19:00:00-05:00"
		new LocalDate(2009, 8, 2)                                 | "2009-08-02"
		new LocalTime(6, 29)                                      | "06:29:00"
		new LocalDateTime(2009, 7, 13, 6, 29)                     | "2009-07-13T06:29:00"
		DateTimeZone.forID("America/Vancouver")                   | "America/Vancouver"
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