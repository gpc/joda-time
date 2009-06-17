package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.test.Record

class AutoTimestampingTests extends GroovyTestCase {

	def record

	void setUp() {
		super.setUp()

		record = new Record(data: "blah")
		assert record.save()
	}

	void testDateCreatedWorks() {
		assertNotNull record.dateCreated
		assertNotNull record.lastUpdated
		assertEquals record.dateCreated, record.lastUpdated
	}

	void testLastUpdatedWorks() {
		def lastUpdatedBefore = record.lastUpdated

		record.data = "foo"
		assert record.save(flush: true)

		assertTrue lastUpdatedBefore < record.lastUpdated
	}

}