package jodatest

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTimeTZ

class ZonedRecord {

	String data
	DateTime dateCreated

	static constraints = {
		data blank: false
	}

	static mapping = {
		dateCreated type: PersistentDateTimeTZ, {
			column name: "date_created_dt"
			column name: "date_created_tz"
		}
	}

}