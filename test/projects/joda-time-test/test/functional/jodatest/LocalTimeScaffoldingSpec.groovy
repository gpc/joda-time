package jodatest

import geb.spock.GebSpec
import org.joda.time.LocalTime
import spock.lang.Unroll

class LocalTimeScaffoldingSpec extends GebSpec {

	def alarm1

	def setup() {
		Alarm.withNewSession {
			alarm1 = Alarm.build(description: "Morning", time: new LocalTime(7, 0))
		}
	}

	def cleanup() {
		Alarm.withNewSession {
			Alarm.list()*.delete(flush: true)
		}
	}

	def "list"() {
		when:
		go "/alarm"

		then:
		$("tbody tr", 0).find("td", 0).text() == alarm1.description
		$("tbody tr", 0).find("td", 1).text() == "7:00 AM"
	}

	def "create"() {
		when:
		go "/alarm/create"
		$("form").description = "Gym"
		$("form").time_hour = "06"
		$("form").time_minute = "15"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Alarm \d+ created/

		and:
		def alarm2 = Alarm.findByDescription("Gym")
		alarm2.time == new LocalTime(6, 15)
	}

	@Unroll({"show formats LocalTime for $locale locale"})
	def "show"() {
		when:
		go "/alarm/show/$alarm1.id?lang=$locale"

		then:
		$("li.fieldcontain", 1).find(".property-value").text() == expectedValue

		where:
		locale    | expectedValue
		Locale.UK | "07:00"
		Locale.US | "7:00 AM"
	}

	def "edit"() {
		when:
		go "/alarm/edit/$alarm1.id"

		then:
		$("form").description == "Morning"
		$("form").time_hour == "07"
		$("form").time_minute == "00"
	}

	@Unroll({"list view is sorted after clicking the column header $x times"})
	def "list view is sortable"() {
		given:
		Alarm.build(description: "Gym", time: new LocalTime(6, 15))
		Alarm.build(description: "Lie In", time: new LocalTime(10, 30))

		when:
		go "/alarm/list"
		x.times {
			$("th a", text: "Time").click()
		}

		then:
		$("tbody tr")*.find("td", 0)*.text() == expected

		where:
		x | expected
		1 | ["Gym", "Morning", "Lie In"]
		2 | ["Lie In", "Morning", "Gym"]
	}
}