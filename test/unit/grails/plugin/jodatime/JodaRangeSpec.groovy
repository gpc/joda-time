package grails.plugin.jodatime

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import spock.lang.Specification
import spock.lang.Unroll
import static grails.plugin.jodatime.JodaDynamicMethods.registerDynamicMethods
import static org.joda.time.DurationFieldType.*

class JodaRangeSpec extends Specification {

	def setupSpec() {
		registerDynamicMethods()
	}

	def "default ranges use day"() {
		given:
		def start = new LocalDate(2011, 10, 31)
		def end = new LocalDate(2011, 11, 11)

		when:
		def range = start..end

		then:
		range.size() == 12
	}

	@Unroll({"can specify $increment as the range increment"})
	def "can specify the range increment"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 1, 0, 0)

		when:
		def range = new JodaRange(increment, start, end)

		then:
		range.size() == expectedSize

		where:
		increment | expectedSize
		days()    | 2
		hours()   | 25
		minutes() | (24 * 60) + 1
	}

}
