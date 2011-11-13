package grails.plugin.jodatime.util

import static grails.plugin.jodatime.JodaDynamicMethods.registerDynamicMethods
import org.joda.time.*
import static org.joda.time.DurationFieldType.*
import spock.lang.*

class DateTimeRangeSpec extends Specification {

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
		def range = DateTimeRange.asRange(increment, start, end)

		then:
		range.size() == expectedSize

		where:
		increment | expectedSize
		days()    | 2
		hours()   | 25
		minutes() | (24 * 60) + 1
	}

	def "unsupported field types cannot be used"() {
		given:
		def from = new LocalDate(2011, 11, 11)
		def to = new LocalDate(2011, 11, 14)
		def range = DateTimeRange.asRange(hours(), from, to)

		when:
		range.step(1)

		then:
		thrown IllegalArgumentException
	}

	def "can use ReadableInstant implementations"() {
		given:
		def from = new DateTime(2011, 11, 11, 0, 0)
		def to = new DateTime(2011, 11, 11, 23, 0)

		expect:
		DateTimeRange.asRange(hours(), from, to).size() == 24
	}

	def "can use an Interval"() {
		given:
		def from = new DateTime(2011, 11, 11, 0, 0)
		def to = new DateTime(2011, 11, 11, 23, 0)
		def interval = new Interval(from, to)

		when:
		def range = DateTimeRange.asRange(hours(), interval)

		then:
		range.size() == 24
	}

	def "can use step method to increment in larger chunks"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 1, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(2)

		then:
		range.size() == 13
	}
	
	def "can use step method to increment with different fields"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(days())

		then:
		range.size() == 31
	}

	def "can use step method to increment with different fields and chunk sizes"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(days(), 2)

		then:
		range.size() == 16
	}

}
