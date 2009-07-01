package com.energizedwork.grails.plugins.jodatime.test

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class Person {

	String name
	LocalDate birthday
	LocalTime alarmClock
	DateTime dateCreated

    static mapping = {
		birthday type: PersistentLocalDate
		alarmClock type: PersistentLocalTimeAsTime
		dateCreated type: PersistentDateTimeTZ, {
			column name: "date_created_dt"
			column name: "date_created_tz"
		}
    }

	static constraints = {
		name unique: true
	}
}
