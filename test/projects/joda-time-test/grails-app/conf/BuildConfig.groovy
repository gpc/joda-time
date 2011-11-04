grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
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
		test("org.seleniumhq.selenium:selenium-htmlunit-driver:2.9.0") {
			excludes "xml-apis"
		}
		test "org.seleniumhq.selenium:selenium-support:2.9.0"
		test "org.codehaus.geb:geb-spock:0.6.1"
		compile "org.jadira.usertype:usertype.jodatime:1.9"
	}
	plugins {
		build ":tomcat:${grailsVersion}"
		runtime ":hibernate:${grailsVersion}"
		test ":build-test-data:1.1.1"
		test ":fixtures:1.0.7"
		test ":geb:0.6.1"
		test ":spock:0.6-SNAPSHOT"
	}
}

grails.plugin.location.'joda-time' = "../../.."