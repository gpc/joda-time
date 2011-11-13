package grails.plugin.jodatime.util

import org.joda.time.*

class DateTimeRange extends ObjectRange {

	private final DurationFieldType increment
	
	static <T extends ReadablePartial> Range<T> asRange(DurationFieldType increment, T from, T to) {
		new DateTimeRange(increment, from, to)
	}

	static <T extends ReadableInstant> Range<T> asRange(DurationFieldType increment, T from, T to) {
		new DateTimeRange(increment, from, to)
	}

	static Range<Interval> asRange(DurationFieldType increment, Interval interval) {
		new DateTimeRange(increment, interval)
	}

	DateTimeRange(DurationFieldType increment, ReadablePartial from, ReadablePartial to) {
		super(from, to)
		this.increment = increment
	}

	DateTimeRange(DurationFieldType increment, ReadablePartial from, ReadablePartial to, boolean reverse) {
		super(from, to, reverse)
		this.increment = increment
	}

	DateTimeRange(DurationFieldType increment, ReadableInstant from, ReadableInstant to) {
		super(from, to)
		this.increment = increment
	}

	DateTimeRange(DurationFieldType increment, ReadableInstant from, ReadableInstant to, boolean reverse) {
		super(from, to, reverse)
		this.increment = increment
	}

	DateTimeRange(DurationFieldType increment, Interval interval) {
		super(interval.start, interval.end)
		this.increment = increment
	}

	DateTimeRange(DurationFieldType increment, Interval interval, boolean reverse) {
		super(interval.start, interval.end, reverse)
		this.increment = increment
	}

	List step(DurationFieldType increment) {
		new DateTimeRange(increment, from, to)
	}

	List step(DurationFieldType increment, int step) {
		new DateTimeRange(increment, from, to).step(step)
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
