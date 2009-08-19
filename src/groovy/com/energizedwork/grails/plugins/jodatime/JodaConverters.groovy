package com.energizedwork.grails.plugins.jodatime

import grails.converters.JSON
import grails.converters.XML
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.ISODateTimeFormat

class JodaConverters {

	static void registerJsonAndXmlMarshallers() {
		final dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
		[JSON, XML].each {converter ->
			converter.registerObjectMarshaller(DateTime, 1) {
				return it?.toString(dateTimeFormatter.withZone(it?.zone))
			}
			converter.registerObjectMarshaller(LocalDate, 2) {
				return it?.toString("yyyy-MM-dd")
			}
			converter.registerObjectMarshaller(LocalTime, 3) {
				return it?.toString("HH:mm:ss")
			}
			converter.registerObjectMarshaller(LocalDateTime, 4) {
				return it?.toString("yyyy-MM-dd'T'HH:mm:ss")
			}
		}
	}

}