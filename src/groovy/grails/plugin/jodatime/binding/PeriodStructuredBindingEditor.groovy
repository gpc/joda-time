package grails.plugin.jodatime.binding

import org.grails.databinding.DataBindingSource
import org.grails.databinding.StructuredBindingEditor
import org.joda.time.Duration
import org.joda.time.DurationFieldType
import org.joda.time.MutablePeriod
import org.joda.time.Period
import org.joda.time.PeriodType

class PeriodStructuredBindingEditor implements StructuredBindingEditor {
    public static final SUPPORTED_TYPES = [Duration, Period].asImmutable()

    protected static final SUPPORTED_PERIOD_FIELDS = (0..<PeriodType.standard().size()).collect { PeriodType.standard().getFieldType(it) }
    protected static final SUPPORTED_DURATION_FIELDS = ["weeks", "days", "hours", "minutes", "seconds", "millis"].collect { DurationFieldType."$it"() }

    Class type

    PeriodStructuredBindingEditor(Class type) {
        this.type = type
    }

    Object getPropertyValue(Object obj, String propertyName, DataBindingSource source) {
        try {
            def fields = type == Duration ? SUPPORTED_DURATION_FIELDS : SUPPORTED_PERIOD_FIELDS
            def period = new MutablePeriod()
            fields.each {
                def value = source["${propertyName}_${it}"] ? source["${propertyName}_${it}"].toInteger() : 0
                period.set(it, value)
            }
            return type == Duration ? period.toPeriod().toStandardDuration() : period.toPeriod()
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Unable to parse structured period from request for period [${propertyName}]")
        }
    }
}

