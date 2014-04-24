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

import java.text.DateFormatSymbols
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.springframework.web.servlet.support.RequestContextUtils
import org.joda.time.*

class DateTimeTagLib {

	static namespace = "joda"
	static defaultEncodeAs = "raw"

	def datePicker = {attrs ->
		log.debug '***** joda:datePicker *****'
		def fields = [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()]
		renderPicker(fields, attrs)
	}

	def timePicker = {attrs ->
		log.debug '***** joda:timePicker *****'
		def fields = [DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute()]
		renderPicker(fields, attrs)
	}

	def dateTimePicker = {attrs ->
		log.debug '***** joda:dateTimePicker *****'
		def fields = [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute()]
		renderPicker(fields, attrs)
	}

	def renderPicker = {List fields, attrs ->
		def precision = attrs.precision ?: (grailsApplication.config.grails.tags.datePicker.default.precision ?: 'minute')
		log.debug "precision = $precision"

		log.debug "fields = $fields"
		switch (precision) {
			case 'year': fields.remove(DateTimeFieldType.monthOfYear())
			case 'month': fields.remove(DateTimeFieldType.dayOfMonth())
			case 'day': fields.remove(DateTimeFieldType.hourOfDay())
			case 'hour': fields.remove(DateTimeFieldType.minuteOfHour())
			case 'minute': fields.remove(DateTimeFieldType.secondOfMinute())
		}
		log.debug "fields = $fields"

		def defaultValue = attrs.'default'
		if (!defaultValue) {
			defaultValue = new DateTime()
		} else if (defaultValue == 'none') {
			defaultValue = null
		} else if (defaultValue instanceof String) {
			defaultValue = getParser(fields).parseDateTime(defaultValue)
		} else if (!(defaultValue instanceof ReadableInstant) && !(defaultValue instanceof ReadablePartial)) {
			throwTagError("Tag [datePicker] requires the default date to be a parseable String or instanceof ReadableInstant or ReadablePartial")
		}
		log.debug "default = $defaultValue"

		def value = attrs.value
		if (value == 'none') {
			value = null
		} else if (!value) {
			value = defaultValue
		}
		log.debug "value = $value"

		def name = attrs.name
		def id = attrs.id ?: name

		def noSelection = attrs.noSelection
		if (noSelection) {
			noSelection = noSelection.entrySet().iterator().next()
		}

		def years = attrs.years

		def dfs = new DateFormatSymbols(RequestContextUtils.getLocale(request))

		if (!years) {
			def tempyear = null
			if (value && value?.isSupported(DateTimeFieldType.year())) tempyear = value.year
			else tempyear = new LocalDate().year
            years = (tempyear - (grailsApplication.config.grails.tags.datePicker.default.yearsBelow ?: 100))..(tempyear + (grailsApplication.config.grails.tags.datePicker.default.yearsAbove ?: 100))
		}

		log.debug "starting rendering"
		out << "<input type=\"hidden\" name=\"$name\" value=\"struct\" />"

		// create day select
		if (fields.contains(DateTimeFieldType.dayOfMonth())) {
			log.debug "rendering day"
			out.println "<select name=\"${name}_day\" id=\"${id}_day\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.dayOfMonth)
				out.println()
			}

			for (i in 1..31) {
				out << "<option value=\"${i}\""
				if (i == value?.dayOfMonth) {
					out << " selected=\"selected\""
				}
				out.println ">${i}</option>"
			}
			out.println '</select>'
		}

		// create month select
		if (fields.contains(DateTimeFieldType.monthOfYear())) {
			log.debug "rendering month"
			out.println "<select name=\"${name}_month\" id=\"${id}_month\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.monthOfYear)
				out.println()
			}

