package jodatest

import org.joda.time.LocalTime
import org.joda.time.contrib.hibernate.PersistentLocalTimeAsTime

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