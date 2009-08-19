package jodatest

class AutoTimestampingTests extends GroovyTestCase {

	void testDateCreatedWorks() {
		def record = new AuditedRecord(data: "blah")
		assert record.save(flush: true)

		assertNotNull record.dateCreated
		assertNotNull record.lastUpdated
		assertEquals record.dateCreated, record.lastUpdated
	}

	void testLastUpdatedWorks() {
		def record = new AuditedRecord(data: "blah")
		assert record.save(flush: true)

		def lastUpdatedBefore = record.lastUpdated

		record.data = "foo"
		assert record.save(flush: true)

		assertTrue lastUpdatedBefore < record.lastUpdated
	}

}