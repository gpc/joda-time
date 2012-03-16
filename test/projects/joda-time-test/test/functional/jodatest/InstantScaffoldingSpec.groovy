package jodatest

import org.joda.time.Instant
import spock.lang.Unroll

@Unroll
class InstantScaffoldingSpec extends GebSpec {

	Event event1

	def setup() {
		Event.withNewSession {
			event1 = Event.build(description: "Launch", occurred: new Instant(92554380000))
		}
	}

	def cleanup() {
		Event.withNewSession {
			Event.list()*.delete(flush: true)
		}
	}

	def "list"() {
		when:
		go "/event"

		then:
		$("tbody tr", 0).find("td", 0).text() == event1.description
		$("tbody tr", 0).find("td", 1).text() == event1.occurred.toString()
	}

	def "create"() {
		when:
		go "/event/create"
		$("form").description = "Landing"
		$("form").occurred = "1972-12-11T09:25:00.000Z"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Event \d+ created/

		and:
		def event2 = Event.findByDescription("Landing")
		event2.occurred == new Instant(92913900000)
	}

	def "show"() {
		when:
		go "/event/show/$event1.id"

		then:
		$("li.fieldcontain", 1).find(".property-value").text() == "1972-12-07T05:33:00.000Z"
	}

	def "edit"() {
		when:
		go "/event/edit/$event1.id"

		then:
		$("form").description == "Launch"
		$("form").occurred == "1972-12-07T05:33:00.000Z"
	}

	def "list view is sorted after clicking the column header #x times"() {
		given:
		Event.build(description: "Landing", occurred: new Instant(92913900000))
		Event.build(description: "Spacewalk", occurred: new Instant(93436020000))

		when:
		go "/event/list"
		x.times {
			$("th a", text: "Occurred").click()
		}

		then:
		$("tbody tr")*.find("td", 0)*.text() == expected

		where:
		x | expected
		1 | ["Launch", "Landing", "Spacewalk"]
		2 | ["Spacewalk", "Landing", "Launch"]
	}
}
