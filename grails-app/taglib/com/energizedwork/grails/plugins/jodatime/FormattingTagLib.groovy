package com.energizedwork.grails.plugins.jodatime

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.springframework.web.servlet.support.RequestContextUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class FormattingTagLib {

	static namespace = "joda"

	def format = {attrs ->
		if (attrs.pattern && attrs.style) {
			throwTagError('Cannot specify both pattern and style attributes')
		}

		def value = attrs.value ?: new DateTime()
		def locale = attrs.locale ?: RequestContextUtils.getLocale(request)
		def zone = attrs.zone
		def chronology = attrs.chronology

		def pattern = attrs.pattern ?: patternForType(value.getClass())
		def style = attrs.style
		if (!style) {
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

	def inputPattern = { attrs ->
		def type = attrs.type ?: DateTime
		def locale = attrs.locale ?: RequestContextUtils.getLocale(request)
		def pattern = patternForType(type)
		if (!pattern) {
			def style
			switch (type) {
				case LocalDate:
					style = "S-"
					break
				case LocalTime:
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
		ConfigurationHolder.config?.flatten()?."jodatime.format.${type.name}"
	}

}