package jodatest

import spock.lang.*
import grails.plugin.geb.*
import org.joda.time.DateTimeZone
import static javax.servlet.http.HttpServletResponse.SC_OK

class DateTimeZoneScaffoldingSpec extends GebSpec {

	@Shared def london

	def setupSpec() {
		london = City.build(name: "London", timeZone: DateTimeZone.forID("Europe/London"))
	}

	def cleanupSpec() {
		City.list()*.delete(flush: true)
	}

	def "list"() {
		when:
		go "/city"

		then:
		$("tbody tr", 0).find("td", 0).text() == "$london.id"
		$("tbody tr", 0).find("td", 1).text() == london.name
		$("tbody tr", 0).find("td", 2).text() == "$london.timeZone"
	}

	def "create"() {
		when:
		go "/city/create"
		$("form").name = "Vancouver"
		$("form").timeZone = "America/Vancouver"
		$("input.save").click()

		then:
		$(".message").text() ==~ /City \d+ created/

		and:
		def jerome = City.findByName("Vancouver")
		jerome.timeZone == DateTimeZone.forID("America/Vancouver")
	}

	def "show"() {
		when:
		go "/city/show/$london.id"

		then:
		$("tr", 0).find("td.value").text() == "$london.id"
		$("tr", 1).find("td.value").text() == "London"
		$("tr", 2).find("td.value").text() == "Europe/London"
	}

	def "edit"() {
		when:
		go "/city/edit/$london.id"

		then:
		$("form").name == "London"
		$("form").timeZone == "Europe/London"
	}
}