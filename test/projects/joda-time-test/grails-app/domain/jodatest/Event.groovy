package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentInstantAsMillisLong
import org.joda.time.Instant

class Event {
	
	String description
	Instant occurred

	static constraints = {
		description blank: false, unique: true
	}
	
	static mapping = {
		occurred type: PersistentInstantAsMillisLong
	}
}
