package grails.plugin.jodatime.simpledatastore

import org.grails.datastore.mapping.model.MappingFactory
import org.joda.time.*

class JodaTimeUnitTestSupport {

	static final Iterable<Class> SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, MonthDay, TimeOfDay, YearMonth, YearMonthDay, Partial, DateTime, DateMidnight, Instant]

	static registerJodaTimePropertyTypes() {
		for (type in SUPPORTED_TYPES) {
			MappingFactory.registerCustomType(new SimpleMapJodaTimeMarshaller(LocalDate))
		}
	}

}
