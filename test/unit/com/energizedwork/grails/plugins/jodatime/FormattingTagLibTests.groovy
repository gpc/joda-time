package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.JodaTimeUtils
import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.joda.time.chrono.IslamicChronology
import org.joda.time.*

class FormattingTagLibTests extends TagLibUnitTestCase {

	void setUp() {
		super.setUp()

		tagLib.metaClass.getOutput = {-> delegate.out.toString() }

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	protected void tearDown() {
		super.tearDown()
		DateTimeUtils.setCurrentMillisSystem()
	}

	void testFormatDefaultsToCurrentDateTimeInRequestLocale() {
		tagLib.format([:])
		assertEquals('Oct 2, 2008 2:50:33 AM', tagLib.output)
	}

	void testFormatUsesValueIfSpecified() {
		tagLib.format(value: new DateTime(1971, 11, 29, 16, 22, 0, 0))
		assertEquals('Nov 29, 1971 4:22:00 PM', tagLib.output)
	}

	void testFormatUsesLocaleIfSpecified() {
		tagLib.format(locale: Locale.FRANCE)
		assertEquals('2 oct. 2008 02:50:33', tagLib.output)
	}

	void testFormatUsesZoneIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(zone: DateTimeZone.forID('America/Vancouver'))
		}
		assertEquals('Oct 1, 2008 6:50:33 PM', tagLib.output)
	}

	void testFormatUsesChronologyIfSpecified() {
		tagLib.format(chronology: IslamicChronology.instance)
		assertEquals('10 1, 1429 2:50:33 AM', tagLib.output)
	}

	void testFormatUsesStyleIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(style: 'LL')
		}
		assertEquals('October 2, 2008 2:50:33 AM BST', tagLib.output)
	}

	void testFormatAcceptsLocalDateValue() {
		tagLib.format(value: new LocalDate(1985, 10, 13))
		assertEquals('Oct 13, 1985', tagLib.output)
	}

	void testFormatAcceptsLocalTimeValue() {
		tagLib.format(value: new LocalTime(16, 23, 42))
		assertEquals('4:23:42 PM', tagLib.output)
	}

	void testFormatDoesNotAcceptBothStyleAndPattern() {
		shouldFail(GrailsTagException) {
			tagLib.format(style: 'SS', pattern: 'yyyy-MM-dd HH:mm:ss')
		}
	}

	void testFormatUsesPatternIfSpecified() {
		JodaTimeUtils.withDateTimeZone(DateTimeZone.forID('Europe/London')) {
			tagLib.format(pattern: '[z] dd-MM-yyyy@HH:mm:ss')
		}
		assertEquals('[BST] 02-10-2008@02:50:33', tagLib.output)
	}

}