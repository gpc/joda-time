package grails.plugins.binding

import grails.persistence.Entity
import org.grails.databinding.SimpleMapDataBindingSource
import org.joda.time.*
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification

class DateTimeConverterSpec extends Specification {

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
        DateTimeConverterSpecEntity entity = new DateTimeConverterSpecEntity()

        def params = [:]
        params.localTime = '16:55'
        params.localDate = '22/10/2013'
        params.localDateTime = '22/10/2013 17:33'
        params.dateTime = '22/10/2013 17:33'
        params.instant = '22/10/2013 17:33'

        when:
        grailsWebDataBinder.bind entity, params as SimpleMapDataBindingSource

        then:
        entity.localTime == new LocalTime(16, 55)
        entity.localDate == new LocalDate(2013, 10, 22)
        entity.localDateTime == new LocalDateTime(2013, 10, 22, 17, 33)
        entity.dateTime == new DateTime(2013, 10, 22, 17, 33)
        entity.instant == new DateTime(2013, 10, 22, 17, 33).toInstant()
    }
}

@Entity
class DateTimeConverterSpecEntity {
    LocalTime localTime
    LocalDate localDate
    LocalDateTime localDateTime
    DateTime dateTime
    Instant instant
}
