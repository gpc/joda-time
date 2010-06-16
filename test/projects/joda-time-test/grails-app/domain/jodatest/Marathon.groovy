package jodatest

import org.joda.time.Duration
import org.joda.time.contrib.hibernate.PersistentDuration

class Marathon {

	String runner
	Duration time

	static constraints = {
		runner blank: false
	}

	static mapping = {
		time type: PersistentDuration
	}
}