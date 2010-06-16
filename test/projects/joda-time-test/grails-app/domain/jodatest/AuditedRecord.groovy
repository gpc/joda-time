package jodatest

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class AuditedRecord {

	String data
	DateTime dateCreated
	DateTime lastUpdated

	static constraints = {
		data: blank: false
	}

	static mapping = {
		dateCreated type: PersistentDateTime
		lastUpdated type: PersistentDateTime
	}

}