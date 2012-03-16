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

package grails.plugin.jodatime.taglib

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import grails.test.mixin.*
import org.joda.time.*
import static org.joda.time.DateTimeZone.UTC
import spock.lang.*

@TestFor(Html5InputTagLib)
@Mock(FormattingTagLib)
@Unroll
class Html5InputTagLibSpec extends Specification {

	def setup() {
		mockCodec HTMLCodec

		tagLib.request.addPreferredLocale Locale.UK

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	def cleanup() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	def "#tag tag renders an HTML5 input"() {
		expect:
		applyTemplate("<joda:$tag name=\"foo\"/>") == expectedOutput

		where:
		tag                  | expectedOutput
		"dateField"          | '<input type="date" name="foo" id="foo" value="" />'
		"timeField"          | '<input type="time" name="foo" id="foo" value="" />'
		"datetimeLocalField" | '<input type="datetime-local" name="foo" id="foo" value="" />'
		"monthField"         | '<input type="month" name="foo" id="foo" value="" />'
		"weekField"          | '<input type="week" name="foo" id="foo" value="" />'
		"datetimeField"      | '<input type="datetime" name="foo" id="foo" value="" />'
	}

	def "#tag tag renders its value in the correct format"() {
		expect:
		applyTemplate("<joda:$tag name=\"foo\" value=\"\${value}\"/>", [value: new DateTime()]).contains("value=\"$expectedOutput\"")

		where:
		tag                  | expectedOutput
		"dateField"          | "2008-10-02"
		"timeField"          | "02:50:33.000"
		"datetimeLocalField" | "2008-10-02T02:50:33.000"
		"monthField"         | "2008-10"
		"weekField"          | "2008-W40"
	}

	def "datetimeField renders its value in the correct format for UTC"() {
		given:
		def value = new DateTime().toLocalDateTime().toDateTime(UTC)

		expect:
		applyTemplate('<joda:datetimeField name="foo" value="${value}"/>', [value: value]).contains('value="2008-10-02T02:50:33.000Z"')
	}

	def "datetimeField renders its value in the correct format for non-UTC"() {
		given:
		def value = new DateTime().toLocalDateTime().toDateTime(DateTimeZone.forOffsetHours(-8))

		expect:
		applyTemplate('<joda:datetimeField name="foo" value="${value}"/>', [value: value]).contains('value="2008-10-02T02:50:33.000-08:00"')
	}

	@Ignore
	def "datetimeField handles partial values"() {
		given:
		def value = new LocalDateTime()

		expect:
		applyTemplate('<joda:datetimeField name="foo" value="${value}"/>', [value: value]).contains('value="2008-10-02T02:50:33.000Z"')
	}

	def "joda:time rejects a #value.class.simpleName value attribute"() {
		when:
		applyTemplate('<joda:time value="${value}">body</joda:time>', [value: value])

		then:
		thrown GrailsTagException

		where:
		value << ["a string", new Date(), new YearMonth()]
	}

	def "joda:time does nothing if passed a null value"() {
		expect:
		applyTemplate('<joda:time value="${value}">body</joda:time>', [value: null]) == ""
	}

	def "joda:time defaults the value to current time if no attribute is passed at all"() {
		expect:
		applyTemplate('<joda:time>body</joda:time>') ==~ /<time datetime="\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(Z|[-\+]\d{2}:\d{2})">body<\/time>/
	}

	def "joda:time outputs a time element with a datetime attribute '#datetimeAttribute' for the value #value"() {
		expect:
		applyTemplate('<joda:time value="${value}">body</joda:time>', [value: value]) == "<time datetime=\"$datetimeAttribute\">body</time>"

		where:
		value                                                                             | datetimeAttribute
		new LocalTime(8, 25)                                                              | "08:25"
		new LocalDate(2008, 10, 2)                                                        | "2008-10-02"
		new LocalDateTime(2008, 10, 2, 13, 15)                                            | "2008-10-02T13:15"
		new LocalDateTime(2008, 10, 2, 1, 50).toDateTime(UTC)                             | "2008-10-02T01:50Z"
		new LocalDateTime(2008, 10, 2, 1, 50).toDateTime(DateTimeZone.forOffsetHours(-8)) | "2008-10-02T01:50-08:00"
	}

	def "joda:time passes the value to its body"() {
		expect:
		applyTemplate('<joda:time value="${value}">${it.toString("MMMM d yyyy")}</joda:time>', [value: new LocalDate(2008, 10, 2)]) == '<time datetime="2008-10-02">October 2 2008</time>'
	}

	def "joda:time accepts a var attribute"() {
		expect:
		applyTemplate('<joda:time value="${value}" var="theDate">${theDate.toString("MMMM d yyyy")}</joda:time>', [value: new LocalDate(2008, 10, 2)]) == '<time datetime="2008-10-02">October 2 2008</time>'
	}

	def "joda:time outputs default text for a #value.class.simpleName value if the body is omitted"() {
		expect:
		applyTemplate('<joda:time value="${value}"/>', [value: value]) == expectedOutput

		where:
		value                                                 | expectedOutput
		new LocalDate(2008, 10, 2)                            | '<time datetime="2008-10-02">02-Oct-2008</time>'
		new LocalDateTime(2008, 10, 2, 1, 50).toDateTime(UTC) | '<time datetime="2008-10-02T01:50Z">02-Oct-2008 01:50:00</time>'
	}

	def "joda:time can accept other attributes"() {
		expect:
		applyTemplate('<joda:time value="${value}" pubdate=""/>', [value: new LocalDate(2008, 10, 2)]) == '<time datetime="2008-10-02" pubdate="">02-Oct-2008</time>'
	}

}
