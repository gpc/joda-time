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

import org.codehaus.groovy.grails.web.binding.StructuredPropertyEditor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.MutableDateTime
import org.joda.time.DateTimeZone

class StructuredDateTimeEditor extends DateTimeEditor implements StructuredPropertyEditor {

	StructuredDateTimeEditor(Class type) {
		super(type)
	}

	private static final FIELDS_BY_TYPE = [
			(LocalDate): ["year", "month", "day"].asImmutable(),
			(LocalTime): ["hour", "minute", "second"].asImmutable(),
			(LocalDateTime): ["year", "month", "day", "hour", "minute", "second"].asImmutable(),
			(DateTime): ["year", "month", "day", "hour", "minute", "second", "zone"].asImmutable()
	].asImmutable()

	private static final DEFAULT_VALUES = [month: 1, day: 1, hour: 0, minute: 0, second: 0].asImmutable()

	private static final JODA_PROP_NAMES = [year: "year", month: "monthOfYear", day: "dayOfMonth", hour: "hourOfDay", minute: "minuteOfHour", second: "secondOfMinute"].asImmutable()


	List getRequiredFields() {
		return [FIELDS_BY_TYPE[type].head()]
	}

	List getOptionalFields() {
		return FIELDS_BY_TYPE[type].tail()
	}

	Object assemble(Class type, Map fieldValues) throws IllegalArgumentException {
		if (fieldValues.isEmpty() || fieldValues.every { !it.value }) return null

		requiredFields.each {
			if (!fieldValues."$it") {
				throw new IllegalArgumentException("Can't populate a $type without a $it")
			}
		}

		try {
			def dt = new MutableDateTime()
			dt.secondOfMinute = 0
			dt.millisOfSecond = 0
			(requiredFields + optionalFields).each {
				switch (it) {
					case "zone":
						// null is OK here as DateTimeZone.forID(null) returns default zone
						dt.zoneRetainFields = DateTimeZone.forID(fieldValues[it])
						break
					default:
						dt."${JODA_PROP_NAMES[it]}" = (fieldValues[it]?.toInteger() ?: DEFAULT_VALUES[it])
				}
			}
			return dt.toDateTime()."to$type.simpleName"()
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException('Unable to parse structured date from request for date ["+propertyName+"]"')
		}
	}
}