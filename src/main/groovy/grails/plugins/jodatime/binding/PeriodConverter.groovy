package grails.plugins.jodatime.binding

import grails.databinding.converters.ValueConverter
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatter

class PeriodConverter implements ValueConverter {
    private static final PeriodFormatter FORMATTER = PeriodFormat.default

    static final Collection<Class> SUPPORTED_TYPES = [Duration, Period].asImmutable()

    Class type

    boolean canConvert(value) {
        value instanceof String
    }

    def convert(value) {
        if (!value) {
            return null
        }

        if (type == Period) {
            return FORMATTER.parsePeriod(value)
        }

        if (type == Duration) {
            return FORMATTER.parsePeriod(value).toStandardDuration()
        }

        throw new IllegalStateException("Unsupported type $type")
    }

    Class<?> getTargetType() {
        type
    }
}
