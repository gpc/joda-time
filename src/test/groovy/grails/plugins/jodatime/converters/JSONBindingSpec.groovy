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
package grails.plugins.jodatime.converters

import grails.converters.JSON
import grails.persistence.Entity
import grails.plugins.jodatime.binding.DateTimeConverter
import grails.testing.gorm.DataTest
import grails.testing.web.GrailsWebUnitTest
import grails.util.GrailsNameUtils
import groovy.transform.CompileStatic
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.joda.time.DateTimeZone.UTC

@Unroll
class JSONBindingSpec extends Specification implements GrailsWebUnitTest, DataTest {

	@Shared DateTimeZone originalTimeZone

	void setupSpec() {
		originalTimeZone = DateTimeZone.default
		DateTimeZone.default = DateTimeZone.forID("Etc/GMT+12")
	}

	void cleanupSpec() {
		DateTimeZone.default = originalTimeZone
	}

	void setup() {
		defineBeans {
			dateTimeConverter(DateTimeConverter) {
				type = DateTime
				configuration = config
			}
		}
	}

	void "can unmarshal a #expected.class.simpleName object from a JSON element #value"() {
		given:
		def json = JSON.parse("""{$propertyName: "$value"}""")

		when:
		def bean = new Timestamp(json)

		then:
		bean[propertyName] == expected

		where:
		value                           | expected
		'2014-04-23T04:30:45.123Z'      | new DateTime(2014, 4, 23, 4, 30, 45, 123, UTC)
		'2014-04-23T04:30:45.123+01:00' | new DateTime(2014, 4, 23, 4, 30, 45, 123, DateTimeZone.forOffsetHours(1))
		'2014-04-23T04:30:45.123'       | new DateTime(2014, 4, 23, 4, 30, 45, 123, DateTimeZone.default)
		'2014-04-23T04:30'              | new DateTime(2014, 4, 23, 4, 30, DateTimeZone.default)
		'2014-04-23T04:30:45.123'       | new LocalDateTime(2014, 4, 23, 4, 30, 45, 123)
		'2014-04-23T04:30:45'           | new LocalDateTime(2014, 4, 23, 4, 30, 45)
		'04:30:45.123'                  | new LocalTime(4, 30, 45, 123)
		'04:30:45'                      | new LocalTime(4, 30, 45)

		propertyName = GrailsNameUtils.getPropertyNameRepresentation(expected.class.simpleName)
	}
}

@CompileStatic
@Entity
class Timestamp {
	DateTime dateTime
	LocalDateTime localDateTime
	LocalTime localTime
}
