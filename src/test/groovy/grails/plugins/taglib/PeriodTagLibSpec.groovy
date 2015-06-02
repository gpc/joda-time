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
import org.grails.taglib.GrailsTagException
import org.joda.time.Period
import spock.lang.IgnoreIf
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Locale.*
import static jodd.jerry.Jerry.jerry as $

@Unroll
@TestFor(PeriodTagLib)
class PeriodTagLibSpec extends Specification {

	void cleanup() {
		grailsApplication.config.jodatime = [:]
	}

	void 'periodPicker defaults id from name'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo"/>')
		def dom = $(output)

		expect:
		dom.find('#foo_hours').attr('name') == 'foo_hours'
		dom.find('#foo_minutes').attr('name') == 'foo_minutes'
		dom.find('#foo_seconds').attr('name') == 'foo_seconds'
	}

	void 'periodPicker uses hours minutes and seconds by default'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo"/>')
		def dom = $(output)

		expect:
		dom.find(':input[type=text]')*.attr('name') == ['foo_hours', 'foo_minutes', 'foo_seconds']
	}

	void 'periodPicker uses fields from config if present'() {
		given:
		grailsApplication.config.jodatime = [periodpicker: [default: [fields : "years,months , days"]]]

		and:
		def output = applyTemplate('<joda:periodPicker name="foo"/>')
		def dom = $(output)

		expect:
		dom.find(':input[type=text]')*.attr('name') == ['foo_years', 'foo_months', 'foo_days']
	}

	void 'periodPicker accepts fields attribute'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" fields="years,months,days"/>')
		def dom = $(output)

		expect:
		dom.find(':input[type=text]')*.attr('name') == ['foo_years', 'foo_months', 'foo_days']
	}

	void 'periodPicker accepts value attribute'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: value])
		def dom = $(output)

		expect:
		dom.find('#foo_hours').attr('value') == "$value.hours"
		dom.find('#foo_minutes').attr('value') == "$value.minutes"
		dom.find('#foo_seconds').attr('value') == "$value.seconds"

		where:
		value = new Period().withHours(8).withMinutes(12).withSeconds(35)
	}

	void 'periodPicker accepts duration value'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: value])
		def dom = $(output)

		expect:
		dom.find('#foo_hours').attr('value') == "$value.standardHours"
		dom.find('#foo_minutes').attr('value') == '12'
		dom.find('#foo_seconds').attr('value') == '35'

		where:
		value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()
	}

	@IgnoreIf({ System.getenv('TRAVIS') != null })
	void 'periodPicker uses hour as highest field when value is duration'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" fields="days,hours,minutes,seconds" value="${value}"/>', [value: value])
		def dom = $(output)

		expect:
		dom.find('#foo_days').attr('value') == '0'
		dom.find('#foo_hours').attr('value') == "$value.standardHours"
		dom.find('#foo_minutes').attr('value') == '0'
		dom.find('#foo_seconds').attr('value') == '0'

		where:
		value = new Period().withDays(1).withHours(1).toStandardDuration()
	}

	void 'periodPicker handles null value attribute'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: null])
		def dom = $(output)

		expect:
		dom.find(':input[type=text]')*.attr('value') == ['0', '0', '0']
	}

	void 'periodPicker outputs labels'() {
		given:
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: null])
		def dom = $(output)

		expect:
		def labels = dom.find('label')
		labels*.attr('for') == ['foo_hours', 'foo_minutes', 'foo_seconds']
		labels*.text() == ['\u00a0hours ', '\u00a0minutes ', '\u00a0seconds ']
		labels.every {
			it.find(':input').attr('id') == it.attr('for')
		}
	}

	void 'periodPicker uses correct default labels in #locale locale'() {
		given:
		request.addPreferredLocale locale

		and:
		def output = applyTemplate('<joda:periodPicker name="foo" value="${value}"/>', [value: null])
		def dom = $(output)

		expect:
		def labels = dom.find('label')
		labels*.text() == expectedText

		where:
		locale  | expectedText
		ENGLISH | ['\u00a0hours ', '\u00a0minutes ', '\u00a0seconds ']
		FRENCH  | ['\u00a0heures ', '\u00a0minutes ', '\u00a0secondes ']
		GERMAN  | ['\u00a0Stunden ', '\u00a0Minuten ', '\u00a0Sekunden ']
	}

	void 'periodPicker labels can be changed with message properties'() {
		given:
		def messages = [
				"org.joda.time.DurationFieldType.hours": "h",
				"org.joda.time.DurationFieldType.minutes": "m",
				"org.joda.time.DurationFieldType.seconds": "s"
		]
		messageSource.addMessages messages, ENGLISH

		and:
		def output = applyTemplate('<joda:periodPicker name="foo"/>')
		def dom = $(output)

		expect:
		dom.find('label')*.text() == ['\u00a0h ', '\u00a0m ', '\u00a0s ']
	}

	void 'formatPeriod requires value attribute'() {
		when:
		applyTemplate('<joda:formatPeriod/>')

		then:
		thrown GrailsTagException
	}

	void 'formatPeriod uses default fields'() {
		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "2 years, 2 months, 2 weeks, 2 days, 2 hours, 2 minutes and 2 seconds"

		where:
		value = new Period().withYears(2).withMonths(2).withWeeks(2).withDays(2).withHours(2).withMinutes(2).withSeconds(2)
	}

	void 'formatPeriod normalizes value'() {
		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "2 years, 1 week and 2 days"

		where:
		value = new Period().withYears(1).withMonths(12).withDays(7).withHours(48)
	}

	void 'formatPeriod accepts field attribute'() {
		expect:
		applyTemplate('<joda:formatPeriod fields="days,hours,minutes" value="${value}"/>', [value: value]) == "16 days, 2 hours and 2 minutes"

		where:
		value = new Period().withWeeks(2).withHours(50).withMinutes(2).withSeconds(2)
	}

	void 'formatPeriod uses fields from config'() {
		given:
		grailsApplication.config.jodatime = [periodpicker :[default: [fields : "years,months , days"]]]

		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "3 years, 2 months and 2 days"

		where:
		value = new Period().withYears(2).withMonths(14).withDays(2).withHours(12)
	}

	void 'formatPeriod omits zero valued fields'() {
		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "2 months, 2 days and 2 minutes"

		where:
		value = new Period().withMonths(2).withDays(2).withMinutes(2)
	}

	void 'formatPeriod handles error if value has years or months and fields does not'() {
		expect:
		applyTemplate('<joda:formatPeriod fields="days,minutes" value="${value}"/>', [value: value]) == "2 days and 2 minutes"

		where:
		value = new Period().withYears(1).withMonths(1).withDays(2).withMinutes(2)
	}

	void 'formatPeriod accepts duration value'() {
		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == "8 hours, 12 minutes and 35 seconds"

		where:
		value = new Period().withHours(8).withMinutes(12).withSeconds(35).toStandardDuration()
	}

	@Issue('http://jira.grails.org/browse/GPJODATIME-33')
	void 'formatPeriod correctly outputs value for #locale locale'() {
		given:
		request.addPreferredLocale locale

		expect:
		applyTemplate('<joda:formatPeriod value="${value}"/>', [value: value]) == expectedOutput

		where:
		locale  | expectedOutput
		ENGLISH | '1 hour'
		FRENCH  | '1 heure'
		GERMAN  | '1 Stunde'

		value = new Period().withHours(1)
	}

}
