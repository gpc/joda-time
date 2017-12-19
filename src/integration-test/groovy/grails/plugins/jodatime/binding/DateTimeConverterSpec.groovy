package grails.plugins.jodatime.binding

import grails.config.Config
import grails.core.GrailsApplication
import grails.databinding.SimpleMapDataBindingSource
import grails.persistence.Entity
import grails.testing.mixin.integration.Integration
import grails.web.databinding.GrailsWebDataBinder
import org.joda.time.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification

@Integration
class DateTimeConverterSpec extends Specification {

    @Autowired
    GrailsWebDataBinder grailsWebDataBinder

    @Autowired
    GrailsApplication grailsApplication

    static currentLocale
    static currentTimeZone

    void setupSpec() {
        currentLocale = LocaleContextHolder.locale
        LocaleContextHolder.locale = Locale.UK

        currentTimeZone = DateTimeZone.default
        DateTimeZone.default == DateTimeZone.UTC
    }

    void cleanupSpec() {
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
   void "test conversion with configuration"() {
        given:
        DateTimeConverterSpecEntity entity = new DateTimeConverterSpecEntity()

        Config config = grailsApplication.config
        config.setAt('jodatime.format.org.joda.time.LocalTime','HH mm')
        config.setAt('jodatime.format.org.joda.time.LocalDate','yyyy-MM-dd')
        config.setAt('jodatime.format.org.joda.time.LocalDateTime','yyyy-MM-dd HH:mm')

        def params = [:]
        params.localTime = '16 55'
        params.localDate = '2013-10-22'
        params.localDateTime = '2013-10-22 17:33'

        when:
        grailsWebDataBinder.bind entity, params as SimpleMapDataBindingSource

        then:
        entity.localTime == new LocalTime(16, 55)
        entity.localDate == new LocalDate(2013, 10, 22)
        entity.localDateTime == new LocalDateTime(2013, 10, 22, 17, 33)

       cleanup:
       config.remove('jodatime.format.org.joda.time.LocalTime')
       config.remove('jodatime.format.org.joda.time.LocalDate')
       config.remove('jodatime.format.org.joda.time.LocalDateTime')

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
