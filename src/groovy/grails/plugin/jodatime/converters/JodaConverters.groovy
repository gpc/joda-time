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

import grails.converters.JSON
import grails.converters.XML
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTimeZone

class JodaConverters {

	static void registerJsonAndXmlMarshallers() {
		[JSON, XML].each {converter ->
			converter.registerObjectMarshaller(DateTime, 1) {
				it?.toString(ISODateTimeFormat.dateTime().withZone(it?.zone))
			}
			converter.registerObjectMarshaller(LocalDate, 2) {
				it?.toString(ISODateTimeFormat.date())
			}
			converter.registerObjectMarshaller(LocalTime, 3) {
				it?.toString(ISODateTimeFormat.time())
			}
			converter.registerObjectMarshaller(LocalDateTime, 4) {
				it?.toString(ISODateTimeFormat.dateTime())
			}
			converter.registerObjectMarshaller(DateTimeZone, 5) {
				it?.ID
			}
		}
	}

}
