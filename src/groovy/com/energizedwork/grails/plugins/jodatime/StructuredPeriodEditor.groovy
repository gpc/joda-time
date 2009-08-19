package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.PeriodEditor
import org.codehaus.groovy.grails.web.binding.StructuredPropertyEditor
import org.joda.time.MutablePeriod
import org.joda.time.PeriodType
import org.joda.time.DurationFieldType
import org.joda.time.Duration

class StructuredPeriodEditor extends PeriodEditor implements StructuredPropertyEditor {

	private static final SUPPORTED_PERIOD_FIELDS = (0..<PeriodType.standard().size()).collect { PeriodType.standard().getFieldType(it) }
	private static final SUPPORTED_DURATION_FIELDS = ["weeks", "days", "hours", "minutes", "seconds", "millis"].collect { DurationFieldType."$it"() }

	StructuredPeriodEditor(Class type) {
		super(type)
	}

	public List getRequiredFields() {
		return []
	}

	public List getOptionalFields() {
		def fields = type == Duration ? SUPPORTED_DURATION_FIELDS : SUPPORTED_PERIOD_FIELDS
		return fields.collect { it.name }
	}

	public Object assemble(Class type, Map fieldValues) {
		try {
			def fields = type == Duration ? SUPPORTED_DURATION_FIELDS : SUPPORTED_PERIOD_FIELDS
			def period = new MutablePeriod()
			fields.each {
				def value = fieldValues."$it" ? fieldValues."$it".toInteger() : 0
				period.set(it, value)
			}
			return type == Duration ? period.toPeriod().toStandardDuration() : period.toPeriod()
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException('Unable to parse structured period from request for period ["+propertyName+"]"')
		}
	}

}