package jodatest

import org.joda.time.LocalDate
import spock.lang.Issue

@Issue('http://jira.grails.org/browse/GPJODATIME-21')
class NestedPropertyBindingSpec extends GebSpec {

	def "can bind with a structured editor to a property of an embedded type"() {
		given:
		go "/employee/create"

		when:
		$("form").name = 'Rob'
		$("form").'job.title' = 'Nautical redistribution specialist'
		$("form").'job.startDate_year' = '2011'
		$("form").'job.startDate_month' = '3'
		$("form").'job.startDate_day' = '15'
		$("form").create().click()

		then:
		$(".message").text() ==~ /Employee \d+ created/

		and:
		def employee = Employee.findByName('Rob')
		employee.job.startDate == new LocalDate(2011, 3, 15)
	}

}
