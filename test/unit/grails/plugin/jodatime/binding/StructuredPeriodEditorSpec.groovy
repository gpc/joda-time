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
package grails.plugin.jodatime.binding

import org.joda.time.*
import spock.lang.*
import grails.plugin.jodatime.binding.StructuredPeriodEditor

@Unroll
class StructuredPeriodEditorSpec extends Specification {

	@Shared Collection<DurationFieldType> allFields = (0..<PeriodType.standard().size()).collect { i -> PeriodType.standard().getFieldType(i) }

	private static <K, V> Map<K, V> toMap(Collection<K> keys, V value) {
		def map = [:]
		keys.each {
			map[it] = value
		}
		map
	}

	def "a #type.simpleName value can be assembled from the fields #fields"() {
		given: def editor = new StructuredPeriodEditor(type)
		expect: editor.assemble(type, fields) == expected
		where:
		type     | fields                                  | expected
		Period   | [:]                                     | new Period()
		Period   | toMap(allFields.name, "1")              | new Period(1, 1, 1, 1, 1, 1, 1, 1)
		Period   | [years: "1", days: "1", minutes: "1"]   | new Period(1, 0, 0, 1, 0, 1, 0, 0)
		Period   | [years: "", days: "1", minutes: "1"]    | new Period(0, 0, 0, 1, 0, 1, 0, 0)
		Duration | [:]                                     | Duration.ZERO
		Duration | [hours: "1", seconds: "1"]              | new Period(1, 0, 1, 0).toStandardDuration()
		Duration | [hours: "", minutes: "1", seconds: "1"] | new Period(0, 1, 1, 0).toStandardDuration()
		Duration | [years: "1", hours: "1"]                | new Period(1, 0, 0, 0).toStandardDuration()
		Duration | [months: "1", hours: "1"]               | new Period(1, 0, 0, 0).toStandardDuration()
	}

}