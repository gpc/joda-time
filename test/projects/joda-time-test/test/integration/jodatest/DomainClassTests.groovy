package jodatest

import org.joda.time.*
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*

class DomainClassTests extends GroovyTestCase {

	def domainClass

	void setUp() {
		super.setUp()
		domainClass = ApplicationHolder.application.getArtefact("Domain", AuditedRecord.name)
	}

	void testDateTimePropertyIsNotConsideredAnAssociation() {
		def property = domainClass.getPropertyByName("dateCreated")
		assertFalse property.isAssociation()
	}

	void testDateTimePropertyTypeIsResolvedCorrectly() {
		def property = domainClass.getPropertyByName("dateCreated")
		assertThat "property type", property.type, equalTo(DateTime)
	}

	void testDateTimePropertyTypeIsResolvedCorrectlyViaMetaClass() {
		def property = AuditedRecord.metaClass.hasProperty(new AuditedRecord(), "dateCreated")
		assertThat "property type", property.type, equalTo(DateTime)
	}

}