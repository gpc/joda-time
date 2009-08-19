package com.energizedwork.grails.plugins.jodatime

import java.beans.PropertyEditorSupport
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatter
import org.joda.time.Period
import org.joda.time.Duration
import org.joda.time.PeriodType
import org.joda.time.DurationFieldType

class PeriodEditor extends PropertyEditorSupport {

	private static final PeriodFormatter FORMATTER = PeriodFormat.default

	static final SUPPORTED_TYPES = [Duration, Period].asImmutable()
	private static final DURATION_PERIOD_TYPE = PeriodType.forFields([DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()] as DurationFieldType[])

	protected final Class type

	PeriodEditor(Class type) {
		this.type = type
	}
	
	String getAsText() {
		if (!value) {
			return ""
		} else if (type == Period) {
			return FORMATTER.print(value)
		} else if (type == Duration) {
			return FORMATTER.print(value.toPeriod(DURATION_PERIOD_TYPE))
		} else {
			throw new IllegalStateException("Unsupported type $type")
		}
	}

	void setAsText(String text) {
		if (!text) {
			value = null
		} else if (type == Period) {
			value = FORMATTER.parsePeriod(text)
		} else if (type == Duration) {
			value = FORMATTER.parsePeriod(text).toStandardDuration()
		} else {
			throw new IllegalStateException("Unsupported type $type")
		}
	}

}