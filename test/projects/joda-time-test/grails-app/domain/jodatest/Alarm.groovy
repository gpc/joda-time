package jodatest

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class Alarm {
	
	String description
	LocalTime time

	static constraints = {
		description blank: false
	}

	static mapping = {
		time type: PersistentLocalTimeAsTime
	}
	
}