package grails.plugins.jodatime.simpledatastore

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.simple.query.SimpleMapResultList
import org.joda.time.*

/**
 * A marshaller for Joda-Time types usable in the Simple Map datastore.
 * @param < T >
 */
class SimpleMapJodaTimeMarshaller<T> extends AbstractMappingAwareCustomTypeMarshaller<T, Map, SimpleMapResultList> {

	SimpleMapJodaTimeMarshaller(Class<T> targetType) {
		super(targetType)
	}

	@Override
	protected writeInternal(PersistentProperty property, String key, T value, Map nativeTarget) {
		nativeTarget[key] = value
	}

	@Override
	protected T readInternal(PersistentProperty property, String key, Map nativeSource) {
		nativeSource[key]
	}

	private static final Set<Class>SUPPORTED_OPERATIONS = [Query.Equals, Query.NotEquals]
	private static final Set<Class>SUPPORTED_OPERATIONS_FOR_COMPARABLE = SUPPORTED_OPERATIONS + [Query.GreaterThan, Query.GreaterThanEquals, Query.LessThan, Query.LessThanEquals, Query.Between]

	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, SimpleMapResultList nativeQuery) {
		def supportedOperations = Comparable.isAssignableFrom(targetType) ? SUPPORTED_OPERATIONS_FOR_COMPARABLE : SUPPORTED_OPERATIONS
		Class op = criterion.getClass()
		if (op in supportedOperations) {
			Closure handler = (nativeQuery.query.handlers as Map<Class, Closure>)[op]
			nativeQuery.results << handler.call(criterion, property)
		} else {
			throw new RuntimeException("unsupported query type $criterion for property $property")
		}
	}

	static final Set<Class> SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, MonthDay, YearMonth, Partial, DateTime, Instant, Duration, DateTimeZone, Interval, Period].asImmutable()

	static final Set<Class> DEPRECATED_TYPES = [TimeOfDay, YearMonthDay, DateMidnight].asImmutable()
}
