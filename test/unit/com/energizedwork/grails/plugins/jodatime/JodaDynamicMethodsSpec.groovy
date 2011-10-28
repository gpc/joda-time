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

import static com.energizedwork.grails.plugins.jodatime.JodaDynamicMethods.registerDynamicMethods
import org.joda.time.*
import static org.joda.time.DateTimeUtils.*
import spock.lang.*

class JodaDynamicMethodsSpec extends Specification {

	def setupSpec() {
		registerDynamicMethods()
		setCurrentMillisFixed new DateTime(2008, 10, 2, 2, 50, 0, 0).millis
	}

	def cleanupSpec() {
		setCurrentMillisSystem()
	}

	@Unroll({"format works on $type"})
	def "format works on various types"() {
		expect:
		type.newInstance().format(format) == expected

		where:
		type      | format                | expected
		DateTime  | "dd/MM/yyyy HH:mm:ss" | "02/10/2008 02:50:00"
		LocalDate | "dd/MM/yyyy"          | "02/10/2008"
		LocalTime | "HH:mm:ss"            | "02:50:00"
	}

	@Unroll({"negation operator works on $type.simpleName"})
	def "negation operator"() {
		expect: -value == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(-3)
	}

	@Unroll({"multiplication operator works on $type.simpleName"})
	def "multiplication operator"() {
		expect: value * 2 == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(6)
	}

	@Unroll({"division operator works on $type.simpleName"})
	def "division operator"() {
		expect: "division producing an integeger works"
		value / 3 == expected

		and: "division is always treated as integer division"
		value / 2 == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(1)
	}

	@Unroll({"standard groovy operators work on $type.simpleName"})
	def "standard groovy operators"() {
		expect:
		// the following should just work
		value + value == type.newInstance(6)
		value + 2 == type.newInstance(5)
		value - type.newInstance(2) == type.newInstance(1)
		value - 1 == type.newInstance(2)
		value + -value == type.newInstance(0)

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
	}

	@Issue("http://jira.grails.org/browse/GPJODATIME-14")
	@Unroll({"can use `next` and `previous` on ${value.getClass().simpleName}"})
	def "can use `next` and `previous` on Joda-Time types"() {
		expect:
		value.next() == value + increment
		value.previous() == value - increment

		where:
		value                                   | increment
		new DateTime(2011, 10, 27, 13, 11)      | Days.ONE
		new LocalDateTime(2011, 10, 27, 13, 11) | Days.ONE
		new LocalDate(2011, 10, 27)             | Days.ONE
		new LocalTime(13, 11)                   | Hours.ONE
		new MonthDay(10, 27)                    | Days.ONE
		new YearMonth(2011, 10)                 | Months.ONE
		new YearMonthDay(2011, 10, 27)          | Days.ONE
		new TimeOfDay(13, 11, 28)               | Hours.ONE
	}

}