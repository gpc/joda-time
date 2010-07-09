package jodatest

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class CommandBindingTests extends AbstractFunctionalTestCase {

	void tearDown() {
		super.tearDown()
		ConfigurationHolder.config.jodatime.format.html5 = false		
		ConfigurationHolder.config.jodatime.format."org.joda.time.LocalDate" = null
	}

	void testBindingErrorsOnEmptyLocalDate() {
		get "/command"
		form("localDate") {
			localDate = ""
			click "Submit"
		}
		// should just get a nullable error, "" should bind as null not fail binding
		assertContentContains "Property [localDate] of class [class jodatest.LocalDateCommand] cannot be null"
		assertContentDoesNotContain "Property localDate must be a valid LocalDate"
	}

	void testBindingErrorsOnMalformedLocalDate() {
		get "/command"
		form("localDate") {
			localDate = "INVALID"
			click "Submit"
		}
		// should get a binding error due to invalid format
		assertContentContains "Property localDate must be a valid LocalDate"
	}

	void testLocalDateBoundUsingRequestLocale() {
		get "/command?lang=$Locale.UK"
		form("localDate") {
			localDate = "02/04/09"
			click "Submit"
		}
		assertContentContains "You entered: 2 April 2009"

		get "/command?lang=$Locale.US"
		form("localDate") {
			localDate = "02/04/09"
			click "Submit"
		}
		assertContentContains "You entered: 4 February 2009"
	}

	void testLocalDateBoundUsingHtml5Format() {
		ConfigurationHolder.config.jodatime.format.html5 = true
		get "/command"
		form("localDate") {
			localDate = "2010-07-08"
			click "Submit"
		}
		assertContentContains "You entered: 8 July 2010"
	}

	void testLocalDateBoundUsingConfiguredFormat() {
		ConfigurationHolder.config.jodatime.format."org.joda.time.LocalDate" = "d/M/yyyy"
		get "/command"
		form("localDate") {
			localDate = "8/7/2010"
			click "Submit"
		}
		assertContentContains "You entered: 8 July 2010"
	}
}