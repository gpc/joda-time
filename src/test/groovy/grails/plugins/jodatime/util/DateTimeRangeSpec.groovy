package grails.plugins.jodatime.util

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import spock.lang.Specification
import spock.lang.Unroll

import static grails.plugins.jodatime.JodaDynamicMethods.registerDynamicMethods
import static org.joda.time.DurationFieldType.days
import static org.joda.time.DurationFieldType.hours
import static org.joda.time.DurationFieldType.minutes

@Unroll
class DateTimeRangeSpec extends Specification {

	void setupSpec() {
		registerDynamicMethods()
	}

	void "default ranges use day"() {
		given:
		def start = new LocalDate(2011, 10, 31)
		def end = new LocalDate(2011, 11, 11)

		when:
		def range = start..end

		then:
		range.size() == 12
	}

	void "can specify #increment as the range increment"() {
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

	void "unsupported field types cannot be used"() {
		given:
		def from = new LocalDate(2011, 11, 11)
		def to = new LocalDate(2011, 11, 14)
		def range = DateTimeRange.asRange(hours(), from, to)

		when:
		range.step(1)

		then:
		thrown IllegalArgumentException
	}

	void "can use ReadableInstant implementations"() {
		given:
		def from = new DateTime(2011, 11, 11, 0, 0)
		def to = new DateTime(2011, 11, 11, 23, 0)

		expect:
		DateTimeRange.asRange(hours(), from, to).size() == 24
	}

	void "can use an Interval"() {
		given:
		def from = new DateTime(2011, 11, 11, 0, 0)
		def to = new DateTime(2011, 11, 11, 23, 0)
		def interval = new Interval(from, to)

		when:
		def range = DateTimeRange.asRange(hours(), interval)

		then:
		range.size() == 24
	}

	void "can use step method to increment in larger chunks"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 1, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(2)

		then:
		range.size() == 13
	}

	void "can use step method to increment with different fields"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(days())

		then:
		range.size() == 31
	}

	void "can use step method to iterate with different fields"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def collectedElements = []
		DateTimeRange.asRange(hours(), start, end).step(days()) {
			collectedElements << it
		}

		then:
		collectedElements.size() == 31
		collectedElements.first() == start
		collectedElements.last() == end
	}

	void "can use step method to increment with different fields and chunk sizes"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def range = DateTimeRange.asRange(hours(), start, end).step(2, days())

		then:
		range.size() == 16
	}

	void "can use step method to iterate with different fields and chunk sizes"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)

		when:
		def collectedElements = []
		DateTimeRange.asRange(hours(), start, end).step(2, days()) {
			collectedElements << it
		}

		then:
		collectedElements.size() == 16
		collectedElements.first() == start
		collectedElements.last() == end
	}

	void "can use step with DurationFieldType argument to convert a regular range"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)
		def range = start..end

		expect:
		range.step(days()).size() == 31
		range.step(2, days()).size() == 16
	}

	void "can use step with DurationFieldType argument to iterate over a regular range"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)
		def range = start..end

		when:
		def collectedElements = []
		range.step(days()) {
			collectedElements << it
		}

		then:
		collectedElements.size() == 31
	}

	void "can use step with DurationFieldType and int arguments to iterate over a regular range"() {
		given:
		def start = new LocalDateTime(2011, 10, 31, 0, 0)
		def end = new LocalDateTime(2011, 11, 30, 0, 0)
		def range = start..end

		when:
		def collectedElements = []
		range.step(2, days()) {
			collectedElements << it
		}

		then:
		collectedElements.size() == 16
	}

	void "step cannot be used with a DurationFieldType argument on non-joda ranges"() {
		given:
		def range = 0..5

		when:
		range.step(days())

		then:
		thrown MissingMethodException
	}
}
