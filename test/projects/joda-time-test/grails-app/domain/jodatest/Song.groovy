package jodatest

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class Song {

	String artist
	String title
	Period duration

	static constraints = {
		artist blank: false
		title blank: false
	}

	static mapping = {
		duration: type: PersistentPeriod
	}
	
}