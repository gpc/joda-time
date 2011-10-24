package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentPeriodAsString
import org.joda.time.Period

class Song {

	String artist
	String title
	Period duration

	static constraints = {
		artist blank: false
		title blank: false
	}

	static mapping = {
		duration type: PersistentPeriodAsString
	}
	
}