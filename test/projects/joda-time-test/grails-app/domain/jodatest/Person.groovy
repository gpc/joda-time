package jodatest

import org.joda.time.LocalDate
import org.joda.time.contrib.hibernate.PersistentLocalDate

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