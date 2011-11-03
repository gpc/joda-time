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

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsSettings")

target(installJodaTimeGormMappings: "Updates Config.groovy with default GORM mappings for Joda-Time types") {
	def configFile = new File(basedir, "grails-app/conf/Config.groovy")
	if (configFile.exists()) {
		def appConfig = configSlurper.parse(configFile.toURI().toURL())
		if (appConfig.grails.gorm.default.mapping) {
			event "StatusError", ["Config.groovy already contains 'grails.gorm.default.mapping'"]
		} else if (isNewPersistenceSupportInstalled()) {
			appendNewPersistenceMappings(configFile)
			event "StatusFinal", ["Added usertype.jodatime GORM mappings to Config.groovy"]
		} else if (isOldPersistenceSupportInstalled()) {
			appendOldPersistenceMappings(configFile)
			event "StatusFinal", ["Added joda-time-hibernate GORM mappings to Config.groovy"]
		} else {
			event "StatusError", ["No Joda-Time Hibernate persistence API is installed"]
		}
	} else {
		event "StatusError", ["Config.groovy not found"]
	}
}

boolean isNewPersistenceSupportInstalled() {
	isClassAvailable("org.jadira.usertype.dateandtime.joda.PersistentDateTime")
}

private void appendNewPersistenceMappings(File configFile) {
	event "StatusUpdate", ["Adding usertype.jodatime GORM mappings to Config.groovy"]
	configFile.withWriterAppend {
		it.write """
// Added by the Joda-Time plugin:
grails.gorm.default.mapping = {
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentDateMidnight, class: org.joda.time.DateMidnight
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentDateTime, class: org.joda.time.DateTime
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString, class: org.joda.time.DateTimeZone
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentDurationAsString, class: org.joda.time.Duration
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentInstantAsMillisLong, class: org.joda.time.Instant
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentInterval, class: org.joda.time.Interval
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDate, class: org.joda.time.LocalDate
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentLocalTime, class: org.joda.time.LocalTime
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentPeriodAsString, class: org.joda.time.Period
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentTimeOfDay, class: org.joda.time.TimeOfDay
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentYearMonthDay, class: org.joda.time.YearMonthDay
\t\"user-type\" type: org.jadira.usertype.dateandtime.joda.PersistentYears, class: org.joda.time.Years
}
"""
	}
}

boolean isOldPersistenceSupportInstalled() {
	isClassAvailable("org.joda.time.contrib.hibernate.PersistentDateTime")
}

private void appendOldPersistenceMappings(File configFile) {
	event "StatusUpdate", ["Adding joda-time-hibernate GORM mappings to Config.groovy"]
	configFile.withWriterAppend {
		it.write """
// Added by the Joda-Time plugin:
grails.gorm.default.mapping = {
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentDateTime, class: org.joda.time.DateTime
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentDuration, class: org.joda.time.Duration
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentInstant, class: org.joda.time.Instant
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentInterval, class: org.joda.time.Interval
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentLocalDate, class: org.joda.time.LocalDate
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentLocalTimeAsString, class: org.joda.time.LocalTime
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
\t\"user-type\" type: org.joda.time.contrib.hibernate.PersistentPeriod, class: org.joda.time.Period
}
"""
	}
}

private boolean isClassAvailable(String name) {
	try {
		Class.forName(name)
		true
	} catch (ClassNotFoundException e) {
		false
	}
}

setDefaultTarget(installJodaTimeGormMappings)