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

import org.apache.commons.lang.LocaleUtils
import org.joda.time.format.DateTimeFormat
import org.springframework.web.servlet.support.RequestContextUtils
import org.joda.time.*

class FormattingTagLib {

	static namespace = "joda"

	def grailsApplication

	def format = {attrs ->
		if (attrs.pattern && attrs.style) {
			throwTagError('Cannot specify both pattern and style attributes')
		}

		if (!attrs.containsKey("value")) attrs.value = new DateTime()
		def value = attrs.value
		if (value) {
			def locale = attrs.locale ?: RequestContextUtils.getLocale(request)
			def zone = attrs.zone
			def chronology = attrs.chronology

			def pattern = attrs.pattern
			def style = attrs.style
			if (!pattern && !style) {
				pattern = patternForType(value.getClass())
				switch (value) {
					case LocalDate:
						style = 'M-'
						break
					case LocalTime:
						style = '-M'
						break
					default:
						style = 'MM'
				}
			}

			def formatter
			if (pattern) {
				formatter = DateTimeFormat.forPattern(pattern).withLocale(locale)
			} else {
				formatter = DateTimeFormat.forStyle(style).withLocale(locale)
			}

			if (zone) formatter = formatter.withZone(zone)
			if (chronology) formatter = formatter.withChronology(chronology)

			out << formatter.print(value)
		}
	}

	def inputPattern = { attrs ->
		def type = attrs.type ?: DateTime.name
		if (type instanceof Class) type = type.name
		def locale = attrs.locale ?: RequestContextUtils.getLocale(request)
		if (locale instanceof String) locale = LocaleUtils.toLocale(locale)

		def pattern = patternForType(type)
		if (!pattern) {
			def style
			switch (type) {
				case LocalDate.name:
					style = "S-"
					break
				case LocalTime.name:
					style = "-S"
					break
				default:
					style = "SS"
			}
			pattern = DateTimeFormat.patternForStyle(style, locale)
		}
		out << pattern
	}

	private String patternForType(Class type) {
		patternForType(type.name)
	}

	private String patternForType(String type) {
		grailsApplication.config.flatten()."jodatime.format.${type}" ?: null
	}

}