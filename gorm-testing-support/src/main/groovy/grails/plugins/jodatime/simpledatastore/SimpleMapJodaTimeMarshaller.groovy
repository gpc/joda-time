package grails.plugins.jodatime.simpledatastore

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.joda.time.DateMidnight
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.MonthDay
import org.joda.time.Partial
import org.joda.time.Period
import org.joda.time.TimeOfDay
import org.joda.time.YearMonth
import org.joda.time.YearMonthDay

/**
 * A marshaller for Joda Time types usable in the Simple Map datastore.
 * @param < T >
 */
class SimpleMapJodaTimeMarshaller<T, Q> extends AbstractMappingAwareCustomTypeMarshaller<T, Map, Q> {
    static final Set<Class> SUPPORTED_TYPES = ([LocalTime, LocalDate, LocalDateTime, MonthDay, YearMonth, Partial, DateTime, Instant, Duration, DateTimeZone, Interval, Period] as Set<Class>).asImmutable()
    static final Set<Class> DEPRECATED_TYPES = ([TimeOfDay, YearMonthDay, DateMidnight] as Set<Class>).asImmutable()

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

    private static final Set<Class> SUPPORTED_OPERATIONS = [Query.Equals, Query.NotEquals] as Set<Class>
    private static final Set<Class> SUPPORTED_OPERATIONS_FOR_COMPARABLE = SUPPORTED_OPERATIONS + ([Query.GreaterThan, Query.GreaterThanEquals, Query.LessThan, Query.LessThanEquals, Query.Between] as Set<Class>)

    @Override
    protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, Q nativeQuery) {
        Set<Class> supportedOperations = Comparable.isAssignableFrom(targetType) ? SUPPORTED_OPERATIONS_FOR_COMPARABLE : SUPPORTED_OPERATIONS
        Class op = criterion.getClass()
        if (op in supportedOperations) {
            Closure handler = (nativeQuery.query.handlers as Map<Class, Closure>)[op]
            nativeQuery.results << handler.call(criterion, property)
        } else {
            throw new RuntimeException("unsupported query type $criterion for property $property")
        }
    }
}
