package jodatest

import org.jadira.usertype.dateandtime.joda.PersistentDateTimeWithZone
import org.joda.time.DateTime

class ZonedRecord {

	String data
	DateTime dateCreated

	static constraints = {
		data blank: false
	}

	static mapping = {
		dateCreated type: PersistentDateTimeWithZone, {
			column name: "date_created_dt"
			column name: "date_created_tz"
		}
	}

}