package jodatest

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class AutoTimestampingTests extends GroovyTestCase {

	void testDateCreatedWorks() {
		def record = new AuditedRecord(data: "blah")
		assert record.save(flush: true)

		assertThat "date created", record.dateCreated, notNullValue()
		assertThat "last updated", record.lastUpdated, notNullValue()
		assertThat "timestamp values", record.dateCreated, equalTo(record.lastUpdated)
	}

	void testLastUpdatedWorks() {
		def record = new AuditedRecord(data: "blah")
		assert record.save(flush: true)

		def lastUpdatedBefore = record.lastUpdated

		record.data = "foo"
		assert record.save(flush: true)

		assertThat "last updated", lastUpdatedBefore, lessThan(record.lastUpdated)
	}

}