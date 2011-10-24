package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString
import org.joda.time.DateTimeZone

class City {

	String name
	DateTimeZone timeZone

	static constraints = {
		name blank: false, unique: true
	}

	static mapping = {
		timeZone type: PersistentDateTimeZoneAsString
	}

}