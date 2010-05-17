package jodatest

import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*

class AutoTimestampingTests extends GroovyTestCase {

	void testDateCreatedWorks() {
		def record = new AuditedRecord(data: "blah")
		assert record.save(flush: true)

		assertThat "date created", record.dateCreated, not(nullValue())
		assertThat "last updated", record.lastUpdated, not(nullValue())
		assertThat "timestamp values", record.dateCreated, equals(record.lastUpdated)
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