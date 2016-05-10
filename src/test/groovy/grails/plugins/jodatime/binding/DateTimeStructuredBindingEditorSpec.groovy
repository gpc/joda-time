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
package grails.plugins.jodatime.binding

import grails.databinding.SimpleMapDataBindingSource
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DateTimeStructuredBindingEditorSpec extends Specification {

	@Shared zone

	void setupSpec() {
		zone = DateTimeZone.default
		DateTimeZone.default = DateTimeZone.forID("Europe/London")
	}

	void cleanupSpec() {
		DateTimeZone.default = zone
	}

	void "getPropertyValue() creates #expected from the fields #fields"() {
		given:
		def editor = new DateTimeStructuredBindingEditor(type)
		def obj = type.newInstance()
		def dateProps = fields.collectEntries { ["some_date_param_name_$it.key".toString(), it.value]}
		def source = new SimpleMapDataBindingSource(dateProps)
		expect: editor.getPropertyValue(obj, 'some_date_param_name', source) == expected
		where:
		type          | fields                                                                                      | expected
		LocalDate     | [year: "1977"]                                                                              | new LocalDate(1977, 1, 1)
		LocalDate     | [year: "2009", month: "02", day: "20"]                                                      | new LocalDate(2009, 2, 20)
		LocalDateTime | [year: "2009"]                                                                              | new LocalDateTime(2009, 1, 1, 0, 0)
		LocalDateTime | [year: "2009", month: "03", day: "06", hour: "17", minute: "21", second: "33"]              | new LocalDateTime(2009, 3, 6, 17, 21, 33)
		DateTime      | [year: "2009"]                                                                              | new DateTime(2009, 1, 1, 0, 0, 0, 0)
		DateTime      | [year: "2009", month: "03", day: "06", hour: "17", minute: "21", second: "33"]              | new DateTime(2009, 3, 6, 17, 21, 33, 0)
		LocalTime     | [hour: "17"]                                                                                | new LocalTime(17, 0)
		LocalTime     | [hour: "17", minute: "55", second: "33"]                                                    | new LocalTime(17, 55, 33)
		DateTime      | [year: "2009", month: "08", day: "24", hour: "13", minute: "06"]                            | new DateTime(2009, 8, 24, 13, 6, 0, 0).withZoneRetainFields(DateTimeZone.forID("Europe/London"))
		DateTime      | [year: "2009", month: "08", day: "24", hour: "13", minute: "06", zone: "America/Vancouver"] | new DateTime(2009, 8, 24, 13, 6, 0, 0).withZoneRetainFields(DateTimeZone.forID("America/Vancouver"))
	}

	void "getPropertyValue() requires year for date types"() {
		given:
		def editor = new DateTimeStructuredBindingEditor(LocalTime)
		def obj = new LocalTime()
		def dateProps = ['some_date_param_name_month': 11, 'some_date_param_name_day': 29]
		def source = new SimpleMapDataBindingSource(dateProps)
		when: editor.getPropertyValue(obj, 'some_date_param_name', source)
		then: thrown(IllegalArgumentException)
	}

	void "getPropertyValue() requires hour for time types"() {
		given:
		def editor = new DateTimeStructuredBindingEditor(LocalTime)
		def obj = new LocalTime()
		def dateProps = ['some_date_param_name_minute': 29]
		def source = new SimpleMapDataBindingSource(dateProps)
		when: editor.getPropertyValue(obj, 'some_date_param_name', source)
		then: thrown(IllegalArgumentException)
	}
}
