package jodatest

import grails.test.mixin.Mock
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.simple.query.SimpleMapResultList
import org.joda.time.LocalDate
import org.grails.datastore.mapping.model.*
import spock.lang.*

@Mock(Person)
class UnitTestSupportSpec extends Specification {

	def setupSpec() {
		MappingFactory.registerCustomType(new SimpleMapLocalDateMarshaller())
	}

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

	def "can use the `between` operator on a LocalDate property in a criteria query"() {
		when:
		def results = Person.withCriteria {
			between "birthday", new LocalDate(2008, 1, 1), new LocalDate(2010, 1, 1)
		}

		then:
		results.name == ["Alex"]
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

	def "metadata for #type properties is correct"() {
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

	def "persistent entity looks right"() {
		given:
		def mappingContext = DomainClassUnitTestMixin.simpleDatastore.mappingContext
		def entity = mappingContext.getPersistentEntity(Person.name)
		def prop = entity.getPropertyByName("birthday")

		expect:
		prop != null
		prop.type == LocalDate
	}

}

class SimpleMapLocalDateMarshaller extends AbstractMappingAwareCustomTypeMarshaller<LocalDate, Map, SimpleMapResultList> {

	SimpleMapLocalDateMarshaller() {
		super(LocalDate)
	}

	@Override
	protected Object writeInternal(PersistentProperty property, String key, LocalDate value, Map nativeTarget) {
		nativeTarget[key] = value
	}

	@Override
	protected LocalDate readInternal(PersistentProperty property, String key, Map nativeSource) {
		nativeSource[key]
	}

	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, SimpleMapResultList nativeQuery) {
		def op = criterion.getClass()
		switch (op) {
			case Query.Equals:
			case Query.NotEquals:
			case Query.GreaterThan:
			case Query.GreaterThanEquals:
			case Query.LessThan:
			case Query.LessThanEquals:
			case Query.Between:
				nativeQuery.results << nativeQuery.query.handlers[op].call(criterion, property)
				break
			default:
				throw new RuntimeException("unsupported query type $criterion for property $property")
		}
	}

}
