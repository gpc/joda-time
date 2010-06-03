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

import org.joda.time.Duration
import org.joda.time.Period
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.nullValue
import static org.junit.Assert.assertThat

class PeriodEditorTests {

	@Test
	void getAsTextHandlesNull() {
		def editor = new PeriodEditor(Period)
		editor.value = null
		assertThat editor.asText, equalTo("")
	}

	@Test
	void setAsTextHandlesNull() {
		def editor = new PeriodEditor(Period)
		editor.asText = null
		assertThat editor.value, nullValue()
	}

	@Test
	void setAsTextHandlesEmptyString() {
		def editor = new PeriodEditor(Period)
		editor.asText = ""
		assertThat editor.value, nullValue()
	}

	@Test
	void getAsTextFormatsValueCorrectly() {
		def editor = new PeriodEditor(Period)
		editor.value = new Period(1, 2, 0, 4, 8, 12, 35, 0)
		assertThat editor.asText, equalTo("1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds")
	}

	@Test
	void getAsTextWithValueOverStandardRange() {
		def editor = new PeriodEditor(Period)
		editor.value = new Period(0, 120, 12, 0)
		assertThat editor.asText, equalTo("120 minutes and 12 seconds")
	}

	@Test
	void setAsTextParsesValueCorrectly() {
		def editor = new PeriodEditor(Period)
		editor.asText = "1 year, 2 months, 4 days, 8 hours, 12 minutes and 35 seconds"
		assertThat editor.value, equalTo(new Period(1, 2, 0, 4, 8, 12, 35, 0))
	}

	@Test
	void setAsTextSupportsDuration() {
		def editor = new PeriodEditor(Duration)
		editor.asText = "1 hour, 35 minutes and 16 seconds"
		assertThat editor.value, equalTo(new Period(1, 35, 16, 0).toStandardDuration())
	}

	@Test
	void getAsTextSupportsDuration() {
		def editor = new PeriodEditor(Duration)
		editor.value = new Period(1, 35, 16, 0).toStandardDuration()
		assertThat editor.asText, equalTo("1 hour, 35 minutes and 16 seconds")
	}

}