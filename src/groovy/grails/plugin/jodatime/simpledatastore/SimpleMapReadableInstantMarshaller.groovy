package grails.plugin.jodatime.simpledatastore

import org.joda.time.ReadableInstant

class SimpleMapReadableInstantMarshaller extends SimpleMapJodaTimeMarshaller<ReadableInstant> {

	SimpleMapReadableInstantMarshaller() {
		super(ReadableInstant)
	}

}
