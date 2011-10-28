package grails.plugin.jodatime.simpledatastore

import org.joda.time.ReadablePartial

class SimpleMapReadablePartialMarshaller extends SimpleMapJodaTimeMarshaller<ReadablePartial> {

	SimpleMapReadablePartialMarshaller() {
		super(ReadablePartial)
	}

}
