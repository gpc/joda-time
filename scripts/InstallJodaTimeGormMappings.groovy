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
			event "StatusUpdate", ["Config.groovy already contains 'grails.gorm.default.mapping'"]
		} else {
			event "StatusUpdate", ["Adding default GORM mappings for Joda-Time types to Config.groovy"]
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
	}
}

setDefaultTarget(installJodaTimeGormMappings)