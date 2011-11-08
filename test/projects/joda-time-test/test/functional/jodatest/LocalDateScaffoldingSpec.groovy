package jodatest

import org.joda.time.LocalDate
import spock.lang.Unroll

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
		$("tbody tr", 0).find("td", 1).text() == "11/29/71"
	}

	def "create"() {
		when:
		go "/person/create"
		$("form").name = "Alex"
		$("form").birthday = "2008-10-02"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Person \d+ created/

		and:
		def alex = Person.findByName("Alex")
		alex.birthday == new LocalDate(2008, 10, 2)
	}

	@Unroll({"show formats LocalDate for $locale locale"})
	def "show"() {
		when:
		go "/person/show/$rob.id?lang=$locale"

		then:
		$("li.fieldcontain", 1).find(".property-value").text() == expectedValue

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

	@Unroll({"list view is sorted after clicking the column header $x times"})
	def "list view is sortable"() {
		given:
		Person.build(name: "Ilse", birthday: new LocalDate(1972, 7, 6))
		Person.build(name: "Alex", birthday: new LocalDate(2008, 10, 2))

		when:
		go "/person/list"
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