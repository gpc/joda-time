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
package grails.plugin.jodatime

import static grails.plugin.jodatime.JodaDynamicMethods.registerDynamicMethods
import org.joda.time.*
import static org.joda.time.DateTimeUtils.*
import spock.lang.*

@Unroll
class JodaDynamicMethodsSpec extends Specification {

	def setupSpec() {
		registerDynamicMethods()
		setCurrentMillisFixed new DateTime(2008, 10, 2, 2, 50, 0, 0).millis
	}

	def cleanupSpec() {
		setCurrentMillisSystem()
	}

	def "format works on #type.simpleName"() {
		expect:
		type.newInstance().format(format) == expected

		where:
		type      | format                | expected
		DateTime  | "dd/MM/yyyy HH:mm:ss" | "02/10/2008 02:50:00"
		LocalDate | "dd/MM/yyyy"          | "02/10/2008"
		LocalTime | "HH:mm:ss"            | "02:50:00"
	}

	def "negation operator works on #type.simpleName"() {
		expect: -value == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(-3)
	}

	def "multiplication operator works on #type.simpleName"() {
		expect: value * 2 == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(6)
	}

	def "division operator works on #type.simpleName"() {
		expect: "division producing an integeger works"
		value / 3 == expected

		and: "division is always treated as integer division"
		value / 2 == expected

		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(1)
	}

	def "standard groovy operators work on #type.simpleName"() {
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
	def "can use `next` and `previous` on #value.class.simpleName"() {
		expect:
		value.next() == value + increment
		value.previous() == value - increment

		where:
		value                                   | increment
		new DateTime(2011, 10, 27, 13, 11)      | Days.ONE
		new LocalDateTime(2011, 10, 27, 13, 11) | Days.ONE
		new LocalDate(2011, 10, 27)             | Days.ONE
		new MonthDay(10, 27)                    | Days.ONE
		new YearMonthDay(2011, 10, 27)          | Days.ONE
		new LocalTime(13, 11)                   | Hours.ONE
		new TimeOfDay(13, 11, 28)               | Hours.ONE
		new YearMonth(2011, 10)                 | Months.ONE
	}

}