package jodatest

import grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller
import grails.test.mixin.Mock
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.joda.time.*
import spock.lang.*

@Mock([Person, Marathon, City, AuditedRecord])
@Unroll
class UnitTestSupportSpec extends Specification {

	void setupSpec() {
		SimpleMapJodaTimeMarshaller.initialize()
	}

	void setup() {
		new Person(name: "Alex", birthday: new LocalDate(2008, 10, 2)).save(failOnError: true)
		new Person(name: "Nicholas", birthday: new LocalDate(2010, 11, 14)).save(failOnError: true)
	}
	
	@Issue("http://jira.grails.org/browse/GPJODATIME-19")
	void "can re-save instances"() {
		given:
		def record = new AuditedRecord(data: "foo").save(failOnError: true)
		
		expect:
		record.save(failOnError: true)
	}

	void "can read a LocalDate property of a domain instance retrieved from the simple datastore"() {
		expect:
		Person.findByName("Alex").birthday == new LocalDate(2008, 10, 2)
	}

	void "can use the dynamic query `#queryMethod` on a LocalDate property"() {
		expect:
		Person."$queryMethod"(argument).name == expected

		where:
		queryMethod                    | argument                   | expected
		"findByBirthday"               | new LocalDate(2008, 10, 2) | "Alex"
		"findByBirthdayNotEquals"      | new LocalDate(2008, 10, 2) | "Nicholas"
		"findByBirthdayGreaterThan"    | new LocalDate(2008, 10, 2) | "Nicholas"
		"findAllByBirthdayGreaterThan" | new LocalDate(2008, 10, 1) | ["Alex", "Nicholas"]
		"findAllByBirthdayGreaterThanEquals" | new LocalDate(2008, 10, 2) | ["Alex", "Nicholas"]
	}

	void "can use the dynamic query `#queryMethod` on a Duration property"() {
		given:
		new Marathon(runner: "Haile Gebrselassie", time: new Period(2, 3, 59, 0).toStandardDuration()).save(failOnError: true)
		new Marathon(runner: "Samuel Wanjiru", time: new Period(2, 5, 10, 0).toStandardDuration()).save(failOnError: true)

		expect:
		Marathon."$queryMethod"(argument).runner == expected

		where:
		queryMethod             | argument                                     | expected
		"findByTime"            | new Period(2, 3, 59, 0).toStandardDuration() | "Haile Gebrselassie"
		"findAllByTimeLessThan" | new Period(2, 5, 10, 0).toStandardDuration() | ["Haile Gebrselassie"]
	}

	void "can use the dynamic query `#queryMethod` on a DateTimeZone property"() {
		given:
		new City(name: "London", timeZone: DateTimeZone.forID("Europe/London")).save(failOnError: true)
		new City(name: "Vancouver", timeZone: DateTimeZone.forID("America/Vancouver")).save(failOnError: true)

		expect:
		City."$queryMethod"(argument).name == expected

		where:
		queryMethod                  | argument                            | expected
		"findByTimeZone"             | DateTimeZone.forID("Europe/London") | "London"
		"findAllByTimeZoneNotEquals" | DateTimeZone.forID("Europe/London") | ["Vancouver"]
	}

	void "cannot use some operators on non-Comparable types"() {
		given:
		new City(name: "London", timeZone: DateTimeZone.forID("Europe/London")).save(failOnError: true)
		new City(name: "Vancouver", timeZone: DateTimeZone.forID("America/Vancouver")).save(failOnError: true)

		when:
		City.findByTimeZoneLessThan(DateTimeZone.forOffsetHours(0))

		then:
		thrown RuntimeException
	}

	void "can use the `#operator` operator on a LocalDate property in a criteria query"() {
		when:
		def results = Person.withCriteria {
			"$operator" "birthday", value
		}

		then:
		results.name == expected

		where:
		operator | value                       | expected
		"gt"     | new LocalDate(2008, 10, 2)  | ["Nicholas"]
		"ge"     | new LocalDate(2008, 10, 2)  | ["Alex", "Nicholas"]
		"eq"     | new LocalDate(2008, 10, 2)  | ["Alex"]
		"lt"     | new LocalDate(2010, 11, 14) | ["Alex"]
		"le"     | new LocalDate(2010, 11, 14) | ["Alex", "Nicholas"]
		"ne"     | new LocalDate(2010, 11, 14) | ["Alex"]
	}

	void "can use the `between` operator on a LocalDate property in a criteria query"() {
		when:
		def results = Person.withCriteria {
			between "birthday", lowerBound, upperBound
		}

		then:
		results.name == expected

		where:
		lowerBound                | upperBound                | expected
		new LocalDate(2008, 1, 1) | new LocalDate(2010, 1, 1) | ["Alex"]
		new LocalDate(2010, 1, 1) | new LocalDate(2011, 1, 1) | ["Nicholas"]
		new LocalDate(2008, 1, 1) | new LocalDate(2011, 1, 1) | ["Alex", "Nicholas"]
	}

	void "empty results are handled correctly"() {
		when:
		def results = Person.withCriteria {
			isNull "birthday"
		}

		then:
		results == []
	}

	void "can order #direction ending by a LocalDate property in a criteria query"() {
		when:
		def results = Person.withCriteria {
			order "birthday", direction
		}

		then:
		results.name == expected

		where:
		direction | expected
		"asc"     | ["Alex", "Nicholas"]
		"desc"    | ["Nicholas", "Alex"]
	}

	void "can use `#projection` projection on a LocalDate property in a criteria query"() {
		when:
		def results = Person.withCriteria {
			projections {
				"$projection" "birthday"
			}
		}

		then:
		results[0] == expected

		where:
		projection | expected
		"max"      | new LocalDate(2010, 11, 14)
		"min"      | new LocalDate(2008, 10, 2)
	}

	void "cannot use `#projection` projection on a LocalDate property in a criteria query"() {
		when:
		Person.withCriteria {
			projections {
				"$projection" "birthday"
			}
		}

		then:
		thrown MissingMethodException

		where:
		projection << ["avg", "sum"]
	}

	void "metadata for LocalDate properties is correct"() {
		given:
		GrailsDomainClass dc = grailsApplication.getDomainClass(Person.name)
		def prop = dc.getPersistentProperty("birthday")

		expect:
		dc.constrainedProperties.containsKey("birthday")
		!dc.associationMap.containsKey("birthday")

		and:
		prop != null
		!prop.association
		!prop.embedded
		!prop.hasOne
		!prop.manyToMany
		!prop.manyToOne
		!prop.oneToMany
		!prop.oneToOne
		prop.type == LocalDate
	}

}
