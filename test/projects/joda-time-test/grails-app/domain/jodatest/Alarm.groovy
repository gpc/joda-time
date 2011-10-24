package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentLocalTimeAsTimestamp
import org.joda.time.LocalTime

class Alarm {
	
	String description
	LocalTime time

	static constraints = {
		description blank: false
	}

	static mapping = {
		time type: PersistentLocalTimeAsTimestamp
	}
	
}