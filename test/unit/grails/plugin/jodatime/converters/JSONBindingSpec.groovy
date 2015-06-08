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
package grails.plugin.jodatime.converters

import groovy.transform.CompileStatic
import org.codehaus.groovy.grails.web.json.JSONElement
import grails.converters.*
import grails.persistence.Entity
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import grails.util.GrailsNameUtils
import org.joda.time.*
import static org.joda.time.DateTimeZone.UTC
import spock.lang.*
import grails.plugin.jodatime.binding.DateTimeConverter

@TestMixin(ControllerUnitTestMixin)
@Mock(Timestamp)
@Unroll
class JSONBindingSpec extends Specification {

	@Shared DateTimeZone originalTimeZone

	void setupSpec() {
		originalTimeZone = DateTimeZone.default
		DateTimeZone.default = DateTimeZone.forID("America/Vancouver")
	}

	void cleanupSpec() {
		DateTimeZone.default = originalTimeZone
	}

	void setup() {
		defineBeans {
			dateTimeConverter(DateTimeConverter) {
				type = DateTime
				grailsApplication = ref("grailsApplication")
			}
		}
	}

	def "can unmarshal a #expected.class.simpleName object from a JSON element #value"() {
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
