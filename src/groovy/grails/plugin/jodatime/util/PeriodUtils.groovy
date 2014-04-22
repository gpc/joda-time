package grails.plugin.jodatime.util

import grails.util.Holders
import org.apache.log4j.Logger
import org.joda.time.Duration
import org.joda.time.DurationFieldType
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatter
import static org.joda.time.DurationFieldType.months
import static org.joda.time.DurationFieldType.years

class PeriodUtils {

	private static final Logger log = Logger.getLogger(getClass())

	static formatPeriod(value, String fields, Locale locale, PeriodFormatter formatter=null) {
		def periodType = getPeriodType(fields, PeriodType.standard())

		if (value instanceof Duration) {
			value = value.toPeriod(periodType)
		} else {
			value = safeNormalize(value, periodType)
		}

		if (!formatter) {
			formatter = PeriodFormat.wordBased(locale)
		}

		return formatter.print(value)
        }

	static PeriodType getPeriodType(String fields, PeriodType defaultPeriodType) {
		PeriodType periodType
		if (fields) {
			periodType = getPeriodTypeForFields(fields)
		} else if (Holders.config.jodatime?.periodpicker?.default?.fields) {
			periodType = getPeriodTypeForFields(Holders.config.jodatime.periodpicker.default.fields)
		} else {
			periodType = defaultPeriodType
		}
		return periodType
	}

	private static PeriodType getPeriodTypeForFields(String fields) {
		def fieldTypes = fields.split(/\s*,\s*/).collect { DurationFieldType."$it"() } as DurationFieldType[]
		return PeriodType.forFields(fieldTypes)
	}

	/**
	 * PeriodFormat.print will throw UnsupportedOperationException if years or months are present in period but
	 * not supported by the formatter so we trim those fields off to avoid the problem.
	 */
	private static Period safeNormalize(Period value, PeriodType periodType) {
		if (!periodType.isSupported(years()) && years() in value.getFieldTypes()) {
			log.warn "Omitting years from value '$value' as format '$periodType' does not support years"
			value = value.withYears(0)
		}
		if (!periodType.isSupported(months()) && months() in value.getFieldTypes()) {
			log.warn "Omitting months from value '$value' as format '$periodType' does not support months"
			value = value.withMonths(0)
		}
		return value.normalizedStandard(periodType)
	}
}
