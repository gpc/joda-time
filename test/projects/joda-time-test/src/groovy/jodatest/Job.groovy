package jodatest

import org.joda.time.LocalDate

class Job {

	String title
	LocalDate startDate
	Date endDate

	static constraints = {
		title blank: false
		startDate()
		endDate nullable: true
	}
}
