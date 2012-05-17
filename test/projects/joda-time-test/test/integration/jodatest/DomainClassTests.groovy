package jodatest

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.joda.time.DateTime
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.CoreMatchers.equalTo

class DomainClassTests extends GroovyTestCase {

	def domainClass

	void setUp() {
		super.setUp()
		domainClass = ApplicationHolder.application.getArtefact("Domain", AuditedRecord.name)
	}

	void testDateTimePropertyIsNotConsideredAnAssociation() {
		def property = domainClass.getPropertyByName("dateCreated")
		assertFalse "property is association", property.isAssociation()
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