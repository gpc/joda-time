package jodatest

import org.joda.time.Period
import org.joda.time.contrib.hibernate.PersistentPeriod

class Song {

	String artist
	String title
	Period duration

	static constraints = {
		artist blank: false
		title blank: false
	}

	static mapping = {
		duration type: PersistentPeriod
	}
	
}