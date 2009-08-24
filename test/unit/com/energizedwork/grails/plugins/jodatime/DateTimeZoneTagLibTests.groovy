package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.joda.time.DateTimeZone
import org.joda.time.DateTimeUtils
import org.joda.time.DateTime

class DateTimeZoneTagLibTests extends TagLibUnitTestCase {

	def defaultZone
	def selectAttrs

	void setUp() {
		super.setUp()

		tagLib.metaClass.select = {attrs ->
			selectAttrs = attrs
		}

		defaultZone = DateTimeZone.default
	}

	void tearDown() {
		super.tearDown()

		DateTimeZone.default = defaultZone
		DateTimeUtils.setCurrentMillisSystem()
	}

	void testCurrentZoneIsSelectedByDefault() {
		tagLib.dateTimeZoneSelect([:])
		assertEquals DateTimeZone.default.ID, selectAttrs.value
	}

	void testValueAttributeIsPassedToSelect() {
		def zone = DateTimeZone.forID("Canada/Pacific")
		tagLib.dateTimeZoneSelect(value: zone)
		assertEquals zone, DateTimeZone.forID(selectAttrs.value)
	}

	void testOptionFormatting() {
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		tagLib.dateTimeZoneSelect(value: DateTimeZone.forID("America/Vancouver"))
		assertEquals "America/Vancouver -08:00", selectAttrs.optionValue(selectAttrs.value)

		tagLib.dateTimeZoneSelect(value: DateTimeZone.forID("Europe/London"))
		assertEquals "Europe/London +00:00", selectAttrs.optionValue(selectAttrs.value)
	}

	void testOptionsAreDSTSensetive() {
		DateTimeUtils.setCurrentMillisFixed new DateTime(2009, 8, 1, 0, 0, 0, 0, DateTimeZone.UTC).millis

		tagLib.dateTimeZoneSelect(value: DateTimeZone.forID("America/Vancouver"))
		assertEquals "America/Vancouver -07:00", selectAttrs.optionValue(selectAttrs.value)

		tagLib.dateTimeZoneSelect(value: DateTimeZone.forID("Europe/London"))
		assertEquals "Europe/London +01:00", selectAttrs.optionValue(selectAttrs.value)
	}

	void testNoDuplicateOptionsAppear() {
		tagLib.dateTimeZoneSelect([:])
		def options = selectAttrs.from.collect {
			selectAttrs.optionValue(it)
		}
		assertEquals options.unique(), options
	}

}
