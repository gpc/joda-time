package grails.plugin.jodatime.binding

import grails.persistence.Entity
import org.grails.databinding.SimpleMapDataBindingSource
import org.joda.time.DateTimeZone
import spock.lang.Specification

class DateTimeZoneConverterSpec extends Specification {

    def grailsWebDataBinder

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