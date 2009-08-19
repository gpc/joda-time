package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DurationFieldType
import org.joda.time.PeriodType
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.Duration

class PeriodTagLib {

	static namespace = "joda"

	def periodPicker = {attrs ->
		def name = attrs.name
		def id = attrs.id ?: name
		def value = attrs.value
		if (value instanceof Duration) {
			value = value.toPeriod()
		}

		def periodType
		if (attrs.fields) {
			def fieldTypes = attrs.fields.split(/,/).collect { DurationFieldType."$it"() } as DurationFieldType[]
			periodType = PeriodType.forFields(fieldTypes)
		} else if (ConfigurationHolder.config?.jodatime?.periodpicker?.default?.fields) {
			def fieldTypes = ConfigurationHolder.config.jodatime.periodpicker.default.fields.split(/\s*,\s*/).collect { DurationFieldType."$it"() } as DurationFieldType[]
			periodType = PeriodType.forFields(fieldTypes)
		} else {
			def fieldTypes = [DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds()] as DurationFieldType[]
			periodType = PeriodType.forFields(fieldTypes)
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

}