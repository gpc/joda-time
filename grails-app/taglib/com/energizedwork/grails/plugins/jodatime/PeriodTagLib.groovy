package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DurationFieldType
import static org.joda.time.DurationFieldType.*
import org.joda.time.PeriodType
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.format.*

class PeriodTagLib {

	static namespace = "joda"

	def periodPicker = {attrs ->
		def name = attrs.name
		def id = attrs.id ?: name
		def value = attrs.value

		def periodType
		if (attrs.fields) {
			periodType = getPeriodTypeForFields(attrs.fields)
		} else if (ConfigurationHolder.config?.jodatime?.periodpicker?.default?.fields) {
			periodType = getPeriodTypeForFields(ConfigurationHolder.config.jodatime.periodpicker.default.fields)
		} else {
			periodType = defaultPeriodType
		}

		if (value instanceof Duration) {
			value = value.toPeriod(periodType)
		}

		out << "<input type=\"hidden\" name=\"$name\" value=\"struct\" />"

		(0..<periodType.size()).each {i ->
			def fieldType = periodType.getFieldType(i)
			out << "<label for=\"${id}_${fieldType.name}\">"
			out << """<input type="text" name="${name}_${fieldType.name}" id="${id}_${fieldType.name}" value="${value?.get(fieldType) ?: 0}" size="1"/>"""
			out << "&nbsp;" << message(code: "${DurationFieldType.name}.$fieldType.name", default: fieldType.name) << " "
			out << "</label>"
		}
	}

	def formatPeriod = {attrs ->
		def value = attrs.value
		if (!value) {
			throwTagError("'value' attribute is required")
		}

		def periodType
		if (attrs.fields) {
			periodType = getPeriodTypeForFields(attrs.fields)
		} else if (ConfigurationHolder.config?.jodatime?.periodpicker?.default?.fields) {
			periodType = getPeriodTypeForFields(ConfigurationHolder.config.jodatime.periodpicker.default.fields)
		} else {
			periodType = PeriodType.standard()
		}

		if (value instanceof Duration) {
			value = value.toPeriod(periodType)
		} else {
			value = value.normalizedStandard(periodType)
		}

		def formatter = PeriodFormat.default

		out << formatter.print(value)
	}

	private static PeriodType defaultPeriodType = PeriodType.forFields([hours(), minutes(), seconds()] as DurationFieldType[])

	private static PeriodType getPeriodTypeForFields(String fields) {
		def fieldTypes = fields.split(/\s*,\s*/).collect { DurationFieldType."$it"() } as DurationFieldType[]
		return PeriodType.forFields(fieldTypes)
	}

}