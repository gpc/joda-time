package jodatest

import grails.datastore.test.DatastoreUnitTestMixin
import grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller
import org.joda.time.LocalDate
import spock.lang.Specification

@Mixin(DatastoreUnitTestMixin)
class SimpleDatastoreSpec extends Specification {

	def "can use a dynamic finder on a joda-time property"() {
		expect:
        Person.count() == 4
		Person.findByBirthday(new LocalDate(2008, 10, 2)).name == "Alex"
		Person.findByBirthdayGreaterThan(new LocalDate(2000, 12, 31)).name == ["Alex", "Nicholas"]
		Person.findByBirthdayIsNull().name == "Mystery"
	}

    def setupSpec() {
        SimpleMapJodaTimeMarshaller.initialize()
    }
	
	def setup() {
        def people = []
        people << new Person(name: "Rob", birthday: new LocalDate(1971, 11, 29))
        people << new Person(name: "Alex", birthday: new LocalDate(2008, 10, 2))
        people << new Person(name: "Nicholas", birthday: new LocalDate(2010, 11, 14))
        people << new Person(name: "Mystery", birthday: null)
		mockDomain Person
	}

	def cleanup() {
		disconnect()
	}

}
