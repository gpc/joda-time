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

	def setup() {
		MappingFactory.registerCustomType(new SimpleMapLocalDateMarshaller())

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
			gt "birthday", new LocalDate(2008, 1, 1)
		}

		then:
		results.name == ["Alex", "Nicholas"]
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
		switch (criterion) {
			case Query.Equals:
				nativeQuery.results << nativeQuery.query.handlers[Query.Equals].call(criterion, property, null)
				break
			case Query.GreaterThan:
				nativeQuery.results << nativeQuery.query.handlers[Query.GreaterThan].call(criterion, property, null)
				break
			default:
				throw new RuntimeException("unsupported query type $criterion for property $property")
		}
	}

}
