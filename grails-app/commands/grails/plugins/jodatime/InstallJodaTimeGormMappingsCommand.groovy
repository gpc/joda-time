package grails.plugins.jodatime

import grails.dev.commands.ApplicationCommand
import grails.dev.commands.ExecutionContext

class InstallJodaTimeGormMappingsCommand implements ApplicationCommand {

    @Override
    boolean handle(ExecutionContext ctx) {
        def configFile = new File(ctx.baseDir, "grails-app/conf/application.groovy")
        if (configFile.exists()) {
            def appConfig = new ConfigSlurper().parse(configFile.toURI().toURL())
            if (appConfig.grails.gorm.default.mapping) {
                println "application.groovy already contains 'grails.gorm.default.mapping'"
            } else if (isNewPersistenceSupportInstalled()) {
                appendNewPersistenceMappings(configFile)
                println "Added usertype.jodatime GORM mappings to application.groovy"
            } else if (isOldPersistenceSupportInstalled()) {
                appendOldPersistenceMappings(configFile)
                println "Added joda-time-hibernate GORM mappings to application.groovy"
            } else {
                println "No Joda-Time Hibernate persistence API is installed"
            }
        } else {
            println "application.groovy not found"
        }
        return true
    }

    @Override
    String getDescription() {
        "Updates application.groovy with default GORM mappings for Joda-Time types"
    }

    boolean isNewPersistenceSupportInstalled() {
        isClassAvailable("org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    }

    private void appendNewPersistenceMappings(File configFile) {
        println "Adding usertype.jodatime GORM mappings to application.groovy"
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
        println "Adding joda-time-hibernate GORM mappings to application.groovy"
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
}
