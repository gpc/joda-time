package grails.plugins.jodatime.binding

import grails.core.GrailsApplication
import grails.databinding.converters.ValueConverter
import grails.plugins.jodatime.Html5DateTimeFormat
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.context.i18n.LocaleContextHolder

class DateTimeConverter implements ValueConverter {

    static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime, Instant].asImmutable()

    Class type
    GrailsApplication grailsApplication

    @Lazy private ConfigObject config = grailsApplication.config.jodatime.format

    boolean canConvert(value) {
        value instanceof String
    }

    def convert(value) {
        value ? formatter.parseDateTime(value)."to$type.simpleName"() : null
    }

    Class<?> getTargetType() {
        type
    }

    protected DateTimeFormatter getFormatter() {
        if (hasConfigPatternFor(type)) {
            return DateTimeFormat.forPattern(getConfigPatternFor(type))
        }

        if (useISO()) {
            return getISOFormatterFor(type)
        }

        def style
        switch (type) {
            case LocalTime:
                style = '-S'
                break
            case LocalDate:
                style = 'S-'
                break
            default:
                style = 'SS'
        }

        return DateTimeFormat.forStyle(style).withLocale(LocaleContextHolder.locale)
    }

    private boolean hasConfigPatternFor(Class type) {
        config.flatten()."$type.name"
    }

    private String getConfigPatternFor(Class type) {
        config.flatten()."$type.name"
    }

    private boolean useISO() {
        config.html5
    }

    private DateTimeFormatter getISOFormatterFor(Class type) {
        switch (type) {
            case LocalTime:
                return Html5DateTimeFormat.time()
            case LocalDate:
                return Html5DateTimeFormat.date()
            case LocalDateTime:
                return Html5DateTimeFormat.datetimeLocal()
            case DateTime:
            case Instant:
                return Html5DateTimeFormat.datetime()
        }
    }
}
