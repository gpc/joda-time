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
package grails.plugins.jodatime.taglib

import grails.plugins.jodatime.JodaTimeUtils
import grails.testing.web.taglib.TagLibUnitTest
import org.grails.taglib.GrailsTagException
import org.joda.time.*
import org.joda.time.chrono.IslamicChronology
import org.joda.time.format.DateTimeFormat
import org.junit.After
import org.junit.Test
import spock.lang.Specification

class FormattingTagLibTests extends Specification implements TagLibUnitTest<FormattingTagLib>{

	DateTimeZone defaultTimeZone

	void setup() {
		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0, DateTimeZone.forID("Europe/London"))
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
		defaultTimeZone = DateTimeZone.default
		DateTimeZone.default = DateTimeZone.forID("Europe/London")

		tagLib.request.addPreferredLocale Locale.UK
	}

	void cleanup() {
		grailsApplication.config.jodatime.clear()
		DateTimeUtils.setCurrentMillisSystem()
		DateTimeZone.default = defaultTimeZone
	}

	@Test
	void formatDefaultsToCurrentDateTimeInRequestLocale() {
		assert applyTemplate('<joda:format/>') == "02-Oct-2008 02:50:33"
	}

	@Test
	void formatDoesNotDefaultsValueIfPassedNull() {
		assert applyTemplate('<joda:format value="${value}"/>', [value: null]) == ""
	}

	@Test
	void formatUsesValueIfSpecified() {
		assert applyTemplate('<joda:format value="${value}"/>', [value: new DateTime(1971, 11, 29, 16, 22, 0, 0)]) == "29-Nov-1971 16:22:00"
	}

	@Test
	void formatUsesLocaleIfSpecified() {
		assert applyTemplate('<joda:format locale="${locale}"/>', [locale: Locale.FRANCE]) == "2 oct. 2008 02:50:33"
	}

	@Test
	void formatUsesZoneIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			assert applyTemplate('<joda:format zone="${zone}"/>', [zone: DateTimeZone.forID("America/Vancouver")]) == "01-Oct-2008 18:50:33"
		}
	}

	@Test
	void formatUsesChronologyIfSpecified() {
		assert applyTemplate('<joda:format chronology="${chronology}"/>', [chronology: IslamicChronology.instance]) == "01-10-1429 02:50:33"
	}

	@Test
	void formatUsesStyleIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			assert applyTemplate('<joda:format style="LL"/>') == "02 October 2008 02:50:33 BST"
		}
	}

	@Test
	void formatStyleAttributeInPreferenceToConfigDefault() {
		grailsApplication.config.jodatime.format.org.joda.time.DateTime = "[z] dd-MM-yyyy@HH:mm:ss"

		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			assert applyTemplate('<joda:format style="F-"/>') == "Thursday, 2 October 2008"
		}
	}

	@Test
	void formatAcceptsLocalDateValue() {
		assert applyTemplate('<joda:format value="${value}"/>', [value: new LocalDate(1985, 10, 13)]) == "13-Oct-1985"
	}

	@Test
	void formatAcceptsLocalTimeValue() {
		assert applyTemplate('<joda:format value="${value}"/>', [value: new LocalTime(16, 23, 42)]) == "16:23:42"
	}

	@Test(expected = GrailsTagException)
	void formatDoesNotAcceptBothStyleAndPattern() {
		assert applyTemplate('<joda:format style="SS" pattern="yyyy-MM-dd HH:mm:ss"/>')
	}

	@Test
	void formatUsesPatternIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			assert applyTemplate('<joda:format pattern="[z] dd-MM-yyyy@HH:mm:ss"/>') == "[BST] 02-10-2008@02:50:33"
		}
	}

	@Test
	void formatUsesPatternIfConfigured() {
		grailsApplication.config.jodatime.format.org.joda.time.DateTime = "[z] dd-MM-yyyy@HH:mm:ss"

		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			assert applyTemplate('<joda:format/>') == "[BST] 02-10-2008@02:50:33"
		}
	}

	@Test
	void patternDefaultsToDateTime() {
		assert applyTemplate('<joda:inputPattern/>') == DateTimeFormat.patternForStyle("SS", Locale.UK)
	}

	@Test
	void patternAcceptsTypeAttribute() {
		assert applyTemplate('<joda:inputPattern type="${type}"/>', [type: LocalDate]) == DateTimeFormat.patternForStyle("S-", Locale.UK)
	}

	@Test
	void patternAcceptsTypeAsString() {
		assert applyTemplate('<joda:inputPattern type="${type}"/>', [type: LocalDate.name]) == DateTimeFormat.patternForStyle("S-", Locale.UK)
	}

	@Test
	void patternAcceptsLocaleAttribute() {
		assert applyTemplate('<joda:inputPattern locale="${locale}"/>', [locale: Locale.CANADA_FRENCH]) == DateTimeFormat.patternForStyle("SS", Locale.CANADA_FRENCH)
	}

	@Test
	void patternAcceptsLocaleAsString() {
		assert applyTemplate('<joda:inputPattern locale="fr_CA"/>') == DateTimeFormat.patternForStyle("SS", Locale.CANADA_FRENCH)
	}

	@Test
	void patternDisplaysConfiguredPatternIfSet() {
		grailsApplication.config.jodatime.format.org.joda.time.DateTime = "[z] dd-MM-yyyy@HH:mm:ss"

		assert applyTemplate('<joda:inputPattern/>') == "[z] dd-MM-yyyy@HH:mm:ss"
	}
}
