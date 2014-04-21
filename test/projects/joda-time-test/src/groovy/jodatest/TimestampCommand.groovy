package jodatest

import grails.validation.Validateable
import org.joda.time.LocalDateTime

@Validateable
class TimestampCommand {
    LocalDateTime timestamp
}
