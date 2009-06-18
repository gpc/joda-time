package com.energizedwork.grails.plugins.jodatime

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.joda.time.DateTime
import com.energizedwork.grails.plugins.jodatime.test.Person

class DomainClassTests extends GroovyTestCase {

	GrailsDomainClass personClass

	void setUp() {
		super.setUp()

		personClass = ApplicationHolder.application.getArtefact("Domain", "$Person.name")
	}

	void testDateTimePropertyIsNotConsideredAnAssociation() {
		def property = personClass.getPropertyByName("dateCreated")
		assertFalse property.isAssociation()
	}

	void testDateTimePropertyTypeIsResolvedCorrectly() {
		def property = personClass.getPropertyByName("dateCreated")
		assertEquals DateTime, property.type
	}

	void testDateTimePropertyTypeIsResolvedCorrectlyViaMetaClass() {
		def property = Person.metaClass.hasProperty(new Person(), "dateCreated")
		assertEquals DateTime, property.type
	}

}