package jodatest

import grails.plugin.spock.IntegrationSpec
import org.joda.time.DateTime

class DomainClassSpec extends IntegrationSpec {

	def grailsApplication
	def domainClass

	void setup() {
		domainClass = grailsApplication.getArtefact("Domain", AuditedRecord.name)
	}

	void 'DateTime property is not considered an association'() {
		given:
		def property = domainClass.getPropertyByName("dateCreated")

		expect:
		!property.isAssociation()
	}

	void 'DateTime property type is resolved correctly'() {
		given:
		def property = domainClass.getPropertyByName("dateCreated")

		expect:
		property.type == DateTime
	}

	void 'DateTime property type is resolved correctly via meta class'() {
		given:
		def property = AuditedRecord.metaClass.hasProperty(new AuditedRecord(), "dateCreated")

		expect:
		property.type == DateTime
	}

}