package jodatest

import spock.lang.*
import grails.plugin.geb.*
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import static javax.servlet.http.HttpServletResponse.SC_OK

class LocalDateScaffoldingSpec extends GebSpec {

	def rob

	def setup() {
		rob = Person.build(name: "Rob", birthday: new LocalDate(1971, 11, 29))
	}

	def cleanup() {
		Person.list()*.delete(flush: true)
	}

	def "list"() {
		when:
		go "/person"
		
		then:
		$("tbody tr", 0).find("td", 0).text() == "$rob.id"
		$("tbody tr", 0).find("td", 1).text() == "Rob"
		$("tbody tr", 0).find("td", 2).text() == "11/29/71"
	}

	def "create"() {
		when:
		go "/person/create"
		$("form").name = "Alex"
		$("form").birthday_day = "2"
		$("form").birthday_month = "10"
		$("form").birthday_year = "2008"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Person \d+ created/

		and:
		def alex = Person.findByName("Alex")
		alex.birthday == new LocalDate(2008, 10, 2)
	}

	@Unroll("show formats LocalDate for #locale locale")
	def "show"() {
		when:
		go "/person/show/$rob.id?lang=$locale"

		then:
		$("tr", 0).find("td.value").text() == "$rob.id"
		$("tr", 1).find("td.value").text() == "Rob"
		$("tr", 2).find("td.value").text() == expectedValue
		
		where:
		locale               | expectedValue
		Locale.UK            | "29/11/71"
		Locale.US            | "11/29/71"
		Locale.FRANCE        | "29/11/71"
		Locale.GERMANY       | "29.11.71"
		Locale.CANADA        | "29/11/71"
		Locale.CANADA_FRENCH | "71-11-29"
	}

	def "edit"() {
		when:
		go "/person/edit/$rob.id"

		then:
		$("form").name == "Rob"
		$("form").birthday_day == "29"
		$("form").birthday_month == "11"
		$("form").birthday_year == "1971"
	}

	@Unroll("list view is sorted after clicking the column header #x times")
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
		$("tbody tr")*.find("td", 1)*.text() == expected
		
		where:
		x | expected
		1 | ["Rob", "Ilse", "Alex"]
		2 | ["Alex", "Ilse", "Rob"]
	}
}