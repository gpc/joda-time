package jodatest

import org.joda.time.*
import org.codehaus.groovy.grails.commons.ApplicationHolder

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
		assertEquals DateTime, property.type
	}

	void testDateTimePropertyTypeIsResolvedCorrectlyViaMetaClass() {
		def property = AuditedRecord.metaClass.hasProperty(new AuditedRecord(), "dateCreated")
		assertEquals DateTime, property.type
	}

}