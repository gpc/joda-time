package jodatest

import org.joda.time.DateTimeZone
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.CoreMatchers.equalTo

class PersistenceTests extends GroovyTestCase {

	def originalZone

	void setUp() {
		super.setUp()
		originalZone = DateTimeZone.default
	}

	void tearDown() {
		super.tearDown()
		DateTimeZone.default = originalZone
	}

	void testPersistenceOfDateTimeWithTZ() {
		def tz = DateTimeZone.forOffsetHours(2)
		DateTimeZone.default = tz

		ZonedRecord.withSession {session ->
			// if we save a new person the zone of the DateTimeTZ field should be correct
			def record = new ZonedRecord(data: "foo")
			assert record.save(flush: true)
			assertThat "time zone of date created", record.dateCreated.zone, equalTo(tz)

			// if we re-load from the db the zone should be correct
			session.clear()
			assertThat "time zone of date created", ZonedRecord.findByData("foo").dateCreated.zone, equalTo(tz)

			// if we re-load from the db when in a different zone, the zone on the record should be correct
			session.clear()
			DateTimeZone.default = DateTimeZone.forOffsetHours(-5)
			assertThat "time zone of date created", ZonedRecord.findByData("foo").dateCreated.zone, equalTo(tz)
		}
	}

	void testQueryingWithEqOnMultiColumnField() {
		def dateCreated
		ZonedRecord.withSession {session ->
			def record = new ZonedRecord(data: "foo")
			assert record.save(flush: true)
			dateCreated = record.dateCreated
			session.clear()
		}

		def record = ZonedRecord.withCriteria(uniqueResult: true) {
			eq("dateCreated", dateCreated)
		}

		assertThat "date crearted", record.dateCreated, equalTo(dateCreated)
	}

//	void testQueryingWithBetweenOnMultiColumnField() {
//		def dateCreated
//		Person.withSession {session ->
//			def person=Person.build(name: "Alex")
//			dateCreated = person.dateCreated
//			session.clear()
//		}
//
//		def person = Person.withCriteria(uniqueResult: true) {
//			between("dateCreated", dateCreated.minusHours(1), dateCreated.plusHours(1))
//		}
//
//		assertEquals dateCreated, person.dateCreated
//	}
//
//	void testQueryingByProjectionOnMultiColumnField() {
//		def dateCreated
//		Person.withSession {session ->
//			def person=Person.build(name: "Alex")
//			dateCreated = person.dateCreated
//			session.clear()
//		}
//
//		def maxDateCreated = Person.withCriteria(uniqueResult: true) {
//			projections {
//				max "dateCreated"
//			}
//		}
//
//		assertEquals dateCreated, maxDateCreated
//	}

}