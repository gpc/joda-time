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

import com.energizedwork.grails.plugins.jodatime.StructuredDateTimeEditor
import org.joda.time.*
import spock.lang.*
import grails.plugin.spock.*

class StructuredDateTimeEditorSpec extends Specification {
	
	@Shared zone
	
	def setupSpec() {
		zone = DateTimeZone.default
		DateTimeZone.default = DateTimeZone.forID("Europe/London")
	}
	
	def cleanupSpec() {
		DateTimeZone.default = zone
	}

	@Unroll("assemble creates #expected from #fields")
	def "assemble creates a new object based on the supplied fields"() {
		given: def editor = new StructuredDateTimeEditor(type)
		expect: editor.assemble(type, fields) == expected
		where:
		type          | fields                                                                                      | expected
		LocalDate     | [:]                                                                                         | null
		LocalDate     | [year: "", month: "", day: ""]                                                              | null
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

	def "assemble requires year for date types"() {
		given: def editor = new StructuredDateTimeEditor(LocalDate)
		when: editor.assemble(LocalDate, [month: 11, day: 29])
		then: thrown(IllegalArgumentException)
	}

	def "assemble requires hour for time types"() {
		given: def editor = new StructuredDateTimeEditor(LocalTime)
		when: editor.assemble(LocalTime, [minute: 29])
		then: thrown(IllegalArgumentException)
	}

}