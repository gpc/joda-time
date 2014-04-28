package jodatest

import grails.converters.JSON
import org.joda.time.LocalDateTime

class TimestampController {

    def put(TimestampCommand command) {
        render contentType: 'text/plain', text: command.timestamp.toString()
    }

}
