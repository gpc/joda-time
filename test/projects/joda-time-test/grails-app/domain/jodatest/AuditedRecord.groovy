package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentDateTime
import org.joda.time.DateTime

class AuditedRecord {

	String data
	DateTime dateCreated
	DateTime lastUpdated

	static constraints = {
		data: blank: false
		dateCreated editable: false
		lastUpdated editable: false
	}

	static mapping = {
		dateCreated type: PersistentDateTime
		lastUpdated type: PersistentDateTime
	}

}