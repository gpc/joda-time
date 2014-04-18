package jodatest

import grails.util.Holders
import spock.lang.Unroll

@Unroll
class CommandBindingSpec extends GebSpec {

	def "binding errors are reported for a LocalDate field with the value '#value'"() {
		when:
		go "/command/index"
		$("form").localDate = value
		$("form").submit().click()

		then:
		$(".errors li").text() == expectedError

		where:
		value     | expectedError
		""        | "Property [localDate] of class [class jodatest.LocalDateCommand] cannot be null"
		"INVALID" | "Property localDate must be a valid LocalDate"
	}

	def "a LocalDate property is bound using the #locale locale"() {
        given:
        Holders.config.jodatime.format.html5 = false

		when:
		go "/command/index?lang=$locale"
		$("form").localDate = value
		$("form").submit().click()

		then:
		$(".message").text() == expectedMessage

        cleanup:
        Holders.config.jodatime.format.html5 = true

		where:
		locale    | value      | expectedMessage
		Locale.UK | "02/04/09" | "You entered: 2 April 2009"
		Locale.US | "02/04/09" | "You entered: 4 February 2009"
	}

	def "a LocalDate property can be bound using HTML5 date format"() {
		when:
		go "/command/index"
		$("form").localDate = "2010-07-08"
		$("form").submit().click()

		then:
		$(".message").text() == "You entered: 8 July 2010"
	}

	def "a LocalDate property can be bound using a configured format"() {
		given:
        Holders.config.jodatime.format."org.joda.time.LocalDate" = "d/M/yyyy"

		when:
		go "/command/index"
		$("form").localDate = "8/7/2010"
		$("form").submit().click()

		then:
		$(".message").text() == "You entered: 8 July 2010"

		cleanup:
        Holders.config.jodatime.format."org.joda.time.LocalDate" = null
	}
}
