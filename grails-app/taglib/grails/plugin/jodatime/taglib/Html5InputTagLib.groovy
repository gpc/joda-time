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

import static grails.plugin.jodatime.Html5DateTimeFormat.*
import static org.codehaus.groovy.grails.web.pages.GroovyPage.EMPTY_BODY_CLOSURE
import org.joda.time.*

class Html5InputTagLib {

	static final namespace = "joda"
	static defaultEncodeAs = "raw"

	def dateField = {attrs ->
		attrs.type = "date"
		attrs.tagName = "dateField"
		if (attrs.value) {
			attrs.value = date().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def timeField = {attrs ->
		attrs.type = "time"
		attrs.tagName = "timeField"
		if (attrs.value) {
			attrs.value = time().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def datetimeField = {attrs ->
		attrs.type = "datetime"
		attrs.tagName = "datetimeField"
		if (attrs.value) {
			attrs.value = datetime().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def datetimeLocalField = {attrs ->
		attrs.type = "datetime-local"
		attrs.tagName = "datetimeLocalField"
		if (attrs.value) {
			attrs.value = datetimeLocal().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def monthField = {attrs ->
		attrs.type = "month"
		attrs.tagName = "monthField"
		if (attrs.value) {
			attrs.value = month().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def weekField = {attrs ->
		attrs.type = "week"
		attrs.tagName = "weekField"
		if (attrs.value) {
			attrs.value = week().print(attrs.value)
		}
		out << g.field(attrs)
	}

	def time = {attrs, body ->
		def value = attrs.containsKey("value") ? attrs.remove("value") : new DateTime()
		if (value) {
			def var = attrs.remove("var")
			def datetimeString
			switch (value) {
				case LocalDate:
				case YearMonthDay:
					datetimeString = date().print(value)
					break
				case LocalTime:
					datetimeString = timeShort().print(value)
					break
				case LocalDateTime:
					datetimeString = datetimeLocalShort().print(value)
					break
				case ReadableInstant:
					datetimeString = datetimeShort().print(value)
					break
				default:
					throwTagError("the joda:time tag requires a ReadableInstant or ReadablePartial value")
			}
			out << '<time datetime="' << datetimeString << '"'
			for (attr in attrs) {
				out << ' ' << attr.key << '="' << attr.value << '"'
			}
			out << '>'
			if (body != EMPTY_BODY_CLOSURE) {
				if (var) {
					out << body((var): value)
				} else {
					out << body(value)
				}
			} else {
				out << joda.format(value: value)
			}
			out << '</time>'
		}
	}

}
