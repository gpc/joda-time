package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.test.Person
import org.joda.time.DateTimeZone

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

		Person.withSession {session ->
			// if we save a new person the zone of the DateTimeTZ field should be correct
			def person = Person.build(name: "Alex")
			assertEquals tz, person.dateCreated.zone

			// if we re-load from the db the zone should be correct
			session.clear()
			assertEquals tz, Person.findByName("Alex").dateCreated.zone

			// if we re-load from the db when in a different zone, the zone on the record should be correct
			session.clear()
			DateTimeZone.default = DateTimeZone.forOffsetHours(-5)
			assertEquals tz, Person.findByName("Alex").dateCreated.zone
		}
	}

	void testQueryingWithEqOnMultiColumnField() {
		def dateCreated
		Person.withSession {session ->
			def person=Person.build(name: "Alex")
			dateCreated = person.dateCreated
			session.clear()
		}

		def person = Person.withCriteria(uniqueResult: true) {
			eq("dateCreated", dateCreated)
		}

		assertEquals dateCreated, person.dateCreated
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