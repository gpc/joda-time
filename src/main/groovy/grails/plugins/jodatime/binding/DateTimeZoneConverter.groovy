package grails.plugins.jodatime.binding

import grails.databinding.converters.ValueConverter
import org.joda.time.DateTimeZone

class DateTimeZoneConverter implements ValueConverter {
    boolean canConvert(value) {
        value instanceof String
    }

    def convert(value) {
        DateTimeZone.forID(value)
    }

    Class<?> getTargetType() {
        DateTimeZone
    }
}
