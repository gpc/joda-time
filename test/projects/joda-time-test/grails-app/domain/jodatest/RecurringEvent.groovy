package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentDateTime
import org.joda.time.DateTime

class RecurringEvent {

	String description
	static hasMany = [occurrences: DateTime]

	static constraints = {
		description blank: false, unique: true
	}

	static mapping = {
		occurrences type: PersistentDateTime
	}

}
