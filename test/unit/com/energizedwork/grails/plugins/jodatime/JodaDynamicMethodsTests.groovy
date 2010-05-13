package com.energizedwork.grails.plugins.jodatime

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import static com.energizedwork.grails.plugins.jodatime.JodaDynamicMethods.registerDynamicMethods
import static org.hamcrest.CoreMatchers.equalTo
import org.joda.time.*
import static org.joda.time.DateTimeUtils.setCurrentMillisFixed
import static org.joda.time.DateTimeUtils.setCurrentMillisSystem
import static org.junit.Assert.assertThat

class JodaDynamicMethodsTests extends GroovyTestCase {

	@BeforeClass static void setUp() {
		registerDynamicMethods()
		setCurrentMillisFixed new DateTime(2008, 10, 2, 2, 50, 0, 0).millis
	}

	@AfterClass static void tearDown() {
		setCurrentMillisSystem()
	}

	@Test
	void formatWorksForDateTime() {
		assertThat new DateTime().format('dd/MM/yyyy HH:mm:ss'), equalTo('02/10/2008 02:50:00')
	}

	@Test
	void formatWorksForLocalDate() {
		assertThat new LocalDate().format('dd/MM/yyyy'), equalTo('02/10/2008')
	}

	@Test
	void formatWorksForLocalTime() {
		assertThat new LocalTime().format('HH:mm:ss'), equalTo('02:50:00')
	}

	@Test
	void groovyOperatorsOnSingleFieldPeriods() {
		[Days, Hours, Minutes, Months, Seconds, Weeks, Years].each { clazz ->
			def instance = clazz.newInstance(3)
			assertThat(-instance, equalTo(clazz.newInstance(-3)))
			assertThat instance * 2, equalTo(clazz.newInstance(6))
			assertThat instance / 3, equalTo(clazz.newInstance(1))
			assertThat instance / 2, equalTo(clazz.newInstance(1)) // this is integer division
			// the following should just work
			assertThat instance + instance, equalTo(clazz.newInstance(6))
			assertThat instance + 2, equalTo(clazz.newInstance(5))
			assertThat instance - clazz.newInstance(2), equalTo(clazz.newInstance(1))
			assertThat instance - 1, equalTo(clazz.newInstance(2))
			assertThat instance + -instance, equalTo(clazz.newInstance(0))
		}
	}

}