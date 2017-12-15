/*
 * Copyright 2010 Rob Fletcher
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
package grails.plugins.jodatime.binding

import org.grails.testing.GrailsUnitTest
import org.joda.time.*
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Locale.UK
import static java.util.Locale.US
import static org.joda.time.DateTimeZone.UTC

@Unroll
class DateTimeEditorSpec extends Specification  implements GrailsUnitTest {

  void cleanup() {
    grailsApplication.config.remove("jodatime")
    // it is frankly shocking that Grails requires me to do this. The test environment is not properly idempotent as configuration changes will leak from one test to another
  }

  void "getAsText converts null to empty string"() {
    given:
    def editor = new DateTimeEditor(LocalDate)

    when: editor.value = null
    then: editor.asText == ""
  }

  void "setAsText converts empty string to null"() {
    given:
    def editor = new DateTimeEditor(LocalDate)

    when: editor.asText = ""
    then: editor.value == null
  }

  void "getAsText formats #type.simpleName instances correctly for #locale locale"() {
    given:
    def editor = new DateTimeEditor(type)

    and: LocaleContextHolder.locale = locale

    when: editor.value = value

    then: editor.asText == expected

    where:
    type          | value                                 | locale | expected
    LocalDate     | new LocalDate(1971, 11, 29)           | UK     | "29/11/71"
    LocalDate     | new LocalDate(1971, 11, 29)           | US     | "11/29/71"
    LocalDateTime | new LocalDateTime(2009, 3, 6, 17, 0)  | UK     | "06/03/09 17:00"
    LocalDateTime | new LocalDateTime(2009, 3, 6, 17, 0)  | US     | "3/6/09 5:00 PM"
    DateTime      | new DateTime(2009, 3, 6, 17, 0, 0, 0) | UK     | "06/03/09 17:00"
    DateTime      | new DateTime(2009, 3, 6, 17, 0, 0, 0) | US     | "3/6/09 5:00 PM"
    LocalTime     | new LocalTime(23, 59)                 | UK     | "23:59"
    LocalTime     | new LocalTime(23, 59)                 | US     | "11:59 PM"
    Instant       | new Instant(92554380000)              | UK     | "07/12/72 05:33"
    Instant       | new Instant(92554380000)              | US     | "12/7/72 5:33 AM"
  }

  void "getAsText formats #type.simpleName instances correctly according to a configured pattern"() {
    given:
    grailsApplication.config.jodatime = [format: ["org.joda.time.$type.simpleName": config]]

    and:
    def editor = new DateTimeEditor(type)

    when: editor.value = value
    then: editor.asText == expected

    where:
    type          | config                | value                                      | expected
    LocalDate     | "dd/MM/yyyy"          | new LocalDate(1971, 11, 29)                | "29/11/1971"
    LocalDateTime | "dd/MM/yyyy h:mm a"   | new LocalDateTime(1971, 11, 29, 17, 0)     | "29/11/1971 5:00 PM"
    DateTime      | "dd/MM/yyyy h:mm a Z" | new DateTime(2009, 3, 6, 17, 0, 0, 0, UTC) | "06/03/2009 5:00 PM +0000"
    LocalTime     | "h:mm a"              | new LocalTime(23, 59)                      | "11:59 PM"
    Instant       | "dd/MM/yyyy h:mm a Z" | new Instant(92554380000)                   | "07/12/1972 5:33 AM +0000"
  }

  void "getAsText formats #type.simpleName instances correctly for HTML5"() {
    given:
    grailsApplication.config.jodatime = [format: [html5: true]]

    and:
    def editor = new DateTimeEditor(type)

    when: editor.value = value
    then: editor.asText == expected

    where:
    type          | value                                                                                              | expected
    LocalDate     | new LocalDate(1971, 11, 29)                                                                        | "1971-11-29"
    LocalDateTime | new LocalDateTime(1971, 11, 29, 17, 0)                                                             | "1971-11-29T17:00:00.000"
    DateTime      | new DateTime(2009, 3, 6, 17, 0, 0, 0).toLocalDateTime().toDateTime(DateTimeZone.forOffsetHours(1)) | "2009-03-06T17:00:00.000+01:00"
    LocalTime     | new LocalTime(23, 59)                                                                              | "23:59:00.000"
    Instant       | new Instant(92554380000)                                                                           | "1972-12-07T05:33:00.000Z"
  }

  def "Instant values are always formatted as UTC"() {
    given:
    grailsApplication.config.jodatime = [format:[html5: true]]

    and:
    def defaultTimeZone = DateTimeZone.default
    DateTimeZone.default = DateTimeZone.forID("EST")

    and:
    def editor = new DateTimeEditor(Instant)

    when: editor.value = new Instant(92554380000)
    then: editor.asText == "1972-12-07T05:33:00.000Z"

    cleanup:
    DateTimeZone.default = defaultTimeZone
  }

  void "setAsText parses #type.simpleName instances from #locale locale format text"() {
    given:
    grailsApplication.config.jodatime = [:]
    and:
    def editor = new DateTimeEditor(type)

    and: LocaleContextHolder.locale = locale

    when: editor.asText = text
    then: editor.value == expected

    where:
    type          | text              | locale | expected
    LocalDate     | "29/11/71"        | UK     | new LocalDate(1971, 11, 29)
    LocalDate     | "11/29/71"        | US     | new LocalDate(1971, 11, 29)
    LocalDateTime | "06/03/09 17:00"  | UK     | new LocalDateTime(2009, 3, 6, 17, 0)
    LocalDateTime | "3/6/09 5:00 PM"  | US     | new LocalDateTime(2009, 3, 6, 17, 0)
    DateTime      | "06/03/09 17:00"  | UK     | new DateTime(2009, 3, 6, 17, 0, 0, 0)
    DateTime      | "3/6/09 5:00 PM"  | US     | new DateTime(2009, 3, 6, 17, 0, 0, 0)
    LocalTime     | "23:59"           | UK     | new LocalTime(23, 59)
    LocalTime     | "11:59 PM"        | US     | new LocalTime(23, 59)
    Instant       | "07/12/72 05:33"  | UK     | new DateTime(1972, 12, 7, 5, 33, 0, 0).toInstant()
    Instant       | "12/7/72 5:33 AM" | US     | new DateTime(1972, 12, 7, 5, 33, 0, 0).toInstant()
  }

  void "setAsText parses #type.simpleName instances correctly according to a configured pattern"() {
    given:
    grailsApplication.config.jodatime = [format: ["org.joda.time.$type.simpleName": config]]

    and:
    def editor = new DateTimeEditor(type)

    when: editor.asText = text
    then: editor.value == expected

    where:
    type          | config                | text                        | expected
    LocalDate     | "dd/MM/yyyy"          | "29/11/1971"                | new LocalDate(1971, 11, 29)
    LocalDateTime | "dd/MM/yyyy h:mm a"   | "29/11/1971 5:00 PM"        | new LocalDateTime(1971, 11, 29, 17, 0)
    DateTime      | "dd/MM/yyyy h:mm a Z" | "06/03/2009 5:00 PM +0000"  | new DateTime(2009, 3, 6, 17, 0, 0, 0, UTC)
    LocalTime     | "h:mm a"              | "11:59 PM"                  | new LocalTime(23, 59)
    Instant       | "dd/MM/yyyy h:mm a Z" | "07/12/1972 12:33 AM -0500" | new DateTime(1972, 12, 7, 5, 33, 0, 0, UTC).toInstant()
  }

  void "setAsText parses #type.simpleName instances correctly using HTML5 format"() {
    given:
    grailsApplication.config.jodatime = [format: [html5: true]]

    and:
    def editor = new DateTimeEditor(type)

    when: editor.asText = text
    then: editor.value == expected

    where:
    type          | text                        | expected
    LocalDate     | "1971-11-29"                | new LocalDate(1971, 11, 29)
    LocalDateTime | "1971-11-29T17:00:00"       | new LocalDateTime(1971, 11, 29, 17, 0)
    DateTime      | "2009-03-06T17:00:00+00:00" | new DateTime(2009, 3, 6, 17, 0, 0, 0, UTC)
    DateTime      | "2009-03-06T17:00:00Z"      | new DateTime(2009, 3, 6, 17, 0, 0, 0, UTC)
    DateTime      | "2009-03-06T17:00:00.123Z"  | new DateTime(2009, 3, 6, 17, 0, 0, 123, UTC)
    LocalTime     | "23:59:00"                  | new LocalTime(23, 59)
    Instant       | "1972-12-07T05:33:00.000Z"  | new DateTime(1972, 12, 7, 5, 33, 0, 0, UTC).toInstant()
  }

  void "configured format trumps HTML5"() {
    given:
    grailsApplication.config.jodatime = [format: [html5: true, "$LocalDate.name":"dd/MM/yyyy"]]

    and:
    def editor = new DateTimeEditor(LocalDate)

    when: editor.value = new LocalDate(1971, 11, 29)
    then: editor.asText == "29/11/1971"
  }
}
