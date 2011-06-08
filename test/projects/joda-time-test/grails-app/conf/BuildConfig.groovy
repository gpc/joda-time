grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {        
        grailsPlugins()
        grailsHome()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
		test "org.hamcrest:hamcrest-all:1.1"
		test("org.seleniumhq.selenium:selenium-htmlunit-driver:2.0b3") {
			excludes "xml-apis"
		}
		compile("joda-time:joda-time-hibernate:1.3") {
			excludes "joda-time", "hibernate"
		}
	}
	plugins {
		build ":tomcat:${grailsVersion}"
		runtime ":hibernate:${grailsVersion}"
		test ":build-test-data:1.1.1"
		test ":fixtures:1.0.7"
		test ":geb:0.5.1"
		test ":spock:0.6-groovy-1.8-SNAPSHOT"
	}
}

grails.plugin.location.'joda-time'="../../.."