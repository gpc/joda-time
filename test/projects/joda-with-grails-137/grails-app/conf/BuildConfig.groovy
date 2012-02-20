grails.project.work.dir = "target"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
		compile "org.jadira.usertype:usertype.jodatime:1.9"
    }
	plugins {
		test ":spock:0.5-groovy-1.7"
	}
}

grails.plugin.location."joda-time" = "../../.."