			dfs.months.eachWithIndex {m, i ->
				if (m) {
					def monthIndex = i + 1
					out << "<option value=\"${monthIndex}\""
					if (monthIndex == value?.monthOfYear) out << " selected=\"selected\""
					out << '>'
					out << m
					out.println '</option>'
				}
			}
			out.println '</select>'
		}

		// create year select
		if (fields.contains(DateTimeFieldType.year())) {
			log.debug "rendering year"
			out.println "<select name=\"${name}_year\" id=\"${id}_year\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.year)
				out.println()
			}

			for (i in years) {
				out << "<option value=\"${i}\""
				if (i == value?.year) {
					out << " selected=\"selected\""
				}
				out.println ">${i}</option>"
			}
			out.println '</select>'
		}

		// do hour select
		if (fields.contains(DateTimeFieldType.hourOfDay())) {
			log.debug "rendering hour"
			out.println "<select name=\"${name}_hour\" id=\"${id}_hour\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.hourOfDay)
				out.println()
			}

			for (i in 0..23) {
				def h = i.toString().padLeft(2, '0')
				out << "<option value=\"${h}\""
				if (value?.hourOfDay == i) out << " selected=\"selected\""
				out << '>' << h << '</option>'
				out.println()
			}
			out.println '</select> :'

			// If we're rendering the hour, but not the minutes, then display the minutes and seconds as 00 in read-only format
			if (!fields.contains(DateTimeFieldType.minuteOfHour())) {
				out.println '00:00'
			}
		}

		// do minute select
		if (fields.contains(DateTimeFieldType.minuteOfHour())) {
			log.debug "rendering minute"
			out.println "<select name=\"${name}_minute\" id=\"${id}_minute\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.minuteOfHour)
				out.println()
			}

			for (i in 0..59) {
				def m = i.toString().padLeft(2, '0')
				out << "<option value=\"${m}\""
				if (value?.minuteOfHour == i) out << " selected=\"selected\""
				out << '>' << m << '</option>'
				out.println()
			}
			out.println '</select> :'

			// If we're rendering the minutes, but not the seconds, then display the seconds as 00 in read-only format
			if (!fields.contains(DateTimeFieldType.secondOfMinute())) {
				out.println '00'
			}
		}

		// do second select
		if (fields.contains(DateTimeFieldType.secondOfMinute())) {
			log.debug "rendering second"
			out.println "<select name=\"${name}_second\" id=\"${id}_second\">"

			if (noSelection) {
				renderNoSelectionOption(noSelection.key, noSelection.value, value?.secondOfMinute)
				out.println()
			}

			for (i in 0..59) {
				def s = i.toString().padLeft(2, '0')
				out << "<option value=\"${s}\""
				if (value?.secondOfMinute == i) out << " selected=\"selected\""
				out << '>' << s << '</option>'
				out.println()
			}
			out.println '</select>'
		}

		// do zone select
		if (attrs.useZone == "true") {
			out << dateTimeZoneSelect(name: "${name}_zone")
		}

		log.debug "done"
	}

	private DateTimeFormatter getParser(List fields) {
		DateTimeFormatter formatter
		if (fields == [DateTimeFieldType.year()]) {
			formatter = ISODateTimeFormat.year()
		} else if (fields == [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()]) {
			formatter = ISODateTimeFormat.yearMonth()
		} else if (fields == [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()]) {
			formatter = ISODateTimeFormat.yearMonthDay()
		} else if (fields == [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay()]) {
			formatter = ISODateTimeFormat.dateHour()
		} else if (fields == [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()]) {
			formatter = ISODateTimeFormat.dateHourMinute()
		} else if (fields == [DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute()]) {
			formatter = ISODateTimeFormat.dateHourMinuteSecond()
		} else if (fields == [DateTimeFieldType.hourOfDay()]) {
			formatter = ISODateTimeFormat.hour()
		} else if (fields == [DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()]) {
			formatter = ISODateTimeFormat.hourMinute()
		} else if (fields == [DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute()]) {
			formatter = ISODateTimeFormat.hourMinuteSecond()
		} else {
			throw new GrailsTagException("Invalid combination of date/time fields: $fields")
		}
		return formatter
	}

	def renderNoSelectionOption = {noSelectionKey, noSelectionValue, value ->
		// If a label for the '--Please choose--' first item is supplied, write it out
		value = (value == null ? '' : value)
		out << '<option value="' << (noSelectionKey == null ? "" : noSelectionKey) << '"'
		if (noSelectionKey == value) {
			out << ' selected="selected"'
		}
		out << '>' << noSelectionValue.encodeAsHTML() << '</option>'
	}

}
