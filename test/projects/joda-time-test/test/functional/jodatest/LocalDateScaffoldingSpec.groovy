package jodatest

import org.joda.time.LocalDate
import spock.lang.Unroll
import org.codehaus.groovy.grails.commons.ConfigurationHolder

@Unroll
class LocalDateScaffoldingSpec extends GebSpec {

	def rob

	def setup() {
		Person.withNewSession {
			rob = Person.build(name: "Rob", birthday: new LocalDate(1971, 11, 29))
		}
	}

	def cleanup() {
		Person.withNewSession {
			Person.list()*.delete(flush: true)
		}
	}

	def "list"() {
		when:
		go "/person"

		then:
		$("tbody tr", 0).find("td", 0).text() == rob.name
		$("tbody tr", 0).find("td", 1).text() == "1971-11-29"
	}

	def "create accepts '#value' as a LocalDate for locale #locale when HTML5 format is #html5Format"() {
		given:
		ConfigurationHolder.config.jodatime.format.html5 = html5Format

		when:
		go "/person/create?lang=$locale"
		$("form").name = "Alex"
		$("form").birthday() << value
		$("form").create().click()

		then:
		$(".message").text() ==~ /Person \d+ created/

		and:
		def alex = Person.findByName("Alex")
		alex.birthday == new LocalDate(2008, 10, 2)

		cleanup:
		ConfigurationHolder.config.jodatime.format.html5 = true

		where:
		html5Format | locale               | value
		false       | Locale.UK            | "2/10/08"
		false       | Locale.US            | "10/2/08"
		false       | Locale.CANADA_FRENCH | "08-10-02"
		true        | Locale.US            | "2008-10-02"
		true        | Locale.UK            | "2008-10-02"
		true        | Locale.CANADA_FRENCH | "2008-10-02"
	}

	def "show formats LocalDate for #locale locale"() {
		given:
		ConfigurationHolder.config.jodatime.format.html5 = false

		when:
		go "/person/show/$rob.id?lang=$locale"

		then:
		$("li.fieldcontain", 1).find(".property-value").text() == expectedValue

		cleanup:
		ConfigurationHolder.config.jodatime.format.html5 = true

		where:
		locale               | expectedValue
		Locale.UK            | "29/11/71"
		Locale.US            | "11/29/71"
		Locale.CANADA_FRENCH | "71-11-29"
	}

	def "edit"() {
		when:
		go "/person/edit/$rob.id"

		then:
		$("form").birthday == "1971-11-29"
	}

	def "list view is sorted after clicking the column header #x times"() {
		given:
		Person.build(name: "Ilse", birthday: new LocalDate(1972, 7, 6))
		Person.build(name: "Alex", birthday: new LocalDate(2008, 10, 2))

		when:
		go "/person"
		x.times {
			$("th a", text: "Birthday").click()
		}

		then:
		$("tbody tr")*.find("td", 0)*.text() == expected

		where:
		x | expected
		1 | ["Rob", "Ilse", "Alex"]
		2 | ["Alex", "Ilse", "Rob"]
	}
}