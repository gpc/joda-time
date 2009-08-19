package jodatest

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

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