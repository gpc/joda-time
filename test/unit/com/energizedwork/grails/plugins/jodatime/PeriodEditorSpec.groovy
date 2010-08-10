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

import org.joda.time.Duration
import org.joda.time.Period
import spock.lang.*
import grails.plugin.spock.*

class PeriodEditorSpec extends Specification {

	@Unroll("getAsText formats #value correctly")
	def "getAsText formats values correctly"() {
		given: def editor = new PeriodEditor(type)
		when: editor.value = value
		then: editor.asText == expected
		where:
		type     | value                                         | expected
		Period   | null                                          | ""
		Period   | new Period(1, 2, 0, 4, 8, 12, 35, 0)          | "1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds"
		Period   | new Period(0, 120, 12, 0)                     | "120 minutes and 12 seconds"
		Duration | new Period(1, 35, 16, 0).toStandardDuration() | "1 hour, 35 minutes and 16 seconds"
	}

	@Unroll("setAsText handles #text correctly")
	def "setAsText handles values correctly"() {
		given: def editor = new PeriodEditor(type)
		when: editor.asText = text
		then: editor.value == expected
		where:
		type     | text                                                           | expected
		Period   | null                                                           | null
		Period   | ""                                                             | null
		Period   | "1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds" | new Period(1, 2, 0, 4, 8, 12, 35, 0)
		Duration | "1 hour, 35 minutes and 16 seconds"                            | new Period(1, 35, 16, 0).toStandardDuration()
	}

}