package jodatest

import spock.lang.*
import grails.plugin.spock.*
import grails.datastore.test.*
import jodatest.*
import org.joda.time.*

@Mixin(DatastoreUnitTestMixin)
class SimpleDatastoreSpec extends UnitSpec {

	def "can use a dynamic finder on a joda-time property"() {
		given:
		new Person(name: "Rob", birthday: new LocalDate(1971, 11, 29)).save(failOnError: true)
		new Person(name: "Alex", birthday: new LocalDate(2008, 10, 2)).save(failOnError: true)
		new Person(name: "Nicholas", birthday: new LocalDate(2010, 11, 14)).save(failOnError: true)
		new Person(name: "Mystery", birthday: null).save(failOnError: true)
		
		expect:
		Person.findByBirthday(new LocalDate(2008, 10, 2)).name == "Alex"
		Person.findByBirthdayGreaterThan(new LocalDate(2000, 1, 1)).name == ["Alex", "Nicholas"]
		Person.findByBirthdayIsNull().name == "Mystery"
	}
	
	def setup() {
		mockDomain Person
	}

	def cleanup() {
		disconnect()
	}

}
