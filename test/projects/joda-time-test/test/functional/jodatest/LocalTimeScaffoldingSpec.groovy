package jodatest

import org.joda.time.LocalTime
import spock.lang.Unroll

@Unroll
class LocalTimeScaffoldingSpec extends GebSpec {
	def alarm1
    def grailsApplication

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
		go "/alarm/index"

		then:
		$("tbody tr", 0).find("td", 0).text() == alarm1.description
		$("tbody tr", 0).find("td", 1).text() == "07:00:00.000"
	}

	def "create"() {
		when:
		go "/alarm/create"
		$("form").description = "Gym"
		$("form").time = "06:15"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Alarm \d+ created/

		and:
		def alarm2 = Alarm.findByDescription("Gym")
		alarm2.time == new LocalTime(6, 15)
	}

	def "show formats LocalTime for #locale locale"() {
        given:
        grailsApplication.config.jodatime.format.html5 = false

		when:
		go "/alarm/show/$alarm1.id?lang=$locale"

		then:
		$("li.fieldcontain", 1).find(".property-value").text() == expectedValue

        cleanup:
        grailsApplication.config.jodatime.format.html5 = true

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
		$("form").time == "07:00:00.000"
	}

	def "list view is sorted after clicking the column header #x times"() {
		given:
		Alarm.build(description: "Gym", time: new LocalTime(6, 15))
		Alarm.build(description: "Lie In", time: new LocalTime(10, 30))

		when:
		go "/alarm/index"
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
