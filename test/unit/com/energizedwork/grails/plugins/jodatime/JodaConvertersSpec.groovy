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

import spock.lang.*
import grails.plugin.spock.*
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement
import org.joda.time.DateTime
import static org.joda.time.DateTimeZone.UTC

class JodaConvertersSpec extends UnitSpec {

	def "json converter can be used in unit test"() {
		given:
		JodaConverters.registerJsonAndXmlMarshallers()
		
		when:
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def json = marshalAsJSON(o)

		then:
		json.dateTime == "1970-01-01T00:00:00Z"
	}

	def "converters are removed at the end if unit tests"() {
		when:
		def o = [dateTime: new DateTime(0).withZone(UTC)]
		def json = marshalAsJSON(o)

		then:
		json.dateTime != "1970-01-01T00:00:00Z"
	}

	private JSONElement marshalAsJSON(object) {
		def sw = new StringWriter()
		(object as JSON).render(sw)
		def json = JSON.parse(sw.toString())
		println "marshalled $object to $sw"
		return json
	}

}
