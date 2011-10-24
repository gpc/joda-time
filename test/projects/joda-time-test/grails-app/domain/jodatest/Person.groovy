package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentLocalDate
import org.joda.time.LocalDate

class Person {

	String name
	LocalDate birthday

	static constraints = {
		name blank: false, unique: true
	}

	static mapping = {
		birthday type: PersistentLocalDate
	}

}