package jodatest

import org.joda.time.LocalDate

class Job {

	String title
	LocalDate startDate
	LocalDate endDate

	static constraints = {
		title blank: false
		endDate nullable: true
	}
}
