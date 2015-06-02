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
package grails.plugins.jodatime.converters

import grails.converters.JSON
import grails.converters.XML
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import groovy.transform.CompileStatic
import org.grails.web.json.JSONElement
import org.joda.time.*
import spock.lang.Specification
import spock.lang.Unroll

import static org.joda.time.DateTimeZone.UTC

@TestMixin(ControllerUnitTestMixin)
@Unroll
class ConversionSpec extends Specification {

	void setup() {
		JodaConverters.registerJsonAndXmlMarshallers()
	}

	def "can marshal a #value.class.simpleName object to XML"() {
		given:
		def o = [value: value]

		when:
		def xml = marshalAsXML(o)

		then:
		xml.entry.find { it."@key" == "value" }?.text() == xmlForm

		where:
		value                                                    | xmlForm
		new DateTime(0).withZone(UTC)                            | "1970-01-01T00:00:00.000Z"
		new DateTime(0).withZone(DateTimeZone.forOffsetHours(3)) | "1970-01-01T03:00:00.000+03:00"
		new LocalDate(2009, 8, 2)                                | "2009-08-02"
		new LocalTime(6, 29)                                     | "06:29:00.000"
		new LocalDateTime(2009, 7, 13, 6, 29)                    | "2009-07-13T06:29:00.000"
		DateTimeZone.forID("America/Vancouver")                  | "America/Vancouver"
	}

	def "can marshal a #value.class.simpleName object to JSON"() {
		given:
		def o = [value: value]

		when:
		def json = marshalAsJSON(o)

		then:
		json.value == jsonForm

		where:
		value                                                     | jsonForm
		new DateTime(0).withZone(UTC)                             | "1970-01-01T00:00:00.000Z"
		new DateTime(0).withZone(DateTimeZone.forOffsetHours(-5)) | "1969-12-31T19:00:00.000-05:00"
		new LocalDate(2009, 8, 2)                                 | "2009-08-02"
		new LocalTime(6, 29)                                      | "06:29:00.000"
		new LocalDateTime(2009, 7, 13, 6, 29)                     | "2009-07-13T06:29:00.000"
		DateTimeZone.forID("America/Vancouver")                   | "America/Vancouver"
	}

	@CompileStatic
	private JSONElement marshalAsJSON(object) {
		def sw = new StringWriter()
		(object as JSON).render(sw)
		def json = JSON.parse(sw.toString())
		return json
	}

	@CompileStatic
	private marshalAsXML(object) {
		def sw = new StringWriter()
		(object as XML).render(sw)
		def xml = XML.parse(sw.toString())
		return xml
	}

}
