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

import grails.plugin.spock.TagLibSpec
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import static org.hamcrest.Matchers.containsString
import org.joda.time.*
import spock.lang.*

class Html5InputTagLibSpec extends TagLibSpec {

	def setup() {
		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def formTagLib = new FormTagLib(out: tagLib.out)
		tagLib.metaClass.getG = { -> formTagLib }

		mockRequest.addPreferredLocale Locale.UK

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	def cleanup() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	@Unroll("#tag tag renders an HTML5 input")
	def "tags render HTML5 inputs"() {
		expect:
		"$tag"(name: "foo") == expectedOutput

		where:
		tag                  | expectedOutput
		"dateField"          | '<input type="date" name="foo" id="foo" value="" />'
		"timeField"          | '<input type="time" name="foo" id="foo" value="" />'
		"datetimeLocalField" | '<input type="datetime-local" name="foo" id="foo" value="" />'
		"monthField"         | '<input type="month" name="foo" id="foo" value="" />'
		"weekField"          | '<input type="week" name="foo" id="foo" value="" />'
		"datetimeField"      | '<input type="datetime" name="foo" id="foo" value="" />'
	}

	@Unroll("#tag tag renders its value in the correct format")
	def "tags render their values in the correct format"() {
		expect:
		"$tag"(name: "foo", value: new DateTime()) containsString("value=\"$expectedOutput\"")

		where:
		tag                  | value          | expectedOutput
		"dateField"          | new DateTime() | "2008-10-02"
		"timeField"          | new DateTime() | "02:50:33.000"
		"datetimeLocalField" | new DateTime() | "2008-10-02T02:50:33.000"
		"monthField"         | new DateTime() | "2008-10"
		"weekField"          | new DateTime() | "2008-W40"
	}

	def "datetimeField renders its value in the correct format for UTC"() {
		given:
		def value = new DateTime().toLocalDateTime().toDateTime(DateTimeZone.UTC)

		expect:
		datetimeField(name: "foo", value: value) containsString('value="2008-10-02T02:50:33.000Z"')
	}

	def "datetimeField renders its value in the correct format for non-UTC"() {
		given:
		def value = new DateTime().toLocalDateTime().toDateTime(DateTimeZone.forOffsetHours(-8))

		expect:
		datetimeField(name: "foo", value: value) containsString('value="2008-10-02T02:50:33.000-08:00"')
	}

	@Ignore
	def "datetimeField handles partial values"() {
		given:
		def value = new LocalDateTime()

		expect:
		datetimeField(name: "foo", value: value) containsString('value="2008-10-02T02:50:33.000Z"')
	}

}
