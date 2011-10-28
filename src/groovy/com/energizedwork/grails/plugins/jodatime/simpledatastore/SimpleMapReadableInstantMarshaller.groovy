package com.energizedwork.grails.plugins.jodatime.simpledatastore

import org.joda.time.ReadableInstant

class SimpleMapReadableInstantMarshaller extends SimpleMapJodaTimeMarshaller<ReadableInstant> {

	SimpleMapReadableInstantMarshaller() {
		super(ReadableInstant)
	}

}
