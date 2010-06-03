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
package com.energizedwork.grails.plugins.jodatime

import java.beans.PropertyEditorSupport
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.context.i18n.LocaleContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
		def pattern = ConfigurationHolder.config?.flatten()?."jodatime.format.$type.name"
		if (pattern) {
			return DateTimeFormat.forPattern(pattern)
		} else {
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

}
