package grails.plugin.jodatime

import org.joda.time.DurationFieldType
import org.joda.time.ReadablePartial

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

	@Override
	protected Object increment(Object value) {
		value.withFieldAdded(increment, 1)
	}

	@Override
	protected Object decrement(Object value) {
		value.withFieldAdded(increment, -1)
	}


}
