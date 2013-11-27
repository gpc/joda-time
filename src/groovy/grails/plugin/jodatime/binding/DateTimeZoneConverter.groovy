package grails.plugin.jodatime.binding

import org.grails.databinding.converters.ValueConverter
import org.joda.time.DateTimeZone

class DateTimeZoneConverter implements ValueConverter {
    boolean canConvert(Object value) {
        value instanceof String
    }

    Object convert(Object value) {
        DateTimeZone.forID(value)
    }

    Class<?> getTargetType() {
        DateTimeZone
    }
}
