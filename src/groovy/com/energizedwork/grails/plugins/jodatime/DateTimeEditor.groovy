package com.energizedwork.grails.plugins.jodatime

import java.beans.PropertyEditorSupport
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.context.i18n.LocaleContextHolder

class DateTimeEditor extends PropertyEditorSupport {

	static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime].asImmutable()

	protected final Class type

	DateTimeEditor(Class type) {
		this.type = type
	}


	String getAsText() {
		return value ? formatter.print(value) : ""
	}

	void setAsText(String text) {
		value = text ? formatter.parseDateTime(text)."to$type.simpleName"() : null
	}

	protected DateTimeFormatter getFormatter() {
		def style
		switch (type) {
			case LocalTime:
				style = '-S'
				break
			case LocalDate:
				style = 'S-'
				break
			default:
				style = 'SS'
		}
		Locale locale = LocaleContextHolder.locale
		return DateTimeFormat.forStyle(style).withLocale(locale)
	}

}
