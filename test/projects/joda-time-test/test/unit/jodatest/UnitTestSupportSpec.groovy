package jodatest

import grails.test.mixin.*
import spock.lang.*
import org.joda.time.*
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsApplication

@Mock(Person)
class UnitTestSupportSpec extends Specification {
	
	def setup() {
		new Person(name: "Alex", birthday: new LocalDate(2008, 10, 2)).save(failOnError: true)
		new Person(name: "Nicholas", birthday: new LocalDate(2010, 11, 14)).save(failOnError: true)
	}
	
	def "sanity check"() {
		expect:
		Person.findByName("Alex").birthday == new LocalDate(2008, 10, 2)
	}
	
	@Unroll
	def "can use dynamic finders on #type properties"() {
		expect:
		Person.findByBirthday(new LocalDate(2008, 10, 2)).name == "Alex"
	}
	
	def "can use criteria queries on #type properties"() {
		when:
		def results = Person.withCriteria {
			gt "birthday", new LocalDate(2010, 1, 1)
		}
		
		then:
		results.name == ["Nicholas"]
	}

	def "metadata for #type properties is correct"() {
		given:
		GrailsDomainClass dc = grailsApplication.getDomainClass(Person.name)

		expect:
		dc.getPersistentProperty("birthday") != null
		!dc.associationMap.containsKey("birthday")
	}
	
}