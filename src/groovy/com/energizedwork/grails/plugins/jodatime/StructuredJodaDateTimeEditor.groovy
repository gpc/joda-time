package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.JodaDateTimeEditor
import org.codehaus.groovy.grails.web.binding.StructuredPropertyEditor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.MutableDateTime

class StructuredJodaDateTimeEditor extends JodaDateTimeEditor implements StructuredPropertyEditor {

	StructuredJodaDateTimeEditor(Class type) {
		super(type)
	}

	private static final FIELDS_BY_TYPE = [
			(LocalDate): ['year', 'month', 'day'].asImmutable(),
			(LocalTime): ['hour', 'minute', 'second'].asImmutable(),
			(LocalDateTime): ['year', 'month', 'day', 'hour', 'minute', 'second'].asImmutable(),
			(DateTime): ['year', 'month', 'day', 'hour', 'minute', 'second'].asImmutable()
	].asImmutable()

	private static final DEFAULT_VALUES = [month: 1, day: 1, hour: 0, minute: 0, second: 0].asImmutable()

	private static final JODA_PROP_NAMES =[year: 'year', month: 'monthOfYear', day: 'dayOfMonth', hour: 'hourOfDay', minute: 'minuteOfHour', second: 'secondOfMinute'].asImmutable()

	List getRequiredFields() {
		return [FIELDS_BY_TYPE[type].head()]
	}

	List getOptionalFields() {
		return FIELDS_BY_TYPE[type].tail()
	}

	Object assemble(Class type, Map fieldValues) throws IllegalArgumentException {
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
				dt."${JODA_PROP_NAMES[it]}" = (fieldValues."$it"?.toInteger() ?: DEFAULT_VALUES[it])
			}
			return dt.toDateTime()."to$type.simpleName"()
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException('Unable to parse structured date from request for date ["+propertyName+"]"')
		}
	}
}