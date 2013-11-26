package grails.plugin.jodatime.binding

import grails.persistence.Entity
import org.grails.databinding.SimpleMapDataBindingSource
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Period
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification

class PeriodConverterSpec extends Specification {
    def grailsWebDataBinder
    static currentLocale
    static currentTimeZone

    def setupSpec() {
        currentLocale = LocaleContextHolder.locale
        LocaleContextHolder.locale = Locale.UK

        currentTimeZone = DateTimeZone.default
        DateTimeZone.default == DateTimeZone.UTC
    }

    def cleanupSpec() {
        LocaleContextHolder.locale = currentLocale
        DateTimeZone.default = currentTimeZone
    }

    void "test conversion"() {
        given:
        PeriodConverterSpecEntity entity = new PeriodConverterSpecEntity()

        def params = [:]
        params.period = '4 weeks, 2 days and 10 hours'
        params.duration = '4 weeks, 2 days and 10 hours'

        when:
        grailsWebDataBinder.bind entity, params as SimpleMapDataBindingSource

        then:
        entity.period == new Period(0,0,4,2,10,0,0,0)
        entity.duration == new Duration(2628000000)
    }
}

@Entity
class PeriodConverterSpecEntity {
    Period period
    Duration duration
}

