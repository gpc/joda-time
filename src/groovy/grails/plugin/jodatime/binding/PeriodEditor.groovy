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
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatter
import org.joda.time.Period
import org.joda.time.Duration
import org.joda.time.PeriodType
import org.joda.time.DurationFieldType

class PeriodEditor extends PropertyEditorSupport {

	private static final PeriodFormatter FORMATTER = PeriodFormat.default

	static final SUPPORTED_TYPES = [Duration, Period].asImmutable()
	private static final DURATION_PERIOD_TYPE = PeriodType.forFields([DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()] as DurationFieldType[])

	protected final Class type

	PeriodEditor(Class type) {
		this.type = type
	}
	
	String getAsText() {
		if (!value) {
			return ""
		} else if (type == Period) {
			return FORMATTER.print(value)
		} else if (type == Duration) {
			return FORMATTER.print(value.toPeriod(DURATION_PERIOD_TYPE))
		} else {
			throw new IllegalStateException("Unsupported type $type")
		}
	}

	void setAsText(String text) {
		if (!text) {
			value = null
		} else if (type == Period) {
			value = FORMATTER.parsePeriod(text)
		} else if (type == Duration) {
			value = FORMATTER.parsePeriod(text).toStandardDuration()
		} else {
			throw new IllegalStateException("Unsupported type $type")
		}
	}

}