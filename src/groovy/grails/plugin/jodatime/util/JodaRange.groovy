package grails.plugin.jodatime.util

import org.joda.time.*

class JodaRange extends ObjectRange {

	private final DurationFieldType increment

	JodaRange(DurationFieldType increment, ReadablePartial from, ReadablePartial to) {
		super(from, to)
		this.increment = increment
	}

	JodaRange(DurationFieldType increment, ReadablePartial from, ReadablePartial to, boolean reverse) {
		super(from, to, reverse)
		this.increment = increment
	}

	JodaRange(DurationFieldType increment, ReadableInstant from, ReadableInstant to) {
		super(from, to)
		this.increment = increment
	}

	JodaRange(DurationFieldType increment, ReadableInstant from, ReadableInstant to, boolean reverse) {
		super(from, to, reverse)
		this.increment = increment
	}

	JodaRange(DurationFieldType increment, Interval interval) {
		super(interval.start, interval.end)
		this.increment = increment
	}

	JodaRange(DurationFieldType increment, Interval interval, boolean reverse) {
		super(interval.start, interval.end, reverse)
		this.increment = increment
	}

	@Override
	protected Object increment(Object value) {
		value.withFieldAdded(increment, 1)
	}

	@Override
	protected Object decrement(Object value) {
		value.withFieldAdded(increment, -1)
	}

}
