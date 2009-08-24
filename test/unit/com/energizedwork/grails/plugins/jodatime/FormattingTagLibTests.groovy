package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.JodaTimeUtils
import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.chrono.IslamicChronology
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class FormattingTagLibTests extends TagLibUnitTestCase {

	void setUp() {
		super.setUp()

		tagLib.metaClass.getOutput = {-> delegate.out.toString() }

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()

		tagLib.request.addPreferredLocale Locale.UK
	}

	protected void tearDown() {
		super.tearDown()
		DateTimeUtils.setCurrentMillisSystem()
	}

	void testFormatDefaultsToCurrentDateTimeInRequestLocale() {
		tagLib.format([:])
		assertEquals("02-Oct-2008 02:50:33", tagLib.output)
	}

	void testFormatUsesValueIfSpecified() {
		tagLib.format(value: new DateTime(1971, 11, 29, 16, 22, 0, 0))
		assertEquals("29-Nov-1971 16:22:00", tagLib.output)
	}

	void testFormatUsesLocaleIfSpecified() {
		tagLib.format(locale: Locale.FRANCE)
		assertEquals("2 oct. 2008 02:50:33", tagLib.output)
	}

	void testFormatUsesZoneIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			tagLib.format(zone: DateTimeZone.forID("America/Vancouver"))
		}
		assertEquals("01-Oct-2008 18:50:33", tagLib.output)
	}

	void testFormatUsesChronologyIfSpecified() {
		tagLib.format(chronology: IslamicChronology.instance)
		assertEquals("01-10-1429 02:50:33", tagLib.output)
	}

	void testFormatUsesStyleIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(style: 'LL')
		}
		assertEquals("02 October 2008 02:50:33 BST", tagLib.output)
	}

	void testFormatAcceptsLocalDateValue() {
		tagLib.format(value: new LocalDate(1985, 10, 13))
		assertEquals("13-Oct-1985", tagLib.output)
	}

	void testFormatAcceptsLocalTimeValue() {
		tagLib.format(value: new LocalTime(16, 23, 42))
		assertEquals("16:23:42", tagLib.output)
	}

	void testFormatDoesNotAcceptBothStyleAndPattern() {
		shouldFail(GrailsTagException) {
			tagLib.format(style: "SS", pattern: "yyyy-MM-dd HH:mm:ss")
		}
	}

	void testFormatUsesPatternIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			tagLib.format(pattern: "[z] dd-MM-yyyy@HH:mm:ss")
		}
		assertEquals("[BST] 02-10-2008@02:50:33", tagLib.output)
	}

	void testFormatUsesPatternIfConfigured() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="[z] dd-MM-yyyy@HH:mm:ss"
		'''
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID("Europe/London")) {
			tagLib.format([:])
		}
		assertEquals("[BST] 02-10-2008@02:50:33", tagLib.output)
	}

	void testPatternDefaultsToDateTime() {
		tagLib.inputPattern([:])
		assertEquals DateTimeFormat.patternForStyle("SS", Locale.UK), tagLib.output
	}

	void testPatternAcceptsTypeAttribute() {
		tagLib.inputPattern(type: LocalDate)
		assertEquals DateTimeFormat.patternForStyle("S-", Locale.UK), tagLib.output
	}

	void testPatternDisplaysConfiguredPatternIfSet() {
		mockConfig '''
			jodatime.format.org.joda.time.DateTime="[z] dd-MM-yyyy@HH:mm:ss"
		'''
		tagLib.inputPattern([:])
		assertEquals "[z] dd-MM-yyyy@HH:mm:ss", tagLib.output
	}

}