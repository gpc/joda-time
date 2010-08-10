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
import static com.energizedwork.grails.plugins.jodatime.JodaDynamicMethods.registerDynamicMethods
import org.joda.time.*
import static org.joda.time.DateTimeUtils.setCurrentMillisFixed
import static org.joda.time.DateTimeUtils.setCurrentMillisSystem

class JodaDynamicMethodsSpec extends Specification {

	def setupSpec() {
		registerDynamicMethods()
		setCurrentMillisFixed new DateTime(2008, 10, 2, 2, 50, 0, 0).millis
	}

	def cleanupSpec() {
		setCurrentMillisSystem()
	}

	@Unroll("format works on #type")
	def "format works on various types"() {
		expect:
		type.newInstance().format(format) == expected
		where:
		type      | format                | expected
		DateTime  | "dd/MM/yyyy HH:mm:ss" | "02/10/2008 02:50:00"
		LocalDate | "dd/MM/yyyy"          | "02/10/2008"
		LocalTime | "HH:mm:ss"            | "02:50:00"
	}
	
	def "multiplication operator"() {
		expect: value * 2 == expected
		where:
		type << [Days, Hours, Minutes, Months, Seconds, Weeks, Years]
		value = type.newInstance(3)
		expected = type.newInstance(6)
	}

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

/*
	@Test
	void groovyOperatorsOnSingleFieldPeriods() {
		[Days, Hours, Minutes, Months, Seconds, Weeks, Years].each { clazz ->
			def instance = clazz.newInstance(3)
			assertThat(-instance, equalTo(clazz.newInstance(-3)))
			assertThat instance * 2, equalTo(clazz.newInstance(6))
			assertThat instance / 3, equalTo(clazz.newInstance(1))
			assertThat instance / 2, equalTo(clazz.newInstance(1)) // this is integer division
			// the following should just work
			assertThat instance + instance, equalTo(clazz.newInstance(6))
			assertThat instance + 2, equalTo(clazz.newInstance(5))
			assertThat instance - clazz.newInstance(2), equalTo(clazz.newInstance(1))
			assertThat instance - 1, equalTo(clazz.newInstance(2))
			assertThat instance + -instance, equalTo(clazz.newInstance(0))
		}
	}
*/
}