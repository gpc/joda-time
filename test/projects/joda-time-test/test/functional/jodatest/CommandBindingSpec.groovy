package jodatest

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import spock.lang.Unroll

class CommandBindingSpec extends GebSpec {

	@Unroll({"binding errors are reported for a LocalDate field with the value '$value'"})
	def "binding errors are reported for LocalDate fields"() {
		when:
		go "/command"
		$("form").localDate = value
		$("form").submit().click()

		then:
		$(".errors li").text() == expectedError
		
		where:
		value     | expectedError
		""        | "Property [localDate] of class [class jodatest.LocalDateCommand] cannot be null"
		"INVALID" | "Property localDate must be a valid LocalDate"
	}
	
	@Unroll({"a LocalDate property is bound using the $locale locale"})
	def "a LocalDate property is bound using the request locale"() {
        given:
        ConfigurationHolder.config.jodatime.format.html5 = false

		when:
		go "/command?lang=$locale"
		$("form").localDate = value
		$("form").submit().click()

		then:
		$(".message").text() == expectedMessage

        cleanup:
        ConfigurationHolder.config.jodatime.format.html5 = true

		where:
		locale    | value      | expectedMessage
		Locale.UK | "02/04/09" | "You entered: 2 April 2009"
		Locale.US | "02/04/09" | "You entered: 4 February 2009"
	}
	
	def "a LocalDate property can be bound using HTML5 date format"() {
		when:
		go "/command"
		$("form").localDate = "2010-07-08"
		$("form").submit().click()

		then:
		$(".message").text() == "You entered: 8 July 2010"
	}
	
	def "a LocalDate property can be bound using a configured format"() {
		given:
		ConfigurationHolder.config.jodatime.format."org.joda.time.LocalDate" = "d/M/yyyy"

		when:
		go "/command"
		$("form").localDate = "8/7/2010"
		$("form").submit().click()

		then:
		$(".message").text() == "You entered: 8 July 2010"
		
		cleanup:
		ConfigurationHolder.config.jodatime.format."org.joda.time.LocalDate" = null
	}
}