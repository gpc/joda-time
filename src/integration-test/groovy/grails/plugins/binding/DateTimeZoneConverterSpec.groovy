package grails.plugins.binding

import grails.databinding.SimpleMapDataBindingSource
import grails.persistence.Entity
import grails.test.mixin.integration.Integration
import grails.web.databinding.GrailsWebDataBinder
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
class DateTimeZoneConverterSpec extends Specification {

    @Autowired
    GrailsWebDataBinder grailsWebDataBinder

    void "test conversion"() {
        given:
        DateTimeZoneConverterSpecEntity entity = new DateTimeZoneConverterSpecEntity()

        def params = [:]
        params.dateTimeZone = 'America/Chicago'

        when:
        grailsWebDataBinder.bind entity, params as SimpleMapDataBindingSource

        then:
        entity.dateTimeZone == DateTimeZone.forID('America/Chicago')
    }
}

@Entity
class DateTimeZoneConverterSpecEntity {
    DateTimeZone dateTimeZone
}
