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

import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import static com.energizedwork.grails.plugins.jodatime.Html5DateTimeFormat.*

class Html5InputTagLib {

	static final namespace = "joda"

	private final formTagLib = new FormTagLib(out: out)

	def dateField = {attrs ->
		attrs.type = "date"
		attrs.tagName = "dateField"
		if (attrs.value) {
			attrs.value = date().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

	def timeField = {attrs ->
		attrs.type = "time"
		attrs.tagName = "timeField"
		if (attrs.value) {
			attrs.value = time().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

	def datetimeField = {attrs ->
		attrs.type = "datetime"
		attrs.tagName = "datetimeField"
		if (attrs.value) {
			attrs.value = datetime().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

	def datetimeLocalField = {attrs ->
		attrs.type = "datetime-local"
		attrs.tagName = "datetimeLocalField"
		if (attrs.value) {
			attrs.value = datetimeLocal().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

	def monthField = {attrs ->
		attrs.type = "month"
		attrs.tagName = "monthField"
		if (attrs.value) {
			attrs.value = month().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

	def weekField = {attrs ->
		attrs.type = "week"
		attrs.tagName = "weekField"
		if (attrs.value) {
			attrs.value = week().print(attrs.value)
		}
		formTagLib.fieldImpl(out, attrs)
	}

}
