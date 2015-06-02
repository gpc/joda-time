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
package grails.plugins.taglib

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.joda.time.*
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

import static jodd.jerry.Jerry.jerry as $

@TestFor(DateTimeTagLib)
@Unroll
class DateTimeTagLibSpec extends Specification {

	def setup() {
		mockCodec HTMLCodec

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	def cleanup() {
		DateTimeUtils.setCurrentMillisSystem()
	}

	def "datePicker outputs only date fields"() {
		when:
		def output = applyTemplate('<joda:datePicker name="foo"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_day]').length() == 1
		dom.find('select[name=foo_month]').length() == 1
		dom.find('select[name=foo_year]').length() == 1

		and:
		dom.find('select[name=foo_hour]').length() == 0
		dom.find('select[name=foo_minute]').length() == 0
		dom.find('select[name=foo_second]').length() == 0
	}

	def "timePicker outputs only time fields"() {
		when:
		def output = applyTemplate('<joda:timePicker name="foo" precision="second"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_hour]').length() == 1
		dom.find('select[name=foo_minute]').length() == 1
		dom.find('select[name=foo_second]').length() == 1

		and:
		dom.find('select[name=foo_day]').length() == 0
		dom.find('select[name=foo_month]').length() == 0
		dom.find('select[name=foo_year]').length() == 0
	}

	def "picker tags use current date & time as default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" precision="second"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_day] option[selected]').attr('value') == '2'
		dom.find('select[name=foo_month] option[selected]').attr('value') == '10'
		dom.find('select[name=foo_month] option[selected]').text() == 'October'
		dom.find('select[name=foo_year] option[selected]').attr('value') == '2008'
		dom.find('select[name=foo_hour] option[selected]').attr('value') == '02'
		dom.find('select[name=foo_minute] option[selected]').attr('value') == '50'
		dom.find('select[name=foo_second] option[selected]').attr('value') == '33'
	}

	def "picker tags accept string default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="1971-11-29T16:22"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_day] option[selected]').attr('value') == '29'
		dom.find('select[name=foo_month] option[selected]').attr('value') == '11'
		dom.find('select[name=foo_month] option[selected]').text() == 'November'
		dom.find('select[name=foo_year] option[selected]').attr('value') == '1971'
		dom.find('select[name=foo_hour] option[selected]').attr('value') == '16'
		dom.find('select[name=foo_minute] option[selected]').attr('value') == '22'
	}

	def "string default format is appropriate for precision"() {
		when:
		def output = applyTemplate('<joda:datePicker name="foo" default="1971-11-29"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_day] option[selected]').attr('value') == '29'
		dom.find('select[name=foo_month] option[selected]').attr('value') == '11'
		dom.find('select[name=foo_month] option[selected]').text() == 'November'
		dom.find('select[name=foo_year] option[selected]').attr('value') == '1971'
	}

	def "timePicker accepts string default"() {
		when:
		def output = applyTemplate('<joda:timePicker name="foo" precision="second" default="23:59:59"/>')
		def dom = $(output)

		then:
		dom.find('select[name=foo_hour] option[selected]').attr('value') == '23'
		dom.find('select[name=foo_minute] option[selected]').attr('value') == '59'
		dom.find('select[name=foo_second] option[selected]').attr('value') == '59'
	}

	def "picker tags accept #defaultValue.class.simpleName default"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}"/>', [defaultValue: defaultValue])
		def dom = $(output)

		then:
		dom.find('select[name=foo_day] option[selected]').attr('value') == '29'
		dom.find('select[name=foo_month] option[selected]').attr('value') == '11'
		dom.find('select[name=foo_month] option[selected]').text() == 'November'
		dom.find('select[name=foo_year] option[selected]').attr('value') == '1971'
		dom.find('select[name=foo_hour] option[selected]').attr('value') == '16'
		dom.find('select[name=foo_minute] option[selected]').attr('value') == '22'

		where:
		defaultValue << [new DateTime(1971, 11, 29, 16, 22, 0, 0), new LocalDateTime(1971, 11, 29, 16, 22, 0, 0)]
	}

	def "picker tags accept #value.class.simpleName value"() {
		given:
		def defaultValue = new DateTime(1971, 11, 29, 16, 22, 0, 0)

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" default="${defaultValue}" value="${value}"/>', [value: value, defaultValue: defaultValue])
		def dom = $(output)

		then:
		dom.find('select[name=foo_day] option[selected]').attr('value') == '8'
		dom.find('select[name=foo_month] option[selected]').attr('value') == '2'
		dom.find('select[name=foo_month] option[selected]').text() == 'February'
		dom.find('select[name=foo_year] option[selected]').attr('value') == '1977'
		dom.find('select[name=foo_hour] option[selected]').attr('value') == '09'
		dom.find('select[name=foo_minute] option[selected]').attr('value') == '30'

		where:
		value << [new DateTime(1977, 2, 8, 9, 30, 0, 0), new LocalDateTime(1977, 2, 8, 9, 30, 0, 0)]
	}

	@Issue('http://jira.grails.org/browse/GPJODATIME-23')
	def "picker tags accept null value"() {
		when:
		def output = applyTemplate('''<joda:dateTimePicker name="foo" value="${value}" default="none" noSelection="['':'']"/>''', [value: null])
		def dom = $(output)

		then:
		for (node in ['day', 'month', 'year', 'hour', 'minute']) {
			def select = dom.find("select[name=foo_$node]")
			assert select.find('option[selected]').size() == 1

			def firstOption = select.find('option').first()
			assert firstOption.attr('value') == ''
			assert firstOption.text() == ''
			assert firstOption.attr('selected') == 'selected'
		}
	}

	def "picker tags accept noSelection arg"() {
		when:
		def output = applyTemplate('''<joda:dateTimePicker name="foo" default="none" noSelection="['': 'Choose sumfink innit']"/>''')
		def dom = $(output)

		then:
		for (node in ['day', 'month', 'year', 'hour', 'minute']) {
			def select = dom.find("select[name=foo_$node]")
			assert select.find('option[selected]').size() == 1
			
			def firstOption = select.find('option').first()
			assert firstOption.attr('value') == ''
			assert firstOption.text() == 'Choose sumfink innit'
			assert firstOption.attr('selected') == 'selected'
		}
	}

	def "picker tags use one hundred years as default"() {
		given:
		def year = new LocalDate().year

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')
		def dom = $(output)

		then:
		def yearSelect = dom.find('select[name=foo_year]')
		yearSelect.find("option[value='${year - 101}']").length() == 0
		yearSelect.find("option[value='${year - 100}']").length() == 1
		yearSelect.find("option[value='${year + 100}']").length() == 1
		yearSelect.find("option[value='${year + 101}']").length() == 0
	}

	def "dateTimePicker uses config for years"() {
		given:
		tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsBelow = 1
		tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsAbove = 2

		and:
		def year = new LocalDate().year

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')
		def dom = $(output)

		then:
		def yearSelect = dom.find('select[name=foo_year]')
		yearSelect.find("option[value='${year - 2}']").length() == 0
		((year - 1)..(year + 2)).every {
			yearSelect.find("option[value='$it']").length() == 1
		}
		yearSelect.find("option[value='${year + 3}']").length() == 0

		cleanup:
		tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsBelow = null
		tagLib.grailsApplication.config.grails.tags.datePicker.default.yearsAbove = null
	}

	def "picker tags accept years arg"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo" years="${1979..1983}" default="1979-12-04T00:00"/>')
		def dom = $(output)

		then:
		def yearSelect = dom.find('select[name=foo_year]')
		yearSelect.find("option[value='1978']").length() == 0
		(1979..1983).every {
			yearSelect.find("option[value='$it']").length() == 1
		}
		yearSelect.find("option[value='1984']").length() == 0

		and:
		yearSelect.find('option[selected]').attr('value') == '1979'
	}

	def "dateTimePicker uses minute as default precision"() {
		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')
		def dom = $(output)

		then:
		['day', 'month', 'year', 'hour', 'minute'].every {
			dom.find("select[name=foo_$it]").length() == 1
		}

		and:
		dom.find('select[name=foo_second]').length() == 0
	}

	def "dateTimePicker uses config for precision"() {
		given:
		tagLib.grailsApplication.config.grails.tags.datePicker.default.precision = "second"

		when:
		def output = applyTemplate('<joda:dateTimePicker name="foo"/>')
		def dom = $(output)

		then:
		['day', 'month', 'year', 'hour', 'minute', 'second'].every {
			dom.find("select[name=foo_$it]").length() == 1
		}

		cleanup:
		tagLib.grailsApplication.config.grails.tags.datePicker.default.precision = null
	}

	def "picker tags accept '#precision' precision arg"() {
		when:
		def output = applyTemplate("<joda:dateTimePicker name=\"foo\" precision=\"$precision\"/>")
		def dom = $(output)

		then:
		dom.find('select[name=foo_year]').length() == (year ? 1 : 0)
		dom.find('select[name=foo_month]').length() == (month ? 1 : 0)
		dom.find('select[name=foo_day]').length() == (day ? 1 : 0)
		dom.find('select[name=foo_hour]').length() == (hour ? 1 : 0)
		dom.find('select[name=foo_minute]').length() == (minute ? 1 : 0)
		dom.find('select[name=foo_second]').length() == (second ? 1 : 0)

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
		def dom = $(output)

		then:
		dom.find('select[name=foo_zone]').length() == 1
	}

}
