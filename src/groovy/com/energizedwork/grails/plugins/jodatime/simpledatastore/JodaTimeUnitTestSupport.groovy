package com.energizedwork.grails.plugins.jodatime.simpledatastore

import org.grails.datastore.mapping.model.MappingFactory

class JodaTimeUnitTestSupport {
	
	static registerJodaTimePropertyTypes() {
		MappingFactory.registerCustomType(new SimpleMapReadablePartialMarshaller())
		MappingFactory.registerCustomType(new SimpleMapReadableInstantMarshaller())
	}
	
}
