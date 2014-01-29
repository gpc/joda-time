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
package grails.plugin.jodatime.binding

import java.beans.PropertyEditorSupport
import grails.plugin.jodatime.Html5DateTimeFormat
import grails.util.Holders
import org.joda.time.*
import org.joda.time.format.*
import org.springframework.context.i18n.LocaleContextHolder

class DateTimeEditor extends PropertyEditorSupport {

	static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime, Instant].asImmutable()

	protected final Class type
	@Lazy private ConfigObject config = Holders.config?.jodatime?.format

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
		if (hasConfigPatternFor(type)) {
			return DateTimeFormat.forPattern(getConfigPatternFor(type))
		} else if (useISO()) {
			return getISOFormatterFor(type)
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

	private boolean hasConfigPatternFor(Class type) {
		config?.flatten()?."$type.name"
	}

	private String getConfigPatternFor(Class type) {
		config?.flatten()?."$type.name"
	}

	private boolean useISO() {
		config?.html5
	}

	private DateTimeFormatter getISOFormatterFor(Class type) {
		switch (type) {
			case LocalTime:
				return Html5DateTimeFormat.time()
			case LocalDate:
				return Html5DateTimeFormat.date()
			case LocalDateTime:
				return Html5DateTimeFormat.datetimeLocal()
			case DateTime:
			case Instant:
				return Html5DateTimeFormat.datetime()
		}
		return null
	}

}
