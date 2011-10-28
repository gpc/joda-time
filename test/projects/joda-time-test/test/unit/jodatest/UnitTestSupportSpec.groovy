package jodatest

import com.energizedwork.grails.plugins.jodatime.simpledatastore.JodaTimeUnitTestSupport
import grails.test.mixin.Mock
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.joda.time.LocalDate
import spock.lang.*

@Mock(Person)
class UnitTestSupportSpec extends Specification {

	def setupSpec() {
		JodaTimeUnitTestSupport.registerJodaTimePropertyTypes()
	}

	def setup() {
		new Person(name: "Alex", birthday: new LocalDate(2008, 10, 2)).save(failOnError: true)
		new Person(name: "Nicholas", birthday: new LocalDate(2010, 11, 14)).save(failOnError: true)
	}

	def "can read a LocalDate property of a domain instance retrieved from the simple datastore"() {
		expect:
		Person.findByName("Alex").birthday == new LocalDate(2008, 10, 2)
	}

	@Unroll({"can us the dynamic query `$queryMethod` on a LocalDate property"})
	def "can use dynamic finders on LocalDate properties"() {
		expect:
		Person."$queryMethod"(argument).name == expected

		where:
		queryMethod                    | argument                   | expected
		"findByBirthday"               | new LocalDate(2008, 10, 2) | "Alex"
		"findByBirthdayNotEquals"      | new LocalDate(2008, 10, 2) | "Nicholas"
		"findByBirthdayGreaterThan"    | new LocalDate(2008, 10, 2) | "Nicholas"
		"findAllByBirthdayGreaterThan" | new LocalDate(2008, 10, 1) | ["Alex", "Nicholas"]
	}

	@Unroll({"can use the `$operator` operator on a LocalDate property in a criteria query"})
	def "can use criteria queries on LocalDate properties"() {
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

	@Unroll
	def "can use the `between` operator on a LocalDate property in a criteria query"() {
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

	def "empty results are handled correctly"() {
		when:
		def results = Person.withCriteria {
			isNull "birthday"
		}

		then:
		results == []
	}

	@Unroll({"can order ${direction}ending by a LocalDate property in a criteria query"})
	def "can order by a LocalDate property in a criteria query"() {
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

	@Unroll({"can use `$projection` projection on a LocalDate property in a criteria query"})
	def "can use projections on a LocalDate property in a criteria query"() {
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

	@Unroll({"cannot use `$projection` projection on a LocalDate property in a criteria query"})
	def "invalid projections on a LocalDate property in a criteria query"() {
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

	def "metadata for LocalDate properties is correct"() {
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
