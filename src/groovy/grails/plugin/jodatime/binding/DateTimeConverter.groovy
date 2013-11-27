package grails.plugin.jodatime.binding

import grails.plugin.jodatime.Html5DateTimeFormat
import org.grails.databinding.converters.ValueConverter
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
    def grailsApplication

    @Lazy private ConfigObject config = grailsApplication.config.jodatime.format

    public boolean canConvert(Object value) {
        value instanceof String
    }

    public Object convert(Object value) {
        value ? formatter.parseDateTime(value)."to$type.simpleName"() : null
    }

    public Class<?> getTargetType() {
        type
    }

    protected DateTimeFormatter getFormatter() {
        if (hasConfigPatternFor(type)) {
            return DateTimeFormat.forPattern(getConfigPatternFor(type))
        } else if (useISO()) {
            return getISOFormatterFor(type)
        } else {
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
            Locale locale = LocaleContextHolder.locale
            return DateTimeFormat.forStyle(style).withLocale(locale)
        }
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
        return null
    }
}
