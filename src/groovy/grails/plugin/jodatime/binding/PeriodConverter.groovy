package grails.plugin.jodatime.binding

import org.grails.databinding.converters.ValueConverter
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatter

class PeriodConverter implements ValueConverter {
    private static final PeriodFormatter FORMATTER = PeriodFormat.default

    static final SUPPORTED_TYPES = [Duration, Period].asImmutable()

    Class type

    boolean canConvert(Object value) {
        value instanceof String
    }

    Object convert(Object value) {
        if (!value) {
            return null
        } else if (type == Period) {
            return FORMATTER.parsePeriod(value)
        } else if (type == Duration) {
            return FORMATTER.parsePeriod(value).toStandardDuration()
        } else {
            throw new IllegalStateException("Unsupported type $type")
        }
    }

    Class<?> getTargetType() {
        type
    }
}
