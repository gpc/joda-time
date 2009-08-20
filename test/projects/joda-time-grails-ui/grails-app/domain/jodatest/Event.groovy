package jodatest

import org.joda.time.LocalDateTime
import org.joda.time.contrib.hibernate.PersistentLocalDateTime

class Event {

	String description
	LocalDateTime time

	static mapping = {
		time type: PersistentLocalDateTime
	}

}