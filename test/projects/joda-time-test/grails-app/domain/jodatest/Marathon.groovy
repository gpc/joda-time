package jodatest

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

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