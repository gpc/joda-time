package jodatest

import grails.datastore.test.DatastoreUnitTestMixin
import grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller
import org.joda.time.LocalDate
import spock.lang.Specification

@Mixin(DatastoreUnitTestMixin)
class SimpleDatastoreSpec extends Specification {

    def "can use a dynamic finder on a joda-time property"() {
        given:
        new Person(id: 1, name: "Rob", birthday: new LocalDate(1971, 11, 29)).save(failOnError: true, flush: true)
        new Person(id: 2, name: "Alex", birthday: new LocalDate(2008, 10, 2)).save(failOnError: true, flush: true)
        new Person(id: 3, name: "Nicholas", birthday: new LocalDate(2010, 11, 14)).save(failOnError: true, flush: true)
        new Person(id: 4, name: "Mystery", birthday: null).save(failOnError: true, flush: true)

        expect:
        Person.count() == 4
        Person.findByBirthday(new LocalDate(2008, 10, 2)).name == "Alex"
        Person.findAllByBirthdayGreaterThan(new LocalDate(2000, 12, 31)).name == ["Alex", "Nicholas"]
        Person.findByBirthdayIsNull().name == "Mystery"
    }

    def setupSpec() {
        // TODO: this shouldn't be necessary as doing it in _Events should be enough
        SimpleMapJodaTimeMarshaller.initialize()
    }

    def setup() {
        mockDomain Person
    }

    def cleanup() {
        disconnect()
    }

}
