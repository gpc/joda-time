/*
 * Copyright 2014 Donald Oellerich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.jodatime.util

import spock.lang.Specification
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import static java.util.Locale.ENGLISH

class PeriodUtilsSpec extends Specification {

	void 'formatPeriod accepts formatter attribute'() {
		given:
		def value = new Period().withWeeks(2).withHours(50).withMinutes(2).withSeconds(2)
		def formatter = new PeriodFormatterBuilder()
			.appendDays().appendSuffix("d").appendSeparator(", ")
			.appendHours().appendSuffix("h").appendSeparator(" and ")
			.appendMinutes().appendSuffix("m")
			.toFormatter()

		expect:
		PeriodUtils.formatPeriod(value, "days,hours,minutes", ENGLISH, formatter) == "16d, 2h and 2m"
	}
}
