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
package grails.plugins.jodatime.taglib

import grails.test.mixin.TestFor
import org.grails.plugins.codecs.HTMLCodec
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(DateTimeZoneTagLib)
class DateTimeZoneTagLibSpec extends Specification {

	private defaultZone

	void setup() {
		mockCodec HTMLCodec

		defaultZone = DateTimeZone.default
	}

	void cleanup() {
		DateTimeZone.default = defaultZone
		DateTimeUtils.setCurrentMillisSystem()
	}

	void "current zone is selected by default"() {
		expect:
		applyTemplate('<joda:dateTimeZoneSelect name="foo"/>') =~ /<option value="${DateTimeZone.default.ID}" selected="selected" >/
	}

	void "value attribute is passed to select"() {
		given:
		def zone = DateTimeZone.forID("Canada/Pacific")

		expect:
		applyTemplate('<joda:dateTimeZoneSelect name="foo" value="${zone}"/>', [zone: zone]) =~ /<option value="${zone.ID}" selected="selected" >/
	}

	void "option formatting"() {
		given:
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		when:
		def output = applyTemplate('<joda:dateTimeZoneSelect name="foo"/>')

		then:
		output.contains(">America/Vancouver -08:00<")
		output.contains(">Europe/London +00:00<")
	}

	void "options are DST sensitive"() {
		given:
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 8, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		when:
		def output = applyTemplate('<joda:dateTimeZoneSelect name="foo"/>')

		then:
		output.contains(">America/Vancouver -07:00<")
		output.contains(">Europe/London +01:00<")
	}

	@Ignore
	void "no duplicate options appear"() {
		tagLib.dateTimeZoneSelect([:])
		def options = selectAttrs.from.collect {
			selectAttrs.optionValue(it)
		}
		assertEquals options.unique(), options
	}
}
