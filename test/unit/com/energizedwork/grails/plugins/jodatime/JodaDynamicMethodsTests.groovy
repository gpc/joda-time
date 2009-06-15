package com.energizedwork.grails.plugins.jodatime

import static com.energizedwork.grails.plugins.jodatime.JodaDynamicMethods.registerDynamicMethods
import org.joda.time.*
import static org.joda.time.DateTimeUtils.setCurrentMillisFixed
import static org.joda.time.DateTimeUtils.setCurrentMillisSystem

class JodaDynamicMethodsTests extends GroovyTestCase {

	void setUp() {
		registerDynamicMethods()
		setCurrentMillisFixed new DateTime(2008, 10, 2, 2, 50, 0, 0).millis
	}

	void tearDown() {
		setCurrentMillisSystem()
	}

	void testFormatWorksForDateTime() {
		assertEquals('02/10/2008 02:50:00', new DateTime().format('dd/MM/yyyy HH:mm:ss'))
	}

	void testFormatWorksForLocalDate() {
		assertEquals('02/10/2008', new LocalDate().format('dd/MM/yyyy'))
	}

	void testFormatWorksForLocalTime() {
		assertEquals('02:50:00', new LocalTime().format('HH:mm:ss'))
	}

	void testGroovyOperatorsOnSingleFieldPeriods() {
		[Days, Hours, Minutes, Months, Seconds, Weeks, Years].each { clazz ->
			def instance = clazz.newInstance(3)
			assertEquals(clazz.newInstance(-3), -instance)
			assertEquals(clazz.newInstance(6), instance * 2)
			assertEquals(clazz.newInstance(1), instance / 3)
			assertEquals(clazz.newInstance(1), instance / 2) // this is integer division
			// the following should just work
			assertEquals(clazz.newInstance(6), instance + instance)
			assertEquals(clazz.newInstance(5), instance + 2)
			assertEquals(clazz.newInstance(1), instance - clazz.newInstance(2))
			assertEquals(clazz.newInstance(2), instance - 1)
			assertEquals(clazz.newInstance(0), instance + -instance)
		}
	}

}