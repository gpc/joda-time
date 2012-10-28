package jodatest

import grails.plugin.spock.IntegrationSpec

class AutoTimestampingSpec extends IntegrationSpec {

	void 'creation timestamp works'() {
		given:
		def record = new AuditedRecord(data: 'blah')
		record.save(flush: true, failOnError: true)

		expect:
		record.dateCreated
		record.lastUpdated
		record.dateCreated==record.lastUpdated
	}

	void 'update timestamp works'() {
		given:
		def record = new AuditedRecord(data: 'blah')
		record.save(flush: true, failOnError: true)

		when:
		record.data = "foo"
		assert record.save(flush: true)

		then:
		record.lastUpdated > old(record.lastUpdated)
	}

}