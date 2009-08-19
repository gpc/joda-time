package jodatest

class CommandBindingTests extends AbstractFunctionalTestCase {

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
}