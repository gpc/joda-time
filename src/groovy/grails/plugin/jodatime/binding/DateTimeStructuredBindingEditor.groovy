package grails.plugin.jodatime.binding

import org.grails.databinding.DataBindingSource
import org.grails.databinding.StructuredBindingEditor
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.MutableDateTime

class DateTimeStructuredBindingEditor implements StructuredBindingEditor {

    static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime].asImmutable()

    Class type

    DateTimeStructuredBindingEditor(Class type) {
        this.type = type
    }

    private static final FIELDS_BY_TYPE = [
            (LocalDate): ["year", "month", "day"].asImmutable(),
            (LocalTime): ["hour", "minute", "second"].asImmutable(),
            (LocalDateTime): ["year", "month", "day", "hour", "minute", "second"].asImmutable(),
            (DateTime): ["year", "month", "day", "hour", "minute", "second", "zone"].asImmutable()
    ].asImmutable()

    private static final DEFAULT_VALUES = [month: 1, day: 1, hour: 0, minute: 0, second: 0].asImmutable()

    private static final JODA_PROP_NAMES = [year: "year", month: "monthOfYear", day: "dayOfMonth", hour: "hourOfDay", minute: "minuteOfHour", second: "secondOfMinute"].asImmutable()


    List getRequiredFields() {
        return [FIELDS_BY_TYPE[type].head()]
    }

    List getOptionalFields() {
        return FIELDS_BY_TYPE[type].tail()
    }

    Object getPropertyValue(Object obj, String propertyName, DataBindingSource source) {
        requiredFields.each {
            if (!source["${propertyName}_${it}"]) {
                throw new IllegalArgumentException("Can't populate a $type without a $it")
            }
        }

        try {
            def dt = new MutableDateTime()
            dt.secondOfMinute = 0
            dt.millisOfSecond = 0
            (requiredFields + optionalFields).each {
                switch (it) {
                    case "zone":
                        // null is OK here as DateTimeZone.forID(null) returns default zone
                        dt.zoneRetainFields = DateTimeZone.forID(source["${propertyName}_${it}"])
                        break
                    default:
                        dt."${JODA_PROP_NAMES[it]}" = (source["${propertyName}_${it}"]?.toInteger() ?: DEFAULT_VALUES[it])
                }
            }
            return dt.toDateTime()."to$type.simpleName"()
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Unable to parse structured date from request for $type [$propertyName]")
        }
    }
}
