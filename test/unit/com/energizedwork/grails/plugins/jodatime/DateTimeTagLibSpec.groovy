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
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.joda.time.*

class DateTimeTagLibSpec extends TagLibSpec {

	def setupSpec() {
		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.currentMillisFixed = fixedDateTime.getMillis()
	}

	def cleanupSpec() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	def setup() {
		loadCodec HTMLCodec

		def mockGrailsApplication = [config: new ConfigObject()]
		tagLib.metaClass.getGrailsApplication = {-> mockGrailsApplication }

		mockRequest.addPreferredLocale Locale.UK
	}
	
	def "date picker only outputs date fields"() {
		when: def markup = tagLib.datePicker(name: 'foo')

		then:
		println "markup='$markup' which is a '${markup?.getClass()}'"
		markup.contains(/<select name="foo_day"/)
		markup.contains(/<select name="foo_month"/)
		markup.contains(/<select name="foo_year"/)
		!markup.contains(/<select name="foo_hour"/)
		!markup.contains(/<select name="foo_minute"/)
		!markup.contains(/<select name="foo_second"/)
	}

	def "time picker only outputs time fields"() {
		when: def markup = timePicker(name: 'foo', precision: 'second')

		then:
		println "markup='$markup' which is a '${markup?.getClass()}'"
		!markup.contains(/<select name="foo_day"/)
		!markup.contains(/<select name="foo_month"/)
		!markup.contains(/<select name="foo_year"/)
		markup.contains(/<select name="foo_hour"/)
		markup.contains(/<select name="foo_minute"/)
		markup.contains(/<select name="foo_second"/)
	}

	def "picker tags use current date-time as default value"() {
		when: def markup = dateTimePicker(name: 'foo', precision: 'second')

		then:
		markup.contains(/<option value="2" selected="selected">2<\/option>/)
		markup.contains(/<option value="10" selected="selected">October<\/option>/)
		markup.contains(/<option value="2008" selected="selected">2008<\/option>/)
		markup.contains(/<option value="2" selected="selected">2<\/option>/)
		markup.contains(/<option value="50" selected="selected">50<\/option>/)
		markup.contains(/<option value="33" selected="selected">33<\/option>/)
	}

	@Unroll("dateTimePicker accepts a #type default")
	def "dateTimePicker accepts a default"() {
		when: def markup = dateTimePicker(name: 'foo', 'default': defaultValue)

		then:
		markup.contains(/<option value="29" selected="selected">29<\/option>/)
		markup.contains(/<option value="11" selected="selected">November<\/option>/)
		markup.contains(/<option value="1971" selected="selected">1971<\/option>/)
		markup.contains(/<option value="16" selected="selected">16<\/option>/)
		markup.contains(/<option value="22" selected="selected">22<\/option>/)
		
		where:
		defaultValue << ["1971-11-29T16:22", new DateTime(1971, 11, 29, 16, 22, 0, 0), new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)]
		type = defaultValue.getClass().simpleName
	}

	def "default value format is appropriate to precision"() {
		when: def markup = datePicker(name: 'foo', 'default': '1971-11-29')

		then:
		markup.contains(/<option value="29" selected="selected">29<\/option>/)
		markup.contains(/<option value="11" selected="selected">November<\/option>/)
		markup.contains(/<option value="1971" selected="selected">1971<\/option>/)
	}

	def "timePicker accepts a String default"() {
		when: def markup = timePicker(name: 'foo', precision: 'second', 'default': '23:59:59')

		then:
		markup.contains(/<option value="23" selected="selected">23<\/option>/)
		markup.contains(/<option value="59" selected="selected">59<\/option>/)
		markup.contains(/<option value="59" selected="selected">59<\/option>/)
	}

	def "the value attribute overrides default"() {
		when:
		def markup = dateTimePicker(name: 'foo', 'default': defaultValue, value: value)

		then:
		markup.contains(/<option value="8" selected="selected">8<\/option>/)
		markup.contains(/<option value="2" selected="selected">February<\/option>/)
		markup.contains(/<option value="1977" selected="selected">1977<\/option>/)
		markup.contains(/<option value="09" selected="selected">09<\/option>/)
		markup.contains(/<option value="30" selected="selected">30<\/option>/)
		
		where:
		defaultValue                                  | value
		new DateTime(1971, 11, 29, 16, 22, 0, 0)      | new DateTime(1977, 2, 8, 9, 30, 0, 0)
		new LocalDateTime(1971, 11, 29, 16, 22, 0, 0) | new LocalDateTime(1977, 2, 8, 9, 30, 0, 0)
	}

	def "picker tags accept a noSelection attribute"() {
		when:
		def markup = dateTimePicker(name: 'foo', noSelection: ['': 'Choose sumfink innit'])

		then:
		markup.contains(/<select name="foo_day" id="foo_day">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">1<\/option>/)
		markup.contains(/<select name="foo_month" id="foo_month">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1">January<\/option>/)
		markup.contains(/<select name="foo_year" id="foo_year">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="1908">1908<\/option>/)
		markup.contains(/<select name="foo_hour" id="foo_hour">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/)
		markup.contains(/<select name="foo_minute" id="foo_minute">\s*<option value="" selected="selected">Choose sumfink innit<\/option>\s*<option value="00">00<\/option>/)
	}

	def "picker tags accept a years attribute"() {
		when:
		def markup = dateTimePicker(name: 'foo', years: 1979..1983, 'default': '1979-12-04T00:00')

		then:
		!markup.contains(/<option value="1978">1978<\/option>/)
		markup.contains(/<option value="1979" selected="selected">1979<\/option>/)
		markup.contains(/<option value="1980">1980<\/option>/)
		markup.contains(/<option value="1981">1981<\/option>/)
		markup.contains(/<option value="1982">1982<\/option>/)
		markup.contains(/<option value="1983">1983<\/option>/)
		!markup.contains(/<option value="1984">1984<\/option>/)
	}

	def "dateTimePicker uses minute as default precision"() {
		when:
		def markup = dateTimePicker(name: 'foo')

		then:
		markup.contains(/<select name="foo_minute"/)
		!markup.contains(/<select name="foo_second"/)
	}

	def "picker tags accept a precision attribute"() {
		given:
		def allFields = ["year", "month", "day", "hour", "minute", "second"]
		
		when:
		def markup = dateTimePicker(name: 'foo', precision: precision)

		then:
		includes.every { markup.contains(/<select name="foo_$it"/) }
		!(allFields - includes).any { markup.contains(/<select name="foo_$it"/) }
		
		where:
		precision | includes
		"year"    | ["year"]
		"month"   | ["year", "month"]
		"day"     | ["year", "month", "day"]
		"hour"    | ["year", "month", "day", "hour"]
		"minute"  | ["year", "month", "day", "hour", "minute"]
		"second"  | ["year", "month", "day", "hour", "minute", "second"]
	}

	def "dateTimePicker accepts a zone attribute"() {
		setup:
		def dateTimeZoneSelectArgs
		tagLib.metaClass.dateTimeZoneSelect = {attrs ->
			dateTimeZoneSelectArgs = attrs
		}
		
		when:
		dateTimePicker(name: "foo", useZone: "true")
		
		then:
		dateTimeZoneSelectArgs.name == "foo_zone"
	}

}
