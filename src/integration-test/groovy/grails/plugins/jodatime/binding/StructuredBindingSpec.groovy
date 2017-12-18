package grails.plugins.jodatime.binding

import grails.databinding.BindUsing
import grails.databinding.BindingFormat
import grails.databinding.SimpleMapDataBindingSource
import grails.persistence.Entity
import grails.testing.mixin.integration.Integration
import grails.validation.Validateable
import grails.web.databinding.GrailsWebDataBinder
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Ignore
import spock.lang.Issue
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

@Integration
class StructuredBindingSpec extends Specification {

	private HttpServletRequest request = new MockHttpServletRequest()
	private GrailsParameterMap params = new GrailsParameterMap(request)

	@Autowired
	GrailsWebDataBinder grailsWebDataBinder

	void 'can bind to a top level field'() {
		given:
		params.putAll([
				name: 'Alex',
				birthday: 'struct',
				birthday_day: '02',
				birthday_month: '10',
				birthday_year: '2008'
		])

		and:
		def person = new Person()

		when:
		grailsWebDataBinder.bind person, params as SimpleMapDataBindingSource

		then:
		person.name == 'Alex'
		person.birthday == new LocalDate(2008, 10, 2)
	}

	void 'can bind to an associated field'() {
		given:
		params.putAll([
				name: 'Rob',
				'child.name': 'Alex',
				'child.birthday': 'struct',
				'child.birthday_day': '02',
				'child.birthday_month': '10',
				'child.birthday_year': '2008'
		])

		and:
		def person = new Parent(child: new Person()) // TODO: Grails, why you are make me do this?

		when:
		grailsWebDataBinder.bind person, params as SimpleMapDataBindingSource

		then:
		!person.errors.hasErrors()

		and:
		person.name == 'Rob'
		person.child.name == 'Alex'
		person.child.birthday == new LocalDate(2008, 10, 2)
	}

	@Issue('http://jira.grails.org/browse/GPJODATIME-21')
	void 'can bind to an embedded field'() {
		given:
		params.putAll([
				name: 'Rob',
				'boss.name': 'Alex',
				'boss.birthday': 'struct',
				'boss.birthday_day': '02',
				'boss.birthday_month': '10',
				'boss.birthday_year': '2008'
		])

		and:
		def person = new Employee()

		when:
//		def binder = GrailsDataBinder.createBinder(person, '', request)
//		binder.bind(params)
		grailsWebDataBinder.bind person, params as SimpleMapDataBindingSource

		then:
		!person.errors.hasErrors()

		and:
		person.name == 'Rob'
		person.boss.name == 'Alex'
		person.boss.birthday == new LocalDate(2008, 10, 2)
	}

	@Ignore
	@Issue('http://jira.grails.org/browse/GPJODATIME-28')
	void 'can bind to a collection field'() {
		given:
		params.putAll([
				name: 'Groovy & Grails User Group',
				'dates[0]': 'struct',
				'dates[0]_day': '09',
				'dates[0]_month': '11',
				'dates[0]_year': '2012',
				'dates[1]': 'struct',
				'dates[1]_day': '13',
				'dates[1]_month': '12',
				'dates[1]_year': '2012',
		])

		and:
		def event = new RecurringEvent()

		when:
		// Due to a bug in Grails this does not work even with regular Date
		// Bug reported here: https://github.com/grails/grails-core/issues/10894
		SimpleMapDataBindingSource dataBindingSource = params
		grailsWebDataBinder.bind(event, dataBindingSource)

		then:
		!event.errors.hasErrors()

		and:
		event.name == params.name
		event.dates == [new LocalDate(2012, 11, 9), new LocalDate(2012, 12, 13)]
	}

	void 'can bind to a command object'() {
		given:
		params.putAll([
			name       : 'GR8Conf EU 2018',
			start      : '30-05-2018',
			end        : '2018-06-01'
		])

		and:
		def eventCommand = new EventCommand()

		when:
		grailsWebDataBinder.bind eventCommand, params as SimpleMapDataBindingSource

		then:
		eventCommand.name == 'GR8Conf EU 2018'
		eventCommand.start == new LocalDate(2018, 05, 30)
		eventCommand.end == new LocalDate(2018, 06, 1)
	}


}

@Entity
class Person {
	String name
	LocalDate birthday
}

@Entity
class Parent extends Person {
	Person child
}

@Entity
class Employee extends Person {
	Person boss
	static embedded = ['boss']
}

@Entity
class RecurringEvent {
	String name
	List<LocalDate> dates
	static hasMany = [dates: LocalDate]
}

class EventCommand implements Validateable {
	String name
	@BindingFormat('dd-MM-yyyy')
	LocalDate start
	@BindUsing({ obj, source ->
		source['end'] ? LocalDate.parse(source['end'].toString()) : null
	})
	LocalDate end
}