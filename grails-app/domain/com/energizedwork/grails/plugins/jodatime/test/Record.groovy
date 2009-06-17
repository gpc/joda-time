package com.energizedwork.grails.plugins.jodatime.test

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class Record {

	String data
	DateTime dateCreated
	DateTime lastUpdated

	static mapping = {
		dateCreated type: PersistentDateTime
		lastUpdated type: PersistentDateTime
	}
}