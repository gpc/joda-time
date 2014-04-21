package jodatest

import grails.converters.JSON
import org.joda.time.LocalDateTime

class TimestampController {

    def put(TimestampCommand command) {
        render contentType: 'text/plain', text: command.timestamp.toString()
    }

    def get() {
    	def command = new TimestampCommand(timestamp: new LocalDateTime())
    	render command as JSON
    }
}
