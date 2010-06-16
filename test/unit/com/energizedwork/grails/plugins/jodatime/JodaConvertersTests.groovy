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
import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.web.json.JSONElement
import org.joda.time.DateTime
import org.junit.Test
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not
import static org.joda.time.DateTimeZone.UTC
import static org.junit.Assert.assertThat

class JodaConvertersTests extends GrailsUnitTestCase {

	@Test
	void jsonConverterCanBeUsedInUnitTest() {
		JodaConverters.registerJsonAndXmlMarshallers()
		def o = [dateTime: new DateTime(0).withZone(UTC)]

		def json = marshalAsJSON(o)

		assertThat "DateTime as JSON", json.dateTime, equalTo("1970-01-01T00:00:00Z")
	}

	@Test
	void convertersAreRemovedAtTheEndOfUnitTests() {
		def o = [dateTime: new DateTime(0).withZone(UTC)]

		def json = marshalAsJSON(o)

		assertThat "DateTime as JSON", json.dateTime, not(equalTo("1970-01-01T00:00:00Z"))
	}

	private JSONElement marshalAsJSON(object) {
		def sw = new StringWriter()
		(object as JSON).render(sw)
		def json = JSON.parse(sw.toString())
		println "marshalled $object to $sw"
		return json
	}

}
