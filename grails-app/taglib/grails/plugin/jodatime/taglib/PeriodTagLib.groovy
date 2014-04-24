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
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.PeriodFormat
import static org.joda.time.DurationFieldType.months
import static org.joda.time.DurationFieldType.years

class PeriodTagLib {

	static namespace = "joda"
	static encodeAsForTags = [periodPicker: "raw"]

	def periodPicker = {attrs ->
		def name = attrs.name
		def id = attrs.id ?: name
		def value = attrs.value

		def periodType = getPeriodType(attrs.fields, DEFAULT_PERIOD_TYPE)

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
		def value = attrs.value
		if (!value) {
			throwTagError("'value' attribute is required")
		}

		def periodType = getPeriodType(attrs.fields, PeriodType.standard())

		if (value instanceof Duration) {
			value = value.toPeriod(periodType)
		} else {
			value = safeNormalize(value, periodType)
		}

		def formatter = PeriodFormat.wordBased(request.locale)

		out << formatter.print(value)
	}

	private PeriodType getPeriodType(String fields, PeriodType defaultPeriodType) {
		PeriodType periodType
		if (fields) {
			periodType = getPeriodTypeForFields(fields)
        } else if (grailsApplication.config.jodatime?.periodpicker?.default?.fields) {
            periodType = getPeriodTypeForFields(grailsApplication.config.jodatime.periodpicker.default.fields)
		} else {
			periodType = defaultPeriodType
		}
		return periodType
	}

	private static final PeriodType DEFAULT_PERIOD_TYPE = getPeriodTypeForFields("hours,minutes,seconds")

	private static PeriodType getPeriodTypeForFields(String fields) {
		def fieldTypes = fields.split(/\s*,\s*/).collect { DurationFieldType."$it"() } as DurationFieldType[]
		return PeriodType.forFields(fieldTypes)
	}

	/**
	 * PeriodFormat.print will throw UnsupportedOperationException if years or months are present in period but
	 * not supported by the formatter so we trim those fields off to avoid the problem.
	 */
	private Period safeNormalize(Period value, PeriodType periodType) {
		if (!periodType.isSupported(years()) && years() in value.getFieldTypes()) {
			log.warn "Omitting years from value '$value' as format '$periodType' does not support years"
			value = value.withYears(0)
		}
		if (!periodType.isSupported(months()) && months() in value.getFieldTypes()) {
			log.warn "Omitting months from value '$value' as format '$periodType' does not support months"
			value = value.withMonths(0)
		}
		return value.normalizedStandard(periodType)
	}

	private String getLabelFor(DurationFieldType fieldType) {
		def bundle = ResourceBundle.getBundle("${PeriodFormat.package.name}.messages", request.locale)
		def defaultLabel = bundle.getString("PeriodFormat.$fieldType.name").trim()
		message(code: "${DurationFieldType.name}.$fieldType.name", default: defaultLabel)
	}

}
