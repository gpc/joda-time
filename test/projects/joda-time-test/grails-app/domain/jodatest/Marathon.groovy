package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentDurationAsString
import org.joda.time.Duration

class Marathon {

	String runner
	Duration time

	static constraints = {
		runner blank: false
	}

	static mapping = {
		time type: PersistentDurationAsString
	}
}