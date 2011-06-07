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

import org.joda.time.DateTimeZone
import org.joda.time.DateTimeUtils
import org.joda.time.DateTime
import org.junit.*
import grails.test.mixin.*
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

@TestFor(DateTimeZoneTagLib)
class DateTimeZoneTagLibTests {

	def defaultZone

	@Before void setUp() {
		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		defaultZone = DateTimeZone.default
	}

	@After void tearDown() {
		DateTimeZone.default = defaultZone
		DateTimeUtils.setCurrentMillisSystem()
	}

	@Test void currentZoneIsSelectedByDefault() {
		assert applyTemplate('<joda:dateTimeZoneSelect name="foo"/>') =~ /<option value="${DateTimeZone.default.ID}" selected="selected" >/
	}

	@Test void valueAttributeIsPassedToSelect() {
		def zone = DateTimeZone.forID("Canada/Pacific")
		assert applyTemplate('<joda:dateTimeZoneSelect name="foo" value="${zone}"/>', [zone: zone]) =~ /<option value="${zone.ID}" selected="selected" >/
	}

	@Test void optionFormatting() {
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		def output = applyTemplate('<joda:dateTimeZoneSelect name="foo"/>')
		
		assert output.contains(">America/Vancouver -08:00<")
		assert output.contains(">Europe/London +00:00<")
	}

	@Test void optionsAreDSTSensetive() {
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 8, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		def output = applyTemplate('<joda:dateTimeZoneSelect name="foo"/>')
		
		assert output.contains(">America/Vancouver -07:00<")
		assert output.contains(">Europe/London +01:00<")
	}

	@Ignore @Test void noDuplicateOptionsAppear() {
		tagLib.dateTimeZoneSelect([:])
		def options = selectAttrs.from.collect {
			selectAttrs.optionValue(it)
		}
		assertEquals options.unique(), options
	}
}
