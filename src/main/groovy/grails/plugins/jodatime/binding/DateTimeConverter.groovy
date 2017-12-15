package grails.plugins.jodatime.binding

import grails.config.Config
import grails.core.GrailsApplication
import grails.core.support.GrailsConfigurationAware
import grails.databinding.converters.ValueConverter
import grails.plugins.jodatime.Html5DateTimeFormat
import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.context.i18n.LocaleContextHolder

class DateTimeConverter implements ValueConverter, GrailsConfigurationAware {

    static final SUPPORTED_TYPES = [LocalTime, LocalDate, LocalDateTime, DateTime, Instant].asImmutable()

    Class type
     Config config

    @Override
    void setConfiguration(Config co) {
        config = co
    }

    boolean canConvert(Object value) {
        value instanceof String
    }

    def convert(Object value) {
        value ? formatter.parseDateTime(value as String)."to$type.simpleName"() : null
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
        Locale locale = LocaleContextHolder.locale
        return DateTimeFormat.forStyle(style).withLocale(locale)
    }

    private boolean hasConfigPatternFor(Class type) {
        config.hasProperty("jodatime.format.${type.name}")
    }

    private String getConfigPatternFor(Class type) {
        config.getProperty("jodatime.format.${type.name}")
    }

    private boolean useISO() {
        config.hasProperty("jodatime.format.html5")
    }

    private static DateTimeFormatter getISOFormatterFor(Class type) {
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
