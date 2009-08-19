package jodatest

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

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