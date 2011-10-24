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

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.LocalDateTime

import spock.lang.*

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

@TestFor(DateTimeTagLib)
class DateTimeTagLibSpec extends Specification {

	def setup() {
		mockCodec HTMLCodec
//		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	def cleanup() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	def "datePicker outputs only date fields"() {
		when:
		def output = applyTemplate('<joda:datePicker name="foo"/>')

		then:
		output =~ /<select name="foo_day"/
		output =~ /<select name="foo_month"/
		output =~ /<select name="foo_year"/
		!output.contains(/<select name="foo_hour"/)
		!output.contains(/<select name="foo_minute"/)
		!output.contains(/<select name="foo_second"/)
	}

	def "timePicker outputs only time fields"() {
		when:
		def output = applyTemplate('<joda:timePicker name="foo" precision="second"/>')

		then:
		!output.contains(/<select name="foo_day"/)
		!output.contains(/<select name="foo_month"/)
		!output.contains(/<select name="foo_year"/)
		output =~ /<select name="foo_hour"/
		output =~ /<select name="foo_minute"/
		output =~ /<select name="foo_second"/
	}

	def "picker tags use current date & time as default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" precision="second"/>')

		then:
		output =~ /<option value="2" selected="selected">2<\/option>/
		output =~ /<option value="10" selected="selected">October<\/option>/
		output =~ /<option value="2008" selected="selected">2008<\/option>/
		output =~ /<option value="2" selected="selected">2<\/option>/
		output =~ /<option value="50" selected="selected">50<\/option>/
		output =~ /<option value="33" selected="selected">33<\/option>/
	}

	def "picker tags accept string default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="1971-11-29T16:22"/>')

		then:
		output =~ /<option value="29" selected="selected">29<\/option>/
		output =~ /<option value="11" selected="selected">November<\/option>/
		output =~ /<option value="1971" selected="selected">1971<\/option>/
		output =~ /<option value="16" selected="selected">16<\/option>/
		output =~ /<option value="22" selected="selected">22<\/option>/
	}

	def "string default format is appropriate for precision"() {
		when:
		def output = applyTemplate('<joda:datePicker name="foo" default="1971-11-29"/>')

		then:
		output =~ /<option value="29" selected="selected">29<\/option>/
		output =~ /<option value="11" selected="selected">November<\/option>/
		output =~ /<option value="1971" selected="selected">1971<\/option>/
	}

	def "timePicker accepts string default"() {
		when:
		def output = applyTemplate('<joda:timePicker name="foo" precision="second" default="23:59:59"/>')

		then:
		output =~ /<option value="23" selected="selected">23<\/option>/
		output =~ /<option value="59" selected="selected">59<\/option>/
		output =~ /<option value="59" selected="selected">59<\/option>/
	}

	@Unroll({"picker tags accept ${defaultValue.getClass().simpleName} default"})
	def "picker tags accept ReadableInstant or ReadablePartial default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}"/>', [defaultValue: defaultValue])

		then:
		output =~ /<option value="29" selected="selected">29<\/option>/
		output =~ /<option value="11" selected="selected">November<\/option>/
		output =~ /<option value="1971" selected="selected">1971<\/option>/
		output =~ /<option value="16" selected="selected">16<\/option>/
		output =~ /<option value="22" selected="selected">22<\/option>/

		where:
		defaultValue << [new DateTime(1971, 11, 29, 16, 22, 0, 0), new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)]
	}

	@Unroll({"picker tags accept ${value.getClass().simpleName} default"})
	def "picker tags accept ReadableInstant or ReadablePartial value"() {
		given:
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}" value="${value}"/>', [value: value, defaultValue: defaultValue])

		then:
		output =~ /<option value="8" selected="selected">8<\/option>/
		output =~ /<option value="2" selected="selected">February<\/option>/
		output =~ /<option value="1977" selected="selected">1977<\/option>/
		output =~ /<option value="09" selected="selected">09<\/option>/
		output =~ /<option value="30" selected="selected">30<\/option>/

		where:
		value << [new DateTime(1977, 2, 8, 9, 30, 0, 0), new LocalDateTime(1977, 2, 8, 9, 30, 0, 0)]
	}

	def "picker tags accept noSelection arg"() {
		when:
		def output = applyTemplate('''<joda:dateTimePicker name="foo" noSelection="['': 'Choose sumfink innit']"/>''')

		then:
		output =~ /<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/
		output =~ /<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/
		output =~ /<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/
		output =~ /<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/
		output =~ /<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/
	}

	def "picker tags accept years arg"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" years="${1979..1983}" default="1979-12-04T00:00"/>')

		then:
		!output.contains(/<option value="1978">1978<\/option>/)
		output =~ /<option value="1979" selected="selected">1979<\/option>/
		output =~ /<option value="1980">1980<\/option>/
		output =~ /<option value="1981">1981<\/option>/
		output =~ /<option value="1982">1982<\/option>/
		output =~ /<option value="1983">1983<\/option>/
		!output.contains(/<option value="1984">1984<\/option>/)
	}

	def "dateTimePicker uses minute as default precision"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')

		then:
		output =~ /<select name="foo_day"/
		output =~ /<select name="foo_month"/
		output =~ /<select name="foo_year"/
		output =~ /<select name="foo_hour"/
		output =~ /<select name="foo_minute"/
		!output.contains(/<select name="foo_second"/)
	}

	@Unroll({"picker tags accept '$precision' precision arg"})
	def "picker tags accept precision arg"() {
		when:
		def output = applyTemplate("<joda:dateTimePicker name=\"foo\" precision=\"$precision\"/>")

		then:
		output.contains(/<select name="foo_year"/) || !year
		output.contains(/<select name="foo_month"/) || !month
		output.contains(/<select name="foo_day"/) || !day
		output.contains(/<select name="foo_hour"/) || !hour
		output.contains(/<select name="foo_minute"/) || !minute
		output.contains(/<select name="foo_second"/) || !second

		where:
		precision | year | month | day   | hour  | minute | second
		"year"    | true | false | false | false | false  | false
		"month"   | true | true  | false | false | false  | false
		"day"     | true | true  | true  | false | false  | false
		"hour"    | true | true  | true  | true  | false  | false
		"minute"  | true | true  | true  | true  | true   | false
		"second"  | true | true  | true  | true  | true   | true
	}

	def "dateTimePicker accepts useZone arg"() {
		given:
		mockTagLib DateTimeZoneTagLib

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" useZone="true"/>')

		then:
		output =~ /<select name="foo_zone"/
	}

}
