package jodatest

import org.joda.time.DateTimeZone

class City {

	String name
	DateTimeZone timeZone

	static constraints = {
		name blank: false, unique: true
	}

}