package grails.plugins.jodatime.converters

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.databinding.converters.FormattedValueConverter
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

class FormattedDateTimeConverter implements FormattedValueConverter, GrailsConfigurationAware {

    static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime, Instant].asImmutable()

    Class type
    Config config

    FormattedDateTimeConverter(Class type) {
        this.type = type
    }

    @Override
    void setConfiguration(Config co) {
        config = co
    }

    @Override
    Object convert(Object value, String format) {
        if (!value) {
            return null
        }

        if (type.isAssignableFrom(value.class)) {
            return value
        } else if (value instanceof CharSequence) {
            return DateTimeFormat.forPattern(format).parseDateTime(value)."to$type.simpleName"()
        } else {
            throw new IllegalStateException("Invalid type for value ${value.class} with value ${value}")
        }
    }

    Class<?> getTargetType() {
        type
    }
}
