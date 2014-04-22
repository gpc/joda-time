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
package grails.plugin.jodatime.taglib

import org.joda.time.Duration
import org.joda.time.DurationFieldType
import org.joda.time.PeriodType
import org.joda.time.format.PeriodFormat
import grails.plugin.jodatime.util.PeriodUtils

class PeriodTagLib {

	static namespace = "joda"

	def periodPicker = {attrs ->
		def name = attrs.name
		def id = attrs.id ?: name
		def value = attrs.value

		def periodType = PeriodUtils.getPeriodType(attrs.fields, DEFAULT_PERIOD_TYPE)

		if (value instanceof Duration) {
			value = value.toPeriod(periodType)
		}

		out << "<input type=\"hidden\" name=\"$name\" value=\"struct\" />"

		(0..<periodType.size()).each {i ->
			def fieldType = periodType.getFieldType(i)
			out << "<label for=\"${id}_${fieldType.name}\">"
			out << """<input type="text" name="${name}_${fieldType.name}" id="${id}_${fieldType.name}" value="${value?.get(fieldType) ?: 0}" size="1"/>"""
			out << "&nbsp;" << getLabelFor(fieldType) << " "
			out << "</label>"
		}
	}

	def formatPeriod = {attrs ->
		if (!attrs.value) {
			throwTagError("'value' attribute is required")
		}

		out << PeriodUtils.formatPeriod(attrs.value, attrs.fields, request.locale)
	}

	private static final PeriodType DEFAULT_PERIOD_TYPE = PeriodUtils.getPeriodTypeForFields("hours,minutes,seconds")

	private String getLabelFor(DurationFieldType fieldType) {
		def bundle = ResourceBundle.getBundle("${PeriodFormat.package.name}.messages", request.locale)
		def defaultLabel = bundle.getString("PeriodFormat.$fieldType.name").trim()
		message(code: "${DurationFieldType.name}.$fieldType.name", default: defaultLabel)
	}

}
