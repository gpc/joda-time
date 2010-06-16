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

import org.joda.time.DateTimeZone
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.nullValue
import static org.junit.Assert.assertThat

class DateTimeZoneEditorTests {

	@Test
	void getAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.value = null
		assertThat editor.asText, equalTo("")
	}

	@Test
	void getAsTextReturnsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.value = DateTimeZone.forID("Europe/London")
		assertThat editor.asText, equalTo("Europe/London")
	}

	@Test
	void setAsTextAcceptsZoneID() {
		def editor = new DateTimeZoneEditor()
		editor.asText = "Europe/London"
		assertThat editor.value, equalTo(DateTimeZone.forID("Europe/London"))
	}

	@Test
	void setAsTextHandlesNull() {
		def editor = new DateTimeZoneEditor()
		editor.asText = ""
		assertThat editor.value, nullValue()
	}

